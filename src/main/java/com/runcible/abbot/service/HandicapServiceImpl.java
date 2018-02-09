package com.runcible.abbot.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.runcible.abbot.model.Boat;
import com.runcible.abbot.model.Handicap;
import com.runcible.abbot.model.HandicapLimit;
import com.runcible.abbot.model.Race;
import com.runcible.abbot.model.RaceResult;
import com.runcible.abbot.model.ResultType;
import com.runcible.abbot.repository.HandicapLimitsRepository;
import com.runcible.abbot.repository.HandicapRepository;
import com.runcible.abbot.service.exceptions.InvalidUpdate;
import com.runcible.abbot.service.exceptions.HandicapLimitAlreadyPresent;
import com.runcible.abbot.service.exceptions.NoSuchFleet;
import com.runcible.abbot.service.exceptions.NoSuchHandicapLimit;
import com.runcible.abbot.service.exceptions.NoSuchUser;
import com.runcible.abbot.service.exceptions.UserNotPermitted;
import com.runcible.abbot.service.points.RaceResultComparator;

@Component
public class HandicapServiceImpl extends AuthorizedService implements HandicapService
{

    @Override
    public List<Handicap> getHandicapsForFleet(
            Integer     raceSeriesID,
            Integer     fleetId,
            Integer     raceID ) throws NoSuchUser, UserNotPermitted, NoSuchFleet
    {
        List<Handicap> handicaps = new ArrayList<Handicap>();
        List<Boat> boats = boatService.getAllBoatsInFleetForSeries(raceSeriesID, fleetId);
        
        for(Boat boat : boats)
        {
            Handicap found = handicapRepo.findByBoatID(boat.getId());
            
            if (found == null )
            {
                handicaps.add(new Handicap(null,boat.getId(),0.0f));
            }
            else
            {
                handicaps.add(found);
            }
        }
        
        return handicaps;
    }

    @Override
    public void updateHandicapsFromResults(Integer raceID) throws NoSuchUser, UserNotPermitted
    {
        //
        //  Handicap adjustments are only made to boats that competed in the race
        //
        List<RaceResult> raceResults = raceResultService.findAll(raceID);
        
        //
        //  Get the handicap limit. Round to an int value for this handicap scheme
        //
        int limit = getHandicapLimit(raceID).intValue();
        
        Race thisRace = raceService.getRaceByID(raceID);
        
        //
        //  For each result, go see if any have won three times before. If so 
        //  their adjustment for winning changes
        //
        Map<Integer,Boolean> thirdWinMap = findThirdWinBoats(raceResults,thisRace);
        
        //
        //  Sort the results so the first result is the best and so on
        //
        Collections.sort(
                raceResults,
                new RaceResultComparator(ResultType.HANDICAP_RESULT));

        
        //
        //  The pushout is the amount of time we have to add to everybody's
        //  handicap. This happens if the reduction of a handicap would take
        //  a competitor below zero
        //
        int pushOut = findPushOut(raceResults,thirdWinMap);
        
        int place = 1;
        
        for(RaceResult result : raceResults)
        {
            int adjustedHandicap = result.getHandicap();
            
            if ( result.getStatus().isFinished() )
            {
                adjustedHandicap -= handicapUpdateForResult(
                        place,
                        result,
                        thirdWinMap.get(result.getBoat().getId()));
                
                place++;
            }
            
            if ( result.getStatus().isStarted() )
            {
                adjustedHandicap += pushOut;

                if ( adjustedHandicap > limit )
                {
                    adjustedHandicap = limit;
                }
                
                updateHandicap(result.getBoat(),adjustedHandicap);
            }
            
        }
    }

    private Map<Integer, Boolean> findThirdWinBoats(
            List<RaceResult> raceResults, Race thisRace)
    {
        Map<Integer,Boolean> thirdWinMap = new HashMap<Integer,Boolean>();
        
        for (RaceResult nextResult : raceResults)
        {
            if ( nextResult.getStatus().isStarted() )
            {
                int wins = raceResultService.getWinsForBoatBeforeDate(
                        thisRace.getRaceSeriesId(), 
                        thisRace.getFleet().getId(), 
                        nextResult.getBoat().getId(), 
                        thisRace.getRaceDate());
            
                thirdWinMap.put(nextResult.getBoat().getId(),(wins >= 2));
            }
        }
        
        return thirdWinMap;
    }

    //
    //  Get the handicap limit for the fleet that raced in the 
    //  race with the ID specified
    //
    private Float getHandicapLimit(Integer raceID) throws NoSuchUser, UserNotPermitted
    {
        //
        //  Get the race so we can find the fleet etc
        //
        Race race = raceService.getRaceByID(raceID);
                
        //
        //  Get the handicap limit for the fleet in this race
        //
        HandicapLimit limit = getHandicapLimitForFleet(race.getRaceSeriesId(), race.getFleet().getId());
        return limit.getLimit();
    }

