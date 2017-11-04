package com.runcible.abbot.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.runcible.abbot.model.Boat;
import com.runcible.abbot.model.Handicap;
import com.runcible.abbot.model.Race;
import com.runcible.abbot.model.RaceResult;
import com.runcible.abbot.model.ResultType;
import com.runcible.abbot.repository.HandicapRepository;
import com.runcible.abbot.service.exceptions.NoSuchFleet;
import com.runcible.abbot.service.exceptions.NoSuchUser;
import com.runcible.abbot.service.exceptions.UserNotPermitted;
import com.runcible.abbot.service.points.RaceResultComparator;
import com.runcible.abbot.service.points.RaceResultSorter;
import com.runcible.abbot.service.points.RaceResultSorterImpl;

public class HandicapServiceImpl implements HandicapService
{

    @Override
    public List<Handicap> getHandicapsForFleet(Integer raceSeriesID, Integer fleetId) 
            throws NoSuchUser, UserNotPermitted, NoSuchFleet
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
        int pushOut = findPushOut(raceResults);
        
        int place = 1;
        
        for(RaceResult result : raceResults)
        {
            int adjustedHandicap = result.getHandicap();
            
            if ( result.getStatus().isFinished() )
            {
                adjustedHandicap -= handicapUpdateForResult(place,result);
                place++;
            }
            
            if ( result.getStatus().isStarted() )
            {
                adjustedHandicap += pushOut;
            }
        }
    }

    private int findPushOut(List<RaceResult> raceResults)
    {
        int maxPushOut = 0;
        
        for(int i=0;i<3;i++)
        {
            RaceResult nextResult = raceResults.get(i);

            if ( ! nextResult.getStatus().isFinished() )
            {
                break;
            }
            
            int adjustment = handicapUpdateForResult(i+1,nextResult);
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

    private int handicapUpdateForResult(int place, RaceResult result)
    {
        int adjust=0;
        if ( result.getStatus().isFinished() )
        {
            switch(place)
            {
            case 1:
                adjust = 3;
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
    private RaceService raceService;

    @Autowired
    private RaceResultService raceResultService;

    @Autowired
    private HandicapRepository handicapRepo;
}
