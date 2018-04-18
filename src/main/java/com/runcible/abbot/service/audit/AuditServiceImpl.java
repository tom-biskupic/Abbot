package com.runcible.abbot.service.audit;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.runcible.abbot.model.RaceSeries;
import com.runcible.abbot.service.LoggedOnUserService;
import com.runcible.abbot.service.RaceSeriesService;
import com.runcible.abbot.service.exceptions.NoSuchUser;
import com.runcible.abbot.service.exceptions.UserNotPermitted;

@Component
public class AuditServiceImpl implements AuditService
{

    @Override
    public void auditEvent(
            AuditEventType  eventType, 
            Integer         raceSeriesId,
            String          objectType,
            String          name) throws NoSuchUser, UserNotPermitted
    {
        logger.info(
                String.format(
                        "%s %s named %s in Race Series %s", 
                        getUserName(),
                        eventType.toString(),
                        name,
                        getSeriesName(raceSeriesId) ));
    }

    private String getUserName() throws NoSuchUser
    {
        String userName = loggedOnUserService.getLoggedOnUser().getEmail();
        return userName;
    }

    private String getSeriesName(Integer raceSeriesId)
            throws NoSuchUser, UserNotPermitted
    {
        RaceSeries raceSeries = raceSeriesService.findByID(raceSeriesId);
        String seriesName = raceSeries.getName();
        return seriesName;
    }

    @Override
    public void auditEvent(
            AuditEventType  eventType, 
            Integer         raceSeriesId,
            String          objectType,
            String          name, 
            String          subObjectName) throws NoSuchUser, UserNotPermitted
    {
        logger.info(
                String.format(
                        "%s %s named %s/%s in Race Series %s", 
                        getUserName(),
                        eventType.toString(),
                        name,
                        subObjectName,
                        getSeriesName(raceSeriesId) ));
    }

    @Override
    public void auditEventFreeForm(
            AuditEventType  eventType,
            Integer         raceSeriesId, 
            String          text) throws NoSuchUser, UserNotPermitted
    {
        logger.info(
                String.format(
                        "%s %s %s in Race Series %s", 
                        getUserName(),
                        eventType.toString(),
                        text,
                        getSeriesName(raceSeriesId) ));
    }

    @Override
    public void auditEvent(
            AuditEventType  eventType, 
            String          objectType,
            String          name) throws NoSuchUser, UserNotPermitted
    {
        logger.info(
                String.format(
                        "%s %s %s named %s", 
                        getUserName(),
                        eventType.toString(),
                        objectType,name ));
    }

    private static final Logger logger = Logger.getLogger(AuditService.class);
    
    @Autowired LoggedOnUserService  loggedOnUserService;
    @Autowired RaceSeriesService    raceSeriesService;
}
