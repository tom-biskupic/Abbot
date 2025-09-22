package com.runcible.abbot.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.runcible.abbot.model.Fleet;
import com.runcible.abbot.repository.FleetRespository;
import com.runcible.abbot.service.audit.AuditEventType;
import com.runcible.abbot.service.audit.AuditService;
import com.runcible.abbot.service.exceptions.NoSuchFleet;
import com.runcible.abbot.service.exceptions.NoSuchRaceSeries;
import com.runcible.abbot.service.exceptions.NoSuchUser;
import com.runcible.abbot.service.exceptions.UserNotPermitted;

@Component
@Transactional
public class FleetServiceImpl extends AuthorizedService implements FleetService
{
    @Override
    public void addFleet(Integer raceSeriesId, Fleet fleet) throws NoSuchRaceSeries, NoSuchUser, UserNotPermitted
    {
        throwIfUserNotPermitted(raceSeriesId);
        
        fleet.setRaceSeriesId(raceSeriesId);
        
        fleetRepo.save(fleet);
        
        auditEvent(fleet, AuditEventType.CREATED);
    }


    @Override
    public Page<Fleet> getAllFleetsForSeries(Integer raceSeriesId, Pageable p) throws NoSuchUser, UserNotPermitted
    {
        throwIfUserNotPermitted(raceSeriesId);
        return fleetRepo.findByRaceSeries(raceSeriesId, p);     
    }
    
    @Override
    public List<Fleet> getAllFleetsForSeries(Integer raceSeriesId) throws NoSuchUser, UserNotPermitted
    {
        throwIfUserNotPermitted(raceSeriesId);

        return fleetRepo.findByRaceSeries(raceSeriesId);
    }

    @Override
    public void updateFleet(Fleet fleet) throws NoSuchUser, UserNotPermitted
    {
        throwIfUserNotPermitted(fleet.getRaceSeriesId());

        fleetRepo.save(fleet);
        
        auditEvent(fleet, AuditEventType.UPDATED);
    }
    
    @Override
    public Fleet getFleetByID(Integer fleetId) throws NoSuchFleet, NoSuchUser, UserNotPermitted
    {
        Optional<Fleet> found = fleetRepo.findById(fleetId);
        
        if ( ! found.isPresent() )
        {
            throw new NoSuchFleet();
        }

        throwIfUserNotPermitted(found.get().getRaceSeriesId());

        return found.get();
    }
    
    @Override
    public void removeFleet(Integer fleetId) throws NoSuchFleet, NoSuchUser, UserNotPermitted
    {
        Optional<Fleet> found = fleetRepo.findById(fleetId);
        
        if ( ! found.isPresent() )
        {
            throw new NoSuchFleet();
        }

        throwIfUserNotPermitted(found.get().getRaceSeriesId());

        fleetRepo.deleteById(fleetId);
        
        auditEvent(found.get(), AuditEventType.DELETED);
    }

    private void auditEvent(Fleet fleet, AuditEventType eventType)
            throws NoSuchUser, UserNotPermitted
    {
        audit.auditEvent(
                eventType, 
                fleet.getRaceSeriesId(), 
                FLEET_OBJECT_NAME, 
                fleet.getFleetName());
    }
 
    private static final String FLEET_OBJECT_NAME = "Fleet";
    
    @Autowired
    private FleetRespository fleetRepo;
    
    @Autowired
    private AuditService audit;
}
