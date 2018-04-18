package com.runcible.abbot.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.runcible.abbot.model.Competition;
import com.runcible.abbot.model.RaceSeries;
import com.runcible.abbot.repository.CompetitionRepository;
import com.runcible.abbot.service.audit.AuditEventType;
import com.runcible.abbot.service.audit.AuditService;
import com.runcible.abbot.service.exceptions.NoSuchCompetition;
import com.runcible.abbot.service.exceptions.NoSuchRaceSeries;
import com.runcible.abbot.service.exceptions.NoSuchUser;
import com.runcible.abbot.service.exceptions.UserNotPermitted;

@Component
@Transactional
public class CompetitionServiceImpl extends AuthorizedService implements CompetitionService
{
    
    @Override
    public Page<Competition> getAllCompetitionsForSeries(Integer raceSeriesId, Pageable pageable) throws NoSuchUser, UserNotPermitted
    {
        throwIfUserNotPermitted(raceSeriesId);
        return competitionRepo.findByRaceSeries(raceSeriesId, pageable);
    }
    
    @Override
    public List<Competition> getAllCompetitionsForSeries(Integer raceSeriesId) throws NoSuchUser, UserNotPermitted
    {
        throwIfUserNotPermitted(raceSeriesId);
        return competitionRepo.findByRaceSeries(raceSeriesId);
    }

    @Override
    public void updateCompetition(Competition competition) throws NoSuchUser, UserNotPermitted
    {
        throwIfUserNotPermitted(competition.getRaceSeriesId());
        competitionRepo.save(competition);
        
        auditEvent(competition, AuditEventType.UPDATED);
    }


    @Override
    public void addCompetition(Integer raceSeriesId, Competition competition) throws NoSuchRaceSeries, NoSuchUser, UserNotPermitted
    {
        throwIfUserNotPermitted(raceSeriesId);
        competition.setRaceSeriesId(raceSeriesId);
        
        competitionRepo.save(competition);
        
        auditEvent(competition, AuditEventType.CREATED);
    }

    @Override
    public Competition getCompetitionByID(Integer competitionId) throws NoSuchCompetition, NoSuchUser, UserNotPermitted
    {
        Competition found = competitionRepo.findOne(competitionId);
        if ( found == null )
        {
            throw new NoSuchCompetition();
        }
        
        throwIfUserNotPermitted(found.getRaceSeriesId());
        
        return found;
    }
    
    @Override
    public void removeCompetition(Integer competitionId) throws NoSuchCompetition, NoSuchUser, UserNotPermitted
    {
        Competition found = competitionRepo.findOne(competitionId);
        if ( found == null )
        {
            throw new NoSuchCompetition();
        }
    
        throwIfUserNotPermitted(found.getRaceSeriesId());

        competitionRepo.delete(competitionId);
        
        auditEvent(found, AuditEventType.DELETED);
    }

    private void auditEvent(
            Competition     competition, 
            AuditEventType  eventType) throws NoSuchUser, UserNotPermitted
    {
        audit.auditEvent(
                eventType, 
                competition.getRaceSeriesId(), 
                COMPETITION_OBJECT_NAME, 
                competition.getName());
    }

    private static final String COMPETITION_OBJECT_NAME = "Competition";
    
    @Autowired
    private RaceSeriesService raceSeriesService;
    
    @Autowired
    private CompetitionRepository competitionRepo;

    @Autowired
    private AuditService audit;
}