    @Override
    public HandicapLimit getHandicapLimitForFleet(
            Integer raceSeriesID,
            Integer fleetID)
    {
        return handicapLimitsRepo.findByFleetID(raceSeriesID, fleetID);
    }

    @Override
    public HandicapLimit getHandicapLimit(Integer raceSeriesID, Integer id) throws NoSuchUser, UserNotPermitted
    {
    	HandicapLimit limit = handicapLimitsRepo.findOne(id);
    	
    	throwIfUserNotPermitted(limit.getRaceSeriesID());
    	if ( raceSeriesID != limit.getRaceSeriesID() )
    	{
    		throw new UserNotPermitted();
    	}
    	
    	return limit;
    }

    @Override
    public void addHandicapLimit(Integer raceSeriesID, HandicapLimit limit) throws NoSuchUser, UserNotPermitted, HandicapLimitAlreadyPresent
    {
        throwIfUserNotPermitted(raceSeriesID);
        if ( handicapLimitsRepo.findByFleetID(raceSeriesID, limit.getFleet().getId()) != null )
        {
        	throw new HandicapLimitAlreadyPresent("Handicap limit for fleet "+limit.getFleet().getFleetName()+" already present");
        }
        
        limit.setRaceSeriesID(raceSeriesID);
        
        handicapLimitsRepo.save(limit);
    }

    @Override
    public void updateHandicapLimit(HandicapLimit limit) throws NoSuchUser, UserNotPermitted, InvalidUpdate
    {
        throwIfUserNotPermitted(limit.getRaceSeriesID());
        HandicapLimit foundLimit = handicapLimitsRepo.findOne(limit.getId());
        if ( 	foundLimit.getFleet().getId() != limit.getFleet().getId() 
        		||
        		foundLimit.getRaceSeriesID() != limit.getRaceSeriesID() )
        {
        	throw new InvalidUpdate();
        }

        handicapLimitsRepo.save(limit);
    }

    @Override
    public Page<HandicapLimit> getHandicapLimits(Integer raceSeriesID, Pageable p)
    {
    	return handicapLimitsRepo.getHandicapLimits(raceSeriesID, p);
    }

    @Override
    public void removeHandicapLimit(Integer id) throws NoSuchHandicapLimit, NoSuchUser, UserNotPermitted
    {
        HandicapLimit limit = handicapLimitsRepo.findOne(id);
        if ( limit == null )
        {
        	throw new NoSuchHandicapLimit();
        }
        throwIfUserNotPermitted(limit.getRaceSeriesID());
    	
    	handicapLimitsRepo.delete(id);
    }
    
    private void updateHandicap(Boat boat, int adjustedHandicap)
    {
        Handicap handicap = handicapRepo.findByBoatID(boat.getId());
        if ( handicap == null )
        {
            handicap = new Handicap(null,boat.getId(),new Float(adjustedHandicap));
        }
        else
        {
            handicap.setValue(new Float(adjustedHandicap));
        }
        handicapRepo.save(handicap);
    }

    private int findPushOut(List<RaceResult> raceResults, Map<Integer, Boolean> thirdWinMap)
    {
        int maxPushOut = 0;
        
        for(int i=0;(i<3) && (i<raceResults.size());i++)
        {
            RaceResult nextResult = raceResults.get(i);

            if ( ! nextResult.getStatus().isFinished() )
            {
                break;
            }
            
            int adjustment = handicapUpdateForResult(i+1,nextResult,thirdWinMap.get(nextResult.getBoat().getId()));
            int pushOut=0;
            
            if ( adjustment > nextResult.getHandicap() )
            {
                pushOut = adjustment-nextResult.getHandicap();
            }
            
            if ( pushOut > maxPushOut )
            {
                maxPushOut = pushOut;
            }
        }
        
        return maxPushOut;
    }

    private int handicapUpdateForResult(int place, RaceResult result, Boolean hadThreeWins)
    {
        int adjust=0;
        if ( result.getStatus().isFinished() )
        {
            switch(place)
            {
            case 1:
                if ( hadThreeWins )
                {
                    adjust = 4;
                }
                else
                {
                    adjust = 3;
                }
                break;
            case 2:
                adjust = 2;
                break;
            case 3:
                adjust = 1;
                break;
            }
        }
        
        return adjust;
    }
    
    @Autowired
    private BoatService boatService;

    @Autowired
    private RaceResultService raceResultService;


    @Autowired
    private HandicapRepository handicapRepo;

    @Autowired
    private HandicapLimitsRepository handicapLimitsRepo;

    @Autowired
    private RaceService raceService;
}
