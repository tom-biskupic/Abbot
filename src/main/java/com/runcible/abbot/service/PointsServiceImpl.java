package com.runcible.abbot.service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.runcible.abbot.model.Boat;
import com.runcible.abbot.model.Competition;
import com.runcible.abbot.model.PointsForBoat;
import com.runcible.abbot.model.PointsTable;
import com.runcible.abbot.model.Race;
import com.runcible.abbot.model.RaceResult;
import com.runcible.abbot.model.ResultStatus;
import com.runcible.abbot.model.ResultType;
import com.runcible.abbot.service.exceptions.NoSuchCompetition;
import com.runcible.abbot.service.exceptions.NoSuchFleet;
import com.runcible.abbot.service.exceptions.NoSuchUser;
import com.runcible.abbot.service.exceptions.UserNotPermitted;
import com.runcible.abbot.service.points.PointsCalculator;
import com.runcible.abbot.service.points.PointsSorter;
import com.runcible.abbot.service.points.PointsTotalCalculator;

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
        
        for( PointsForBoat pointsForBoat : boatPoints.values() )
        {
            pointsTotalCalculator.updateTotals(competition, pointsForBoat);
        }
        
        points.getPointsForBoat().addAll(boatPoints.values());
        
        //
        //  Finally sort the points by their total with drops (reverse)
        //
        pointsSorter.sortPoints(points);

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
        
        boolean useHandicap = competition.getResultType() == ResultType.HANDICAP_RESULT;
        
        int numberOfStarters = countNumberOfStarters(results);
        
        Set<Boat> boatsDone = new HashSet<Boat>();
        
        for ( RaceResult result : results )
        {
            
        	Boat boat = result.getBoat();
            boatPoints.get(boat).getPoints().add(
        			pointsCalculator.calculatePoints(
        			        competition, 
        			        numberOfStarters, 
        			        useHandicap ? result.getHandicapPlace() : result.getScratchPlace(), 
        			        result.getStatus()));
            boatsDone.add(boat);
        }
        
        //
        //  Now find any boats we don't have results for and add
        //  DNS entries for them
        //
        for(Boat boat : boatPoints.keySet() )
        {
            if ( ! boatsDone.contains(boat) )
            {
                boatPoints.get(boat).getPoints().add(
                        pointsCalculator.calculatePoints(
                                competition, numberOfStarters, 0, ResultStatus.DNS));
            }
        }
    }


    private int countNumberOfStarters(List<RaceResult> results)
    {
        int count = 0;
        for (RaceResult result : results)
        {
            if ( result.getStatus().isStarted() )
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
    private PointsTotalCalculator pointsTotalCalculator;
    
    @Autowired
    private PointsSorter pointsSorter;
}
