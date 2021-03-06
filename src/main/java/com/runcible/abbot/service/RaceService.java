package com.runcible.abbot.service;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.runcible.abbot.model.Competition;
import com.runcible.abbot.model.Race;
import com.runcible.abbot.model.RaceDay;
import com.runcible.abbot.service.exceptions.NoSuchUser;
import com.runcible.abbot.service.exceptions.UserNotPermitted;

public interface RaceService
{
    /**
     * Returns all the races in a race series based on the pageable
     * @param seriesId The ID of the race series we are searching
     * @param p A pageageble describing what to return
     * @return A page of results
     * @throws UserNotPermitted 
     * @throws NoSuchUser 
     */
    public Page<Race> getAllRacesForSeries(Integer seriesId, Pageable p) throws NoSuchUser, UserNotPermitted;
    
    /**
     * Add a race to the database.
     * @param race The race to add
     * @throws UserNotPermitted 
     * @throws NoSuchUser 
     */
    public void addRace(Integer raceSeriesId, Race race) throws NoSuchUser, UserNotPermitted;

    /**
     * Updates a race
     * @param race The race to update
     * @throws UserNotPermitted 
     * @throws NoSuchUser 
     */
    public void updateRace(Race race) throws NoSuchUser, UserNotPermitted;

    /**
     * Returns the race matching the ID provided
     * @param raceID The ID of the race to return
     * @return The race
     * @throws UserNotPermitted 
     * @throws NoSuchUser 
     */
    public Race getRaceByID(Integer raceID) throws NoSuchUser, UserNotPermitted;
    
    /**
     * Removes the race matching the ID provided
     * @param raceID The ID of the race to remove
     * @throws UserNotPermitted 
     * @throws NoSuchUser 
     */
    public void removeRace(Integer raceID) throws NoSuchUser, UserNotPermitted;

    /**
     * Returns a list of RaceDays which are days on which races occur within the series
     * Each day includes a list of races that occur on that day
     * @return  A list of race days
     * @throws 	NoSuchUser If the logged on user is not valid
     * @throws 	UserNotPermitted If the logged on user is not 
     * 			permitted to perform this operation
     */
	public List<RaceDay> getRaceDays(Integer raceSeriesId) throws NoSuchUser, UserNotPermitted;
	
	/**
	 * Returns the races in the selected competition;
	 * @param competition the competition of interest
	 * @return The races in the selected competition
	 */
	public List<Race> getRacesInCompetition(Competition competition);

	/**
	 * Returns all the races for a given fleet in a race series
	 * @param raceSeriesID The race series
	 * @param fleetID The fleet ID
	 * @return The list of races for that series/fleet
	 * @throws UserNotPermitted  If the logged user is not permitted to manage this fleet
	 * @throws NoSuchUser If the logged on user is invalid 
	 */
	public List<Race> getRacesForFleet(Integer raceSeriesID, Integer fleetID) throws NoSuchUser, UserNotPermitted;
	
	/**
	 * Returns the most recent finished race before the race specified.
	 * Used to find the previous race for handicap reasons.
	 * 
	 * @param raceSeriesID The ID of the race series we are looking in
	 * @param raceID The ID of the race we are trying to find the previous race of
	 * @return The previous race or NULL if there isn't one.
	 * @throws NoSuchUser
	 * @throws UserNotPermitted
	 */
	public Integer findPreviousFinishedRaceId(Integer raceID ) throws NoSuchUser, UserNotPermitted;
}

