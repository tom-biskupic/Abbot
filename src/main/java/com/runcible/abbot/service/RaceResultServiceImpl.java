package com.runcible.abbot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.runcible.abbot.model.Boat;
import com.runcible.abbot.model.RaceResult;
import com.runcible.abbot.repository.RaceResultRepository;
import com.runcible.abbot.service.exceptions.NoSuchBoat;
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
		return raceResultRepo.findRacesResults(raceId,p);
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
