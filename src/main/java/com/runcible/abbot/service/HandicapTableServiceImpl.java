package com.runcible.abbot.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.runcible.abbot.model.Boat;
import com.runcible.abbot.model.BoatHandicaps;
import com.runcible.abbot.model.Handicap;
import com.runcible.abbot.model.HandicapTable;
import com.runcible.abbot.model.Race;
import com.runcible.abbot.model.RaceStatus;
import com.runcible.abbot.service.exceptions.NoSuchFleet;
import com.runcible.abbot.service.exceptions.NoSuchUser;
import com.runcible.abbot.service.exceptions.UserNotPermitted;

@Component
public class HandicapTableServiceImpl implements HandicapTableService
{

    @Override
    public HandicapTable getHandicapTable(
            Integer raceSeriesId, 
            Integer fleetId,
            boolean shortCourse ) throws NoSuchUser, UserNotPermitted, NoSuchFleet
    {
        HandicapTable handicapTable = new HandicapTable(fleetService.getFleetByID(fleetId));
        
        List<Race> races = raceService.getRacesForFleet(raceSeriesId,fleetId);
        races = findCompletedRaces(races);
        races = findRacesOfType(races,shortCourse);
        handicapTable.setRaces(races);
        
        List<Boat> boatsInFleet = boatService.getAllBoatsInFleetForSeries(raceSeriesId,fleetId);
        
        Map<Integer,BoatHandicaps> boatHandicapMap = new HashMap<Integer,BoatHandicaps>();
        
        for(Boat boat : boatsInFleet )
        {
            boatHandicapMap.put(boat.getId(),new BoatHandicaps(boat));
        }
        
        int raceCount=1;
        for(Race race : races )
        {
            List<Handicap> handicapsForRace = handicapService.getHandicapsForFleet(raceSeriesId,fleetId,race.getId());
            
            for(Handicap handicap : handicapsForRace)
            {
                BoatHandicaps foundBoatHandicaps = boatHandicapMap.get(handicap.getBoatID());
                foundBoatHandicaps.getHandicaps().add(handicap.getValue());
            }
            
            for(BoatHandicaps boatHandicap : boatHandicapMap.values() )
            {
                List<Float> handicaps = boatHandicap.getHandicaps();
                if ( handicaps.size() < raceCount )
                {
                    handicaps.add(0.0f);
                }
            }
        }
        
        handicapTable.setBoatHandicaps(new ArrayList<BoatHandicaps>(boatHandicapMap.values()));
        
        return handicapTable;
    }

    private List<Race> findRacesOfType(List<Race> races, boolean shortCourse)
    {
        List<Race> completedRaces = new ArrayList<Race>();
        
        for(Race race : races )
        {
            if (race.isShortCourseRace() == shortCourse )
            {
                completedRaces.add(race);
            }
        }
        return completedRaces;
    }

    private List<Race> findCompletedRaces(List<Race> races)
    {
        List<Race> completedRaces = new ArrayList<Race>();
        
        for(Race race : races )
        {
            if (race.getRaceStatus() == RaceStatus.COMPLETED )
            {
                completedRaces.add(race);
            }
        }
        return completedRaces;
    }

    private @Autowired RaceService      raceService;
    private @Autowired BoatService      boatService;
    private @Autowired FleetService     fleetService;
    private @Autowired HandicapService  handicapService;
}
