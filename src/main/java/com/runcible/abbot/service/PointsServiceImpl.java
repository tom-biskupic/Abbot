package com.runcible.abbot.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.print.attribute.standard.Finishings;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.runcible.abbot.model.Boat;
import com.runcible.abbot.model.Competition;
import com.runcible.abbot.model.PointsForBoat;
import com.runcible.abbot.model.PointsSystem;
import com.runcible.abbot.model.PointsTable;
import com.runcible.abbot.model.Race;
import com.runcible.abbot.model.RaceResult;
import com.runcible.abbot.model.ResultStatus;
import com.runcible.abbot.model.ResultType;
import com.runcible.abbot.service.exceptions.NoSuchCompetition;
import com.runcible.abbot.service.exceptions.NoSuchFleet;
import com.runcible.abbot.service.exceptions.NoSuchUser;
import com.runcible.abbot.service.exceptions.UserNotPermitted;

@Component
public class PointsServiceImpl implements PointsService
{
    @Override
    public PointsTable generatePointsTable(
            Integer raceSeriesID,
            Integer competitionID) throws NoSuchCompetition, NoSuchUser, UserNotPermitted, NoSuchFleet
    {
        Competition competition = competitionService.getCompetitionByID(competitionID);
        PointsTable points = new PointsTable(competition);
        
        List<Race> races = raceService.getRacesInCompetition(competition);
        points.setRaces(races);
        
        List<Boat> boats = boatService.getAllBoatsInFleetForSeries(
                raceSeriesID, 
                competition.getFleet().getId());
        
        Map<Boat,PointsForBoat> boatPoints = makeBointPoints(boats);
        
        for( Race race : races )
        {
            addPointsForRace(boatPoints,race,competition);
        }
        
        return points;
    }
    
    private void addPointsForRace(
            Map<Boat,PointsForBoat> boatPoints, 
            Race                    race,
            Competition             competition ) throws NoSuchUser, UserNotPermitted
    {
        //
        //  Get results for this race
        //
        List<RaceResult> results = raceResultService.findAll(race.getId());
        
        //
        //  Sort the results by either handicap place or scratch place depending
        //  on the competition settings
        //
        sortResults(competition, results);
        
        int numberOfStarters = countNumberOfStarters(results);
        
        int place=1;
        for ( RaceResult result : results )
        {
            if ( ! isStarted(result) )
            {
                boatPoints.get(result.getBoat()).getPoints().add(
                        calcPoints(competition.getPointsSystem(),competition.getFleetSize()));
            }
            else if ( !isFinished(result) )
            {
                boatPoints.get(result.getBoat()).getPoints().add(
                        calcPoints(competition.getPointsSystem(),numberOfStarters));
            }
            else
            {
                boatPoints.get(result.getBoat()).getPoints().add(
                        calcPoints(competition.getPointsSystem(),place++));
            }
        }
    }

    private Float calcPoints(PointsSystem pointsSystem, Integer fleetSize)
    {
        return null;
    }

    private int countNumberOfFinishers(List<RaceResult> results)
    {
        int count = 0;
        for (RaceResult result : results)
        {
            if (isFinished(result))
            {
                count++;
            }
        }
        
        return count;
    }

    private boolean isFinished(RaceResult result)
    {
        return isStarted(result)
                &&
                result.getStatus() != ResultStatus.DNF;
    }

    private int countNumberOfStarters(List<RaceResult> results)
    {
        int count = 0;
        for (RaceResult result : results)
        {
            if ( isStarted(result) )
            {
                count++;
            }
        }
        
        return count;
    }

    private boolean isStarted(RaceResult result)
    {
        return result.getStatus() != ResultStatus.DNS 
                &&
                result.getStatus() != ResultStatus.DNC;
    }

    private void sortResults(Competition competition, List<RaceResult> results)
    {
        if ( competition.getResultType() == ResultType.HANDICAP_RESULT )
        {
            Collections.sort(
                    results,
                    new Comparator<RaceResult>() 
                    {
                        @Override
                        public int compare(RaceResult left, RaceResult right)
                        {
                            return differenceToInt(right.getCorrectedTime() - left.getCorrectedTime());
                        }
                    });
        }
        else
        {
            Collections.sort(
                    results,
                    new Comparator<RaceResult>() 
                    {
                        @Override
                        public int compare(RaceResult left, RaceResult right)
                        {
                            return differenceToInt(right.getSailingTime() - left.getSailingTime());
                        }
                    });
        }
    }

    protected int differenceToInt(int difference)
    {
        if ( difference > 0 )
        {
            return 1;
        }
        else if ( difference < 0 )
        {
            return -1;
        }
        return 0;
    }

    private Map<Boat, PointsForBoat> makeBointPoints(List<Boat> boats)
    {
        Map<Boat,PointsForBoat> boatPoints = new HashMap<Boat,PointsForBoat>();
        
        for(Boat boat : boats )
        {
            boatPoints.put(boat,new PointsForBoat(boat));
        }
        
        return boatPoints;
    }

    @Autowired
    private CompetitionService competitionService;
    
    @Autowired
    private RaceService raceService;
    
    @Autowired
    private BoatService boatService;
    
    @Autowired
    private RaceResultService raceResultService;

}
