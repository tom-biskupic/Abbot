package com.runcible.abbot.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.runcible.abbot.model.Boat;
import com.runcible.abbot.model.Competition;
import com.runcible.abbot.model.PointsForBoat;
import com.runcible.abbot.model.PointsTable;
import com.runcible.abbot.model.Race;
import com.runcible.abbot.model.RaceResult;
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
        results = resultSorter.sortResults(results,competition);
        
        int numberOfStarters = countNumberOfStarters(results);
        
        int place=1;
        for ( RaceResult result : results )
        {
        	boatPoints.get(result.getBoat()).getPoints().add(
        			pointsCalculator.calculatePoints(
        			        competition, numberOfStarters, place++, result));
        }
    }


    private int countNumberOfStarters(List<RaceResult> results)
    {
        int count = 0;
        for (RaceResult result : results)
        {
            if ( result.isStarted() )
            {
                count++;
            }
        }
        
        return count;
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

    @Autowired
    private PointsCalculator pointsCalculator;
    
    @Autowired
    private RaceResultSorter resultSorter;
}
