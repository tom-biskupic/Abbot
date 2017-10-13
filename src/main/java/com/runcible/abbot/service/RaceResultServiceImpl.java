package com.runcible.abbot.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.runcible.abbot.model.Boat;
import com.runcible.abbot.model.Race;
import com.runcible.abbot.model.RaceResult;
import com.runcible.abbot.repository.RaceResultRepository;
import com.runcible.abbot.service.exceptions.NoSuchBoat;
import com.runcible.abbot.service.exceptions.NoSuchFleet;
import com.runcible.abbot.service.exceptions.NoSuchRaceResult;
import com.runcible.abbot.service.exceptions.NoSuchUser;
import com.runcible.abbot.service.exceptions.UserNotPermitted;

@Component
public class RaceResultServiceImpl implements RaceResultService 
{
	@Override
	public Page<RaceResult> findAll(Integer raceId,Pageable p) throws NoSuchUser, UserNotPermitted 
	{
		checkAuthorized(raceId);
		return raceResultRepo.findRaceResults(raceId,p);
	}

	@Override
	public RaceResult getResultByID(Integer resultId) throws NoSuchUser, UserNotPermitted, NoSuchRaceResult
	{
		RaceResult result = raceResultRepo.findOne(resultId);
		if ( result == null )
		{
			throw new NoSuchRaceResult();
		}
		
		checkAuthorized(result.getRaceId());
		
		return result;
	}
	
	@Override
	public void addResult(Integer raceId,RaceResult result) throws NoSuchUser, UserNotPermitted, NoSuchBoat 
	{
		checkAuthorized(raceId);
		result.setRaceId(raceId);
		
        updateCalculatedDurations(result);
		
		//
		//    Not sure why we need to do this - something to do with it being
		//    a ManyToOne relationship
		//
		Boat foundBoat = boatService.getBoatByID(result.getBoat().getId());
		result.setBoat(foundBoat);
		raceResultRepo.save(result);
	}

	@Override
	public void updateResult(RaceResult result) throws NoSuchUser, UserNotPermitted, NoSuchRaceResult 
	{
		if ( raceResultRepo.findOne(result.getId()) == null )
		{
			throw new NoSuchRaceResult();
		}

		updateCalculatedDurations(result);

		checkAuthorized(result.getRaceId());
		raceResultRepo.save(result);
	}

	public void removeResult(Integer resultId) throws NoSuchRaceResult, NoSuchUser, UserNotPermitted
	{
        RaceResult found = raceResultRepo.findOne(resultId);
        if ( found == null )
        {
            throw new NoSuchRaceResult();
        }
	    checkAuthorized(found.getRaceId());
	    raceResultRepo.delete(found);
	}
	
	//
	// This is very slow. Would be much better to do this in the DB. 
	// The problem is that the query for finding all boats in a fleet
	// is a merge of a series of queries.
	//
	public List<Boat> findBoatsNotInRace(Integer raceId) throws NoSuchUser, UserNotPermitted, NoSuchFleet
	{
	    List<Boat> boatsNotInRace=new ArrayList<Boat>();
	    
	    //
	    // This will throw if we are not permitted to manage this race
	    //
	    Race race = raceService.getRaceByID(raceId);
	    
	    List<Boat> allBoats = boatService.getAllBoatsInFleetForSeries(race.getRaceSeriesId(), race.getFleet().getId());
	    List<RaceResult> raceResults = raceResultRepo.findRaceResults(race.getId());

        for (Boat nextBoat : allBoats )
        {
            if ( ! haveResultForBoat(raceResults, nextBoat) )
            {
                boatsNotInRace.add(nextBoat);
            }
        }
	    
	    return boatsNotInRace;
	}

    private boolean haveResultForBoat(List<RaceResult> raceResults, Boat nextBoat)
    {
        for(RaceResult nextResult : raceResults)
        {
            if ( nextBoat.getId().equals(nextResult.getBoat().getId()) )
            {
                return true;
            }
        }
        
        return false;
    }
	
    private void updateCalculatedDurations(RaceResult result)
    {
        int sailingTime = timeService.subtractTime(result.getStartTime(), result.getFinishTime());
        result.setSailingTime(sailingTime);
	        
	    result.setCorrectedTime(sailingTime - result.getHandicap()*60);
    }

   
	private void checkAuthorized(Integer raceId) throws NoSuchUser, UserNotPermitted 
	{
		//
		//	This will throw if the user is not permitted to manage this race
		//
		raceService.getRaceByID(raceId);
	}

	@Autowired	RaceService            raceService;
	@Autowired  BoatService            boatService;
	@Autowired 	RaceResultRepository   raceResultRepo;
	@Autowired  TimeService            timeService;
}
