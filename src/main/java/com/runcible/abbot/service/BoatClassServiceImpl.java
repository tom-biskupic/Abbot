package com.runcible.abbot.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.runcible.abbot.model.BoatClass;
import com.runcible.abbot.model.BoatDivision;
import com.runcible.abbot.repository.BoatClassRepository;
import com.runcible.abbot.service.audit.AuditEventType;
import com.runcible.abbot.service.audit.AuditService;
import com.runcible.abbot.service.exceptions.DuplicateDivision;
import com.runcible.abbot.service.exceptions.NoSuchBoatClass;
import com.runcible.abbot.service.exceptions.NoSuchDivision;
import com.runcible.abbot.service.exceptions.NoSuchUser;
import com.runcible.abbot.service.exceptions.UserNotPermitted;

@Component
@Transactional
public class BoatClassServiceImpl extends AuthorizedService implements BoatClassService
{
    @Override
    public Page<BoatClass> getAllBoatClassesForSeries( 
            Integer raceSeriesID, 
            Pageable pageable ) throws NoSuchUser, UserNotPermitted
    {
        throwIfUserNotPermitted(raceSeriesID);

        return boatClassRepo.findByRaceSeries(raceSeriesID, pageable); 
    }

    public List<BoatClass> getAllBoatClassesForSeries(Integer raceSeriesId) throws NoSuchUser, UserNotPermitted
    {
        throwIfUserNotPermitted(raceSeriesId);

        return boatClassRepo.findByRaceSeries(raceSeriesId);
    }

    @Override
    public void addBoatClass(Integer raceSeriesId, BoatClass boatClass) throws NoSuchUser, UserNotPermitted
    {
        throwIfUserNotPermitted(raceSeriesId);
        boatClass.setRaceSeriesId(raceSeriesId);
        boatClassRepo.save(boatClass);
        
        auditEvent(boatClass, AuditEventType.CREATED);
    }

    @Override
    public void updateBoatClass(BoatClass boatClass) throws NoSuchUser, UserNotPermitted
    {
        throwIfUserNotPermitted(boatClass.getRaceSeriesId());
        boatClassRepo.save(boatClass);
        
        auditEvent(boatClass, AuditEventType.UPDATED);
    }

    @Override
    public void removeBoatClass(Integer boatClassId) throws NoSuchBoatClass, NoSuchUser, UserNotPermitted
    {
        BoatClass foundClass = boatClassRepo.findOne(boatClassId);
        if ( foundClass == null )
        {
            throw new NoSuchBoatClass();
        }
        
        throwIfUserNotPermitted(foundClass.getRaceSeriesId());

        boatClassRepo.delete(boatClassId);
        
        auditEvent(foundClass, AuditEventType.DELETED);
    }

    @Override
    public BoatClass getBoatClassByID(Integer boatClassId) throws NoSuchBoatClass, NoSuchUser, UserNotPermitted
    {
        BoatClass boatClass = boatClassRepo.findOne(boatClassId); 
        if ( boatClass == null )
        {
            throw new NoSuchBoatClass();
        }
        
        throwIfUserNotPermitted(boatClass.getRaceSeriesId());

        return boatClass;
    }
    
    @Override
    public void addDivision(Integer boatClassId, BoatDivision division) throws DuplicateDivision, NoSuchUser, UserNotPermitted, NoSuchBoatClass
    {
        BoatClass boatClass = boatClassRepo.findOne(boatClassId);

        if ( boatClass == null )
        {
            throw new NoSuchBoatClass();
        }
        
        if ( boatClass.getDivision(division.getName()) != null)
        {
            throw new DuplicateDivision();
        }

        throwIfUserNotPermitted(boatClass.getRaceSeriesId());

        boatClass.addDivision(division);
        boatClassRepo.save(boatClass);

        auditEvent(division, boatClass, AuditEventType.CREATED);
    }

    @Override
    public void updateDivision(Integer boatClassId, BoatDivision division) 
            throws DuplicateDivision, NoSuchDivision, NoSuchBoatClass, NoSuchUser, UserNotPermitted
    {
        BoatClass boatClass = boatClassRepo.findOne(boatClassId);

        if ( boatClass == null )
        {
            throw new NoSuchBoatClass();
        }
        
        throwIfUserNotPermitted(boatClass.getRaceSeriesId());

        BoatDivision foundDivision = boatClass.getDivision(division.getName()); 
        if ( foundDivision != null && foundDivision.getId() != division.getId() )
        {
            throw new DuplicateDivision();
        }

        foundDivision = boatClass.getDivision(division.getId());
        
        if ( foundDivision == null )
        {
            throw new NoSuchDivision();
        }
        
        boatClass.removeDivision(foundDivision);
        boatClass.addDivision(division);

        boatClassRepo.save(boatClass);
        
        auditEvent(division, boatClass, AuditEventType.UPDATED);
    }
    
    @Override
    public void removeDivision(Integer boatClassId, Integer divisionId) 
            throws NoSuchDivision, NoSuchBoatClass, NoSuchUser, UserNotPermitted
    {
        BoatClass boatClass = boatClassRepo.findOne(boatClassId);

        if ( boatClass == null )
        {
            throw new NoSuchBoatClass();
        }

        throwIfUserNotPermitted(boatClass.getRaceSeriesId());
        
        BoatDivision foundDivision = boatClass.getDivision(divisionId);
        
        if ( foundDivision == null )
        {
            throw new NoSuchDivision();
        }
        
        boatClass.removeDivision(foundDivision);
        
        boatClassRepo.save(boatClass);
        
        auditEvent(foundDivision, boatClass, AuditEventType.DELETED);
    }

    private void auditEvent(
            BoatClass       boatClass, 
            AuditEventType  eventType) throws NoSuchUser, UserNotPermitted
    {
        audit.auditEvent(
                eventType, 
                boatClass.getRaceSeriesId(), 
                BOAT_CLASS_NAME, 
                boatClass.getName());
    }
    
    private void auditEvent(
            BoatDivision    division, 
            BoatClass       boatClass,
            AuditEventType  eventType) throws NoSuchUser, UserNotPermitted
    {
        audit.auditEvent(
                eventType, 
                boatClass.getRaceSeriesId(), 
                DIVISION_NAME, 
                boatClass.getName(),
                division.getName());
    }
    
    @Autowired 
    private BoatClassRepository boatClassRepo;
    
    
    @Autowired
    private AuditService audit;
    
    private static final String DIVISION_NAME = "Division";
    private static final String BOAT_CLASS_NAME = "Boat class";
}
