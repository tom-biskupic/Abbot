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
import com.runcible.abbot.service.audit.AuditEventType;
import com.runcible.abbot.service.audit.AuditService;
import com.runcible.abbot.service.exceptions.DuplicateResult;
import com.runcible.abbot.service.exceptions.HandicapLimitAlreadyPresent;
import com.runcible.abbot.service.exceptions.NoSuchFleet;
import com.runcible.abbot.service.exceptions.NoSuchHandicapLimit;
import com.runcible.abbot.service.exceptions.NoSuchRaceResult;
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

        //
        //  Find the previous race and then find the handicaps for all the boats
        //  in the fleet after that race
        //
        Integer previousRaceId = this.raceService.findPreviousFinishedRaceId(raceID);
        
        List<Boat> boats = boatService.getAllBoatsInFleetForSeries(raceSeriesID, fleetId);
        
        for(Boat boat : boats)
        {
            Handicap foundHandicap = null;

            if ( previousRaceId != null )
            {
                foundHandicap = handicapRepo.findByBoatAndRace(boat.getId(),previousRaceId);
            }
            
            if (foundHandicap == null )
            {
                handicaps.add(new Handicap(null,boat.getId(),raceID,0.0f));
            }
            else
            {
                handicaps.add(foundHandicap);
            }
        }
        
        return handicaps;
    }

    @Override
    public void updateHandicapsFromResults(Integer raceID) throws NoSuchUser, UserNotPermitted
    {
        Race thisRace = raceService.getRaceByID(raceID);

        //
        //  Handicap adjustments are only made to boats that competed in the race
        //
        List<RaceResult> raceResults = raceResultService.findAll(raceID);
        
        //
        //  Get the handicap limit. Round to an int value for this handicap scheme
        //
        Float limit = getHandicapLimit(raceID);
        
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
        Float pushOut = findPushOut(raceResults,thirdWinMap,thisRace.isShortCourseRace());
        
        int place = 1;
        
        for(RaceResult result : raceResults)
        {
            Float adjustedHandicap = result.getHandicap();
            
            if ( result.getStatus().isFinished() )
            {
                adjustedHandicap -= handicapUpdateForResult(
                        place,
                        result,
                        thirdWinMap.get(result.getBoat().getId()),
                        thisRace.isShortCourseRace());
                
                place++;
            } 
            
            if ( result.getStatus().isStarted() )
            {
                adjustedHandicap += pushOut;

                if ( limit > 0.0f && adjustedHandicap > limit )
                {
                    adjustedHandicap = limit;
                }
                
                updateHandicap(result.getBoat(),raceID,adjustedHandicap);
            }
            else
            {
                //
                //  Just carry the previous handicap forward with no adjustment
                //
                updateHandicap(result.getBoat(),raceID,result.getHandicap());
            }
        }
        
        audit.auditEventFreeForm(
                AuditEventType.UPDATED, 
                thisRace.getRaceSeriesId(), 
                "Handicaps as a result of the race "+thisRace.getName());
    }

    public void updateHandicapsFromPreviousRace(Integer raceId) throws NoSuchUser, UserNotPermitted, NoSuchRaceResult, DuplicateResult, NoSuchFleet
    {
        //
        // This will throw if we are not permitted to manage this race
        //
        Race race = raceService.getRaceByID(raceId);
        List<Handicap> handicaps = getHandicapsForFleet(race.getRaceSeriesId(),race.getFleet().getId(),raceId);
        
        List<RaceResult> existingResults = raceResultService.findAll(raceId);

        for(RaceResult result : existingResults)
        {
            if ( ! result.getOverrideHandicap() )
            {
                Float handicapFromPreviousRace = findHandicap(result.getBoat(),handicaps);
                if ( handicapFromPreviousRace != result.getHandicap() )
                {
                    result.setHandicap(handicapFromPreviousRace);
                    raceResultService.updateResult(result);
                }
            }
        }
    }

    private Float findHandicap(Boat boat, List<Handicap> handicaps)
    {
        for(Handicap handicap : handicaps)
        {
            if ( handicap.getBoatID().equals(boat.getId()) )
            {
                return handicap.getValue();
            }
        }
        
        return 0.0f;
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
                        thisRace.getRaceDate(),
                        thisRace.isShortCourseRace());
            
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
        
        //
        // Return zero if no limit has been specified
        //
        if ( limit != null )
        {
            //
            //  If this is a short course race then the limit is half the specified limit
            //
            if ( race.isShortCourseRace() )
            {
                return limit.getLimit()/2.0f;
            }
            else
            {
                return limit.getLimit();
            }
        }
        else
        {
            return 0.0f;
        }
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
        
        audit.auditEvent(
                AuditEventType.CREATED, 
                raceSeriesID, 
                HANDICAP_LIMIT_OBJECT_NAME, 
                limit.getFleet().getFleetName() );
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
        
        audit.auditEvent(
                AuditEventType.UPDATED, 
                limit.getRaceSeriesID(), 
                HANDICAP_LIMIT_OBJECT_NAME, 
                limit.getFleet().getFleetName() );

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
    	
        audit.auditEvent(
                AuditEventType.DELETED, 
                limit.getRaceSeriesID(), 
                HANDICAP_LIMIT_OBJECT_NAME, 
                limit.getFleet().getFleetName() );

    }
    
    private void updateHandicap(Boat boat, Integer raceID, Float adjustedHandicap)
    {
        Handicap handicap = handicapRepo.findByBoatAndRace(boat.getId(),raceID);
        
        if ( handicap == null )
        {
            handicap = new Handicap(null,boat.getId(),raceID,adjustedHandicap);
        }
        else
        {
            handicap.setValue(adjustedHandicap);
        }
        
        handicapRepo.save(handicap);
    }

    private Float findPushOut(
            List<RaceResult>        raceResults, 
            Map<Integer, Boolean>   thirdWinMap,
            boolean                 shortCourse )
    {
        Float maxPushOut = 0.0f;
        
        for(int i=0;(i<3) && (i<raceResults.size());i++)
        {
            RaceResult nextResult = raceResults.get(i);

            if ( ! nextResult.getStatus().isFinished() )
            {
                break;
            }
            
            Float adjustment = handicapUpdateForResult(
                    i+1,
                    nextResult,
                    thirdWinMap.get(nextResult.getBoat().getId()),
                    shortCourse);
            Float pushOut=0.0f;
            
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

    private Float handicapUpdateForResult(
            int         place, 
            RaceResult  result, 
            Boolean     hadThreeWins,
            Boolean     shortCourse)
    {
        Float adjust=0.0f;
        if ( result.getStatus().isFinished() )
        {
            switch(place)
            {
            case 1:
                if ( hadThreeWins )
                {
                    adjust = 4.0f;
                }
                else
                {
                    adjust = 3.0f;
                }
                break;
            case 2:
                adjust = 2.0f;
                break;
            case 3:
                adjust = 1.0f;
                break;
            }
        }
        
        if ( shortCourse )
        {
            return adjust/2.0f;
        }
        else
        {
            return adjust;
        }
    }
    
    private static final String HANDICAP_OBJECT_NAME="Handicap";
    private static final String HANDICAP_LIMIT_OBJECT_NAME="Handicap Limit";
    
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
    
    @Autowired
    private AuditService audit;
}
