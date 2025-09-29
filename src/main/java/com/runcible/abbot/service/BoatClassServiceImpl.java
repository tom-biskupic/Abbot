package com.runcible.abbot.service;

import java.util.List;
import java.util.Optional;

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
        Optional<BoatClass> foundClass = boatClassRepo.findById(boatClassId);
        if ( !foundClass.isPresent() )
        {
            throw new NoSuchBoatClass();
        }
        
        throwIfUserNotPermitted(foundClass.get().getRaceSeriesId());

        boatClassRepo.deleteById(boatClassId);
        
        auditEvent(foundClass.get(), AuditEventType.DELETED);
    }

    @Override
    public BoatClass getBoatClassByID(Integer boatClassId) throws NoSuchBoatClass, NoSuchUser, UserNotPermitted
    {
        Optional<BoatClass> boatClass = boatClassRepo.findById(boatClassId); 
        if ( !boatClass.isPresent() )
        {
            throw new NoSuchBoatClass();
        }
        
        throwIfUserNotPermitted(boatClass.get().getRaceSeriesId());

        return boatClass.get();
    }
    
    @Override
    public void addDivision(Integer boatClassId, BoatDivision division) throws DuplicateDivision, NoSuchUser, UserNotPermitted, NoSuchBoatClass
    {
        Optional<BoatClass> boatClass = boatClassRepo.findById(boatClassId);

        if ( ! boatClass.isPresent() )
        {
            throw new NoSuchBoatClass();
        }
        
        if ( boatClass.get().getDivision(division.getName()) != null)
        {
            throw new DuplicateDivision();
        }

        throwIfUserNotPermitted(boatClass.get().getRaceSeriesId());

        boatClass.get().addDivision(division);
        boatClassRepo.save(boatClass.get());

        auditEvent(division, boatClass.get(), AuditEventType.CREATED);
    }

    @Override
    public void updateDivision(Integer boatClassId, BoatDivision division) 
            throws DuplicateDivision, NoSuchDivision, NoSuchBoatClass, NoSuchUser, UserNotPermitted
    {
        Optional<BoatClass> boatClass = boatClassRepo.findById(boatClassId);

        if ( ! boatClass.isPresent() )
        {
            throw new NoSuchBoatClass();
        }
        
        throwIfUserNotPermitted(boatClass.get().getRaceSeriesId());

        BoatDivision foundDivision = boatClass.get().getDivision(division.getName()); 
        if ( foundDivision != null && foundDivision.getId() != division.getId() )
        {
            throw new DuplicateDivision();
        }

        foundDivision = boatClass.get().getDivision(division.getId());
        
        if ( foundDivision == null )
        {
            throw new NoSuchDivision();
        }
        
        boatClass.get().removeDivision(foundDivision);
        boatClass.get().addDivision(division);

        boatClassRepo.save(boatClass.get());
        
        auditEvent(division, boatClass.get(), AuditEventType.UPDATED);
    }
    
    @Override
    public void removeDivision(Integer boatClassId, Integer divisionId) 
            throws NoSuchDivision, NoSuchBoatClass, NoSuchUser, UserNotPermitted
    {
        Optional<BoatClass> boatClass = boatClassRepo.findById(boatClassId);

        if ( ! boatClass.isPresent() )
        {
            throw new NoSuchBoatClass();
        }

        throwIfUserNotPermitted(boatClass.get().getRaceSeriesId());
        
        BoatDivision foundDivision = boatClass.get().getDivision(divisionId);
        
        if ( foundDivision == null )
        {
            throw new NoSuchDivision();
        }
        
        boatClass.get().removeDivision(foundDivision);
        
        boatClassRepo.save(boatClass.get());
        
        auditEvent(foundDivision, boatClass.get(), AuditEventType.DELETED);
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
