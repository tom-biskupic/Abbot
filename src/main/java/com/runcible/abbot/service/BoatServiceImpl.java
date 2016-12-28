package com.runcible.abbot.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.runcible.abbot.model.Boat;
import com.runcible.abbot.model.Fleet;
import com.runcible.abbot.model.FleetSelector;
import com.runcible.abbot.repository.BoatRepository;
import com.runcible.abbot.service.exceptions.NoSuchBoat;
import com.runcible.abbot.service.exceptions.NoSuchCompetition;
import com.runcible.abbot.service.exceptions.NoSuchFleet;
import com.runcible.abbot.service.exceptions.NoSuchRaceSeries;
import com.runcible.abbot.service.exceptions.NoSuchUser;
import com.runcible.abbot.service.exceptions.UserNotPermitted;

@Component
@Transactional
public class BoatServiceImpl extends AuthorizedService implements BoatService
{
    @Override
    public Page<Boat> getAllBoatsForSeries(Integer raceSeriesId, Pageable p) throws NoSuchUser, UserNotPermitted
    {
        throwIfUserNotPermitted(raceSeriesId);
        return boatRepo.findByRaceSeries(raceSeriesId, p);
    }

    @Override
    public void updateBoat(Boat boat) throws NoSuchUser, UserNotPermitted
    {
        throwIfUserNotPermitted(boat.getRaceSeriesID());
        boatRepo.save(boat);
    }

    @Override
    public void addBoat(Integer raceSeriesId, Boat boat) throws NoSuchRaceSeries, NoSuchUser, UserNotPermitted
    {
        throwIfUserNotPermitted(raceSeriesId);
        boat.setRaceSeriesID(raceSeriesId);
        boatRepo.save(boat);
    }

    @Override
    public Boat getBoatByID(Integer boatId) throws NoSuchBoat, NoSuchUser, UserNotPermitted
    {
        Boat found = boatRepo.findOne(boatId);
        
        if ( found == null )
        {
        	throw new NoSuchBoat();
        }
        
        throwIfUserNotPermitted(found.getRaceSeriesID());
        return found;
    }

    @Override
    public void removeBoat(Integer boatId) throws NoSuchCompetition, NoSuchUser, UserNotPermitted, NoSuchBoat
    {
        Boat found = boatRepo.findOne(boatId);

        if ( found == null )
        {
        	throw new NoSuchBoat();
        }

        throwIfUserNotPermitted(found.getRaceSeriesID());
        boatRepo.delete(found);
    }

    @Override
	public List<Boat> getAllBoatsInFleetForSeries(Integer raceSeriesId, Integer fleetId) throws NoSuchUser, UserNotPermitted, NoSuchFleet
	{
        throwIfUserNotPermitted(raceSeriesId);
        Fleet fleet = fleetService.getFleetByID(fleetId);
        
        List<Boat> returnList = new ArrayList<Boat>();
        
        for(FleetSelector fs : fleet.getFleetClasses() )
        {
        	List<Boat> nextList = null;
        	if ( fs.getBoatDivision() != null )
        	{
	        	nextList = boatRepo.findBoatByClassAndDivision(
	        			raceSeriesId, 
	        			fs.getBoatClass().getId(), 
	        			fs.getBoatDivision().getId());
        	}
        	else
        	{
	        	nextList = boatRepo.findBoatByClass(
	        			raceSeriesId, 
	        			fs.getBoatClass().getId()); 
        	}
        	
        	returnList.addAll(nextList);
        }
        
    	return returnList;
	}

    @Autowired
    private BoatRepository boatRepo;
    
    @Autowired
    private FleetService fleetService;
}
