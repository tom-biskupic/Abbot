package com.runcible.abbot.service;

import com.runcible.abbot.model.PointsTable;
import com.runcible.abbot.service.exceptions.NoSuchCompetition;
import com.runcible.abbot.service.exceptions.NoSuchFleet;
import com.runcible.abbot.service.exceptions.NoSuchUser;
import com.runcible.abbot.service.exceptions.UserNotPermitted;

public interface PointsService
{
    /**
     * Generates a points table containing points for each competitor in
     * each race in the compeition.
     * 
     * @param raceSeriesID The race series the competition is part of
     * @param competitionID the competition the race series is part of
     * @return The points table
     * @throws UserNotPermitted 
     * @throws NoSuchUser 
     * @throws NoSuchCompetition 
     * @throws NoSuchFleet 
     */
    public PointsTable generatePointsTable(
            Integer raceSeriesID,
            Integer competitionID ) throws NoSuchCompetition, NoSuchUser, UserNotPermitted, NoSuchFleet;
}
