package com.runcible.abbot.service;

import com.runcible.abbot.model.HandicapTable;
import com.runcible.abbot.service.exceptions.NoSuchFleet;
import com.runcible.abbot.service.exceptions.NoSuchUser;
import com.runcible.abbot.service.exceptions.UserNotPermitted;

public interface HandicapTableService
{
    public HandicapTable getHandicapTable(
            Integer raceSeriesId, 
            Integer fleetId,
            boolean shortCourse) throws NoSuchUser, UserNotPermitted, NoSuchFleet;
}
