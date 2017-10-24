package com.runcible.abbot.service;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        
        points.getPointsForBoat().addAll(boatPoints.values());
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
        Collections.sort(results,new RaceResultComparator(competition.getResultType()));
        
        int numberOfStarters = countNumberOfStarters(results);
        
        int place=1;
        for ( RaceResult result : results )
        {
            if ( ! isStarted(result) )
            {
                boatPoints.get(result.getBoat()).getPoints().add(
                        calcPoints(competition.getPointsSystem(),competition.getFleetSize()+1));
            }
            else if ( !isFinished(result) )
            {
                boatPoints.get(result.getBoat()).getPoints().add(
                        calcPoints(competition.getPointsSystem(),numberOfStarters+1));
            }
            else
            {
                boatPoints.get(result.getBoat()).getPoints().add(
                        calcPoints(competition.getPointsSystem(),place++));
            }
        }
    }

    private Float calcPoints(PointsSystem pointsSystem, Integer place)
    {
        if ( pointsSystem == PointsSystem.LOW_POINTS )
        {
            return new Float(place);
        }
        else
        {
            if ( place <= 7 )
            {
                //
                //  Table is zero index based but place is one based
                //
                return BonusPointsTable[place-1];
            }
            else
            {
                //
                //  So seventh place is 13, 8th is 14 an so on
                //
                return new Float(place+6); 
            }
        }
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
        Collections.sort(
                results,
                new RaceResultComparator(competition.getResultType()));
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

    private static final Float BonusPointsTable[] = {0.0f,3.0f,5.7f,8.0f,10.0f,11.7f,13.0f};
    
    @Autowired
    private CompetitionService competitionService;
    
    @Autowired
    private RaceService raceService;
    
    @Autowired
    private BoatService boatService;
    
    @Autowired
    private RaceResultService raceResultService;

}
