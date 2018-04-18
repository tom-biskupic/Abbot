package com.runcible.abbot.service.audit;

import com.runcible.abbot.service.exceptions.NoSuchUser;
import com.runcible.abbot.service.exceptions.UserNotPermitted;

public interface AuditService
{
    /**
     * Logs an event to say that an operation was completed for the item with
     * the item name specified. Additional information about 
     * @param eventType The type of the event that occured
     * @param raceSeriesId The race series this was related to
     * @param name The name of the object the event was relevant to.
     * @throws UserNotPermitted 
     * @throws NoSuchUser 
     */
    public void auditEvent(
            AuditEventType eventType,
            Integer         raceSeriesId,
            String          objectType,
            String          name) throws NoSuchUser, UserNotPermitted;
    
    /**
     * Logs an event to say that an operation was completed for the item with
     * the item name specified. Additional information about 
     * @param eventType The type of the event that occured
     * @param raceSeriesId The race series this was related to
     * @param name The name of the object the event was relevant to.
     * @param subObjectName The sub-object this is related to
     * @throws NoSuchUser 
     * @throws UserNotPermitted 
     */
    public void auditEvent(
            AuditEventType eventType,
            Integer         raceSeriesId, 
            String          objectType,
            String          name,
            String          subObjectName) throws NoSuchUser, UserNotPermitted;

    /**
     * Logs an event to say that an operation was completed for the item with
     * the name specified. This version does not log the race series so it can
     * be used for objects that are not part of a series (such as a user or a
     * race series itself). 
     * @param eventType The type of the event that occured
     * @param name The name of the object the event was relevant to.
     * @throws NoSuchUser 
     * @throws UserNotPermitted 
     */
    public void auditEvent(
            AuditEventType eventType,
            String          objectType,
            String          name) throws NoSuchUser, UserNotPermitted;
    
    /**
     * Logs a free-form audit event that includs thelogged on user, the event type,
     * the race series ID some application chosen text 
     * @param eventType The even that occured
     * @param raceSeriesId The race series this is related to
     * @param text The text to be logged 
     * @throws NoSuchUser 
     * @throws UserNotPermitted 
     */
    public void auditEventFreeForm(
            AuditEventType eventType,
            Integer         raceSeriesId, 
            String          text ) throws NoSuchUser, UserNotPermitted;

}
