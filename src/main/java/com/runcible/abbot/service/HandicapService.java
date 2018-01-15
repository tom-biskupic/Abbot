package com.runcible.abbot.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.runcible.abbot.model.Handicap;
import com.runcible.abbot.model.HandicapLimit;
import com.runcible.abbot.service.exceptions.InvalidUpdate;
import com.runcible.abbot.service.exceptions.HandicapLimitAlreadyPresent;
import com.runcible.abbot.service.exceptions.NoSuchFleet;
import com.runcible.abbot.service.exceptions.NoSuchHandicapLimit;
import com.runcible.abbot.service.exceptions.NoSuchUser;
import com.runcible.abbot.service.exceptions.UserNotPermitted;

public interface HandicapService
{
    /**
     * Returns a list of handicaps for each boat in the fleet specified. If a boat
     * does not have a handicap a default is chosen (zero or whatever is appropriate
     * for the current handicap system).
     * @param raceSeriesID the ID of the race series this fleet is part of
     * @param the ID of the fleet we are interested in
     * @return The list of handicaps for each boat
     * @throws NoSuchFleet 
     * @throws UserNotPermitted 
     * @throws NoSuchUser 
     */
    public List<Handicap> getHandicapsForFleet(Integer raceSeriesID,Integer fleetId) 
            throws NoSuchUser, UserNotPermitted, NoSuchFleet;

    /**
     * Updates the handicaps for the race with ID specified
     * @param raceID
     * @throws UserNotPermitted 
     * @throws NoSuchUser 
     */
    public void updateHandicapsFromResults(Integer raceID) throws NoSuchUser, UserNotPermitted;
    
    /**
     * Returns the handicap limit for the selected fleet
     * @param raceSeriesID Race series we are searching in
     * @param fleetID The ID of the fleet we are looking for the handicap limit
     * @return The handicap limit for the fleet.
     */
    public HandicapLimit getHandicapLimitForFleet(Integer raceSeriesID, Integer fleetID);

    /**
     * Returns the handicap limit with the id specified
     * @param raceSeresID the ID of the race series this limit is within 
     * @param id The id of the race series
     * @return the handicap limit
     * @throws UserNotPermitted 
     * @throws NoSuchUser 
     */
    public HandicapLimit getHandicapLimit(Integer raceSeresID, Integer id) throws NoSuchUser, UserNotPermitted;
    
    /**
     * Add the handicap limit to the race series
     * @param raceSeriesID The ID of the race series to be updated
     * @param limit The handicap limit to add
     * @throws UserNotPermitted If the logged on user is not permitted to manage this series 
     * @throws NoSuchUser  If the logged on user is invalid
     * @throws HandicapLimitAlreadyPresent Handicap limit for this fleet already exists
     */
    public void addHandicapLimit(Integer raceSeriesID, HandicapLimit limit) throws NoSuchUser, UserNotPermitted, HandicapLimitAlreadyPresent;
    /**
     * Update the handicap limit to the race series. You can't alter the handicap
     * @param limit The handicap limit to be updated
     * @throws UserNotPermitted If the logged on user is not permitted to manage this race series
     * @throws NoSuchUser If the logged on user is invalid
     * @throws InvalidUpdate 
     */
    public void updateHandicapLimit(HandicapLimit limit) throws NoSuchUser, UserNotPermitted, InvalidUpdate;

    /**
     * Returns a page of handicap limits for the race series ID selected.
     * @param raceSeriesID The ID of the race series we are looking in
     * @return A page of handicap limits
     */
    public Page<HandicapLimit> getHandicapLimits(Integer raceSeriesID,Pageable p);

    /**
     * Removes the handicap limit with the ID provided
     * @param id The id of the handicap limit to remove
     * @throws NoSuchHandicapLimit 
     * @throws UserNotPermitted 
     * @throws NoSuchUser 
     */
	public void removeHandicapLimit(Integer id) throws NoSuchHandicapLimit, NoSuchUser, UserNotPermitted;
}

