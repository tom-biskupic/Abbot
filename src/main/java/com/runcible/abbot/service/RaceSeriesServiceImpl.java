package com.runcible.abbot.service;

import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.runcible.abbot.model.RaceSeries;
import com.runcible.abbot.model.User;
import com.runcible.abbot.repository.RaceSeriesRepository;
import com.runcible.abbot.service.audit.AuditEventType;
import com.runcible.abbot.service.audit.AuditService;
import com.runcible.abbot.service.exceptions.NoSuchUser;
import com.runcible.abbot.service.exceptions.UserNotPermitted;

@Component
@Transactional
public class RaceSeriesServiceImpl extends AuthorizedService implements RaceSeriesService
{

    @Override
    public Page<RaceSeries> findAll(Pageable page,User user)
    {
        return raceSeriesRepo.findByPermittedUsers(user.getId(),page);
    }

    @Override
    public RaceSeries findByID(Integer id) throws NoSuchUser, UserNotPermitted
    {
        Optional<RaceSeries> foundRaceSeries = raceSeriesRepo.findById(id);
        
        throwIfUserNotPermitted(id);
        return foundRaceSeries.get();
    }

    @Override
    public void update(RaceSeries series) throws NoSuchUser, UserNotPermitted
    {
        throwIfUserNotPermitted(series.getId());

        Date now = Calendar.getInstance().getTime();
        series.setLastUpdated(now);
        raceSeriesRepo.save(series);
        auditEvent(series, AuditEventType.UPDATED);
    }


    @Override
    public void add(RaceSeries series) throws NoSuchUser, UserNotPermitted
    {
        Date now = Calendar.getInstance().getTime();
        series.setDateCreated(now);
        series.setLastUpdated(now);
        RaceSeries savedSeries = raceSeriesRepo.save(series);
        
        raceSeriesAuthorizationService.authorizeUserForRaceSeries(savedSeries, loggedOnUserService.getLoggedOnUser());
        
        auditEvent(series, AuditEventType.CREATED);
    }

    private void auditEvent(RaceSeries series, AuditEventType eventType) throws NoSuchUser, UserNotPermitted
    {
        //
        // This doesn't use the AuditService to avoid a circular dependency
        //
        logger.info(
            String.format(
                    "%s %s %s named %s", 
                    loggedOnUserService.getLoggedOnUser().getEmail(),
                    eventType.toString(),
                    RACE_SERIES_OBJECT_NAME,
                    series.getName() ));
    }

    private static final String RACE_SERIES_OBJECT_NAME = "Race series";

    @Autowired
    private RaceSeriesRepository raceSeriesRepo;
    
    @Autowired
    private LoggedOnUserService loggedOnUserService;
    
    @Autowired
    private RaceSeriesAuthorizationService raceSeriesAuthorizationService;

    private static final Logger logger = LogManager.getLogger(AuditService.class);
}
