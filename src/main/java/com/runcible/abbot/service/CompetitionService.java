package com.runcible.abbot.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.runcible.abbot.model.Competition;
import com.runcible.abbot.service.exceptions.NoSuchCompetition;
import com.runcible.abbot.service.exceptions.NoSuchRaceSeries;
import com.runcible.abbot.service.exceptions.NoSuchUser;
import com.runcible.abbot.service.exceptions.UserNotPermitted;

public interface CompetitionService
{

    /**
     * Returns all the competitions in the series identified by raceSeriesId
     * @param raceSeriesId The ID of the race series we want the competitions for
     * @param p Pageable parameter to allow paging
     * @return A Page of results.
     * @throws UserNotPermitted The logged on user is not permitted to manage this race series
     * @throws NoSuchUser 
     */
    public abstract Page<Competition> getAllCompetitionsForSeries(
            Integer raceSeriesId, Pageable p) throws NoSuchUser,
            UserNotPermitted;

    /**
     * Updates the competition in the system
     * @param competition The competition to update
     * @throws UserNotPermitted The logged on user is not permitted to manage this race series
     * @throws NoSuchUser 
     */
    public abstract void updateCompetition(Competition competition)
            throws NoSuchUser, UserNotPermitted;

    /**
     * Adds the competition to the race series with ID specified
     * @param raceSeriesId The ID of the race series to add the competition to
     * @param competition The competition to add
     * @throws NoSuchRaceSeries The race series ID provided does not exists
     * @throws UserNotPermitted The logged on user is not permitted to manage this race series
     * @throws NoSuchUser 
     */
    public abstract void addCompetition(Integer raceSeriesId,
            Competition competition) throws NoSuchRaceSeries, NoSuchUser,
            UserNotPermitted;

    /**
     * Returns the competition with the ID specified from the system
     * @param competitionId ID of the competition to return
     * @return The competition with ID specified
     */
    public abstract Competition getCompetitionByID(Integer competitionId)
            throws NoSuchCompetition, NoSuchUser, UserNotPermitted;

    /**
     * Removes the competition with ID specified
     * @param competitionId
     * @throws NoSuchCompetition 
     * @throws UserNotPermitted The logged on user is not permitted to manage this race series
     * @throws NoSuchUser 
     */
    public abstract void removeCompetition(Integer competitionId)
            throws NoSuchCompetition, NoSuchUser, UserNotPermitted;

    /**
     * Returns all of the competitions in this race series as a list. Used to generate
     * drop down lists.
     * @param raceSeriesId The race series we are searching
     * @return The list of competitons configured for this race series
     * @throws UserNotPermitted 
     * @throws NoSuchUser  Logged on user is not valid
     */
    public abstract List<Competition> getAllCompetitionsForSeries(Integer raceSeriesId) throws NoSuchUser, UserNotPermitted;
}