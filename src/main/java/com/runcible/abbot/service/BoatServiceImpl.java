package com.runcible.abbot.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.runcible.abbot.model.Boat;
import com.runcible.abbot.model.Fleet;
import com.runcible.abbot.model.FleetSelector;
import com.runcible.abbot.repository.BoatRepository;
import com.runcible.abbot.service.audit.AuditEventType;
import com.runcible.abbot.service.audit.AuditService;
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
        
        auditEvent(boat, AuditEventType.UPDATED);
    }

    @Override
    public void addBoat(Integer raceSeriesId, Boat boat) throws NoSuchRaceSeries, NoSuchUser, UserNotPermitted
    {
        throwIfUserNotPermitted(raceSeriesId);
        boat.setRaceSeriesID(raceSeriesId);
        boatRepo.save(boat);
        
        auditEvent(boat, AuditEventType.CREATED);
    }

    @Override
    public Boat getBoatByID(Integer boatId) throws NoSuchBoat, NoSuchUser, UserNotPermitted
    {
        Optional<Boat> found = boatRepo.findById(boatId);
        
        if ( !found.isPresent() )
        {
        	throw new NoSuchBoat();
        }
        
        throwIfUserNotPermitted(found.get().getRaceSeriesID());
        return found.get();
    }

    @Override
    public void removeBoat(Integer boatId) throws NoSuchCompetition, NoSuchUser, UserNotPermitted, NoSuchBoat
    {
        Optional<Boat> found = boatRepo.findById(boatId);

        if ( ! found.isPresent() )
        {
        	throw new NoSuchBoat();
        }

        throwIfUserNotPermitted(found.get().getRaceSeriesID());
        boatRepo.deleteById(found.get().getId());
        
        auditEvent(found.get(), AuditEventType.DELETED);
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

    private void auditEvent(Boat boat, AuditEventType eventType)
            throws NoSuchUser, UserNotPermitted
    {
        audit.auditEvent(
                eventType, 
                boat.getRaceSeriesID(), 
                BOAT_OBJECT_NAME, 
                boat.getName());
    }


    private static final String BOAT_OBJECT_NAME = "Boat";
    
    @Autowired
    private BoatRepository boatRepo;
    
    @Autowired
    private FleetService fleetService;
    
    @Autowired
    private AuditService audit;
}
