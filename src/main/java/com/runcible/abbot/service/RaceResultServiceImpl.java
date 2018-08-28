package com.runcible.abbot.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.runcible.abbot.model.Boat;
import com.runcible.abbot.model.Race;
import com.runcible.abbot.model.RaceResult;
import com.runcible.abbot.model.ResultStatus;
import com.runcible.abbot.repository.RaceResultRepository;
import com.runcible.abbot.service.audit.AuditEventType;
import com.runcible.abbot.service.audit.AuditService;
import com.runcible.abbot.service.exceptions.DuplicateResult;
import com.runcible.abbot.service.exceptions.NoSuchBoat;
import com.runcible.abbot.service.exceptions.NoSuchFleet;
import com.runcible.abbot.service.exceptions.NoSuchRaceResult;
import com.runcible.abbot.service.exceptions.NoSuchUser;
import com.runcible.abbot.service.exceptions.UserNotPermitted;
import com.runcible.abbot.service.points.RaceResultPlaceUpdater;

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
    public List<RaceResult> findAll(Integer raceId) throws NoSuchUser, UserNotPermitted
    {
	    checkAuthorized(raceId);
	    return raceResultRepo.findRaceResults(raceId);
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
	public void addResult(Integer raceId,RaceResult result) 
	        throws NoSuchUser, UserNotPermitted, NoSuchBoat, DuplicateResult 
	{
		checkAuthorized(raceId);
		result.setRaceId(raceId);
		
        updateCalculatedDurations(result);
		
        updateRacePlaces(raceId, result);
        
        addResultInternal(result);
        
        auditEvent(result, AuditEventType.CREATED);
	}

    private void updateRacePlaces(Integer raceId, RaceResult result) throws DuplicateResult
    {
        List<RaceResult> existingResults = raceResultRepo.findRaceResults(raceId);
        
        raceResultPlaceUpdater.updateResultPlaces(result, existingResults);
        
        for( RaceResult nextResult : existingResults )
        {
            raceResultRepo.save(nextResult);
        }
    }

    private void addResultInternal(RaceResult result)
            throws NoSuchBoat, NoSuchUser, UserNotPermitted
    {
        //
        //    Not sure why we need to do this - something to do with it being
        //    a ManyToOne relationship
        //
        Boat foundBoat = boatService.getBoatByID(result.getBoat().getId());
		result.setBoat(foundBoat);
		raceResultRepo.save(result);
    }

    @Override
	public void updateResult(RaceResult result) 
	        throws NoSuchUser, UserNotPermitted, NoSuchRaceResult, DuplicateResult 
	{
		if ( raceResultRepo.findOne(result.getId()) == null )
		{
			throw new NoSuchRaceResult();
		}

		checkAuthorized(result.getRaceId());

		updateCalculatedDurations(result);

		updateRacePlaces(result.getRaceId(), result);
		
		raceResultRepo.save(result);
		
		auditEvent(result, AuditEventType.UPDATED);
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
	    
	    try
	    {
	        updateRacePlaces(found.getRaceId(), null);
	    }
	    catch(DuplicateResult e)
	    {
	        //
	        // This is not possible
	        //
	    }

	    auditEvent(found, AuditEventType.DELETED);
	}
	
	//
	// This is very slow. Would be much better to do this in the DB. 
	// The problem is that the query for finding all boats in a fleet
	// is a merge of a series of queries.
	//
	public List<Boat> findBoatsNotInRace(Integer raceId) throws NoSuchUser, UserNotPermitted, NoSuchFleet
	{
	    //
	    // This will throw if we are not permitted to manage this race
	    //
	    Race race = raceService.getRaceByID(raceId);

	    return findBoatsNotInRace(race);
	}

    private List<Boat> findBoatsNotInRace(Race race)
            throws NoSuchUser, UserNotPermitted, NoSuchFleet
    {
        List<Boat> boatsNotInRace=new ArrayList<Boat>();

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

    @Override
    public void addNonStartersToRace(Integer raceID, ResultStatus resultStatus) 
            throws NoSuchUser, UserNotPermitted, NoSuchFleet, NoSuchBoat
    {
        //
        // This will throw if we are not permitted to manage this race
        //
        Race race = raceService.getRaceByID(raceID);

        List<Boat> boats = findBoatsNotInRace(race);
        for(Boat boat : boats)
        {
            addResultInternal(makeResult(boat,raceID,resultStatus));
        }
    }
    
    private RaceResult makeResult(Boat boat,Integer raceID,ResultStatus resultStatus)
    {
        return new RaceResult(raceID,boat,0.0f,false,null,null,resultStatus);
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
        if ( result.getStatus().isFinished() )
        {
            int sailingTime = timeService.subtractTime(result.getStartTime(), result.getFinishTime());
            result.setSailingTime(sailingTime);
    	        
    	    result.setCorrectedTime(sailingTime - (int)(result.getHandicap()*60.0f));
        }
        else 
        {
            result.setSailingTime(null);
            result.setCorrectedTime(null);
            
            result.setFinishTime(null);
            
            if ( ! result.getStatus().isStarted() )
            {
                result.setStartTime(null);
            }
        }
    }


    public int getWinsForBoatBeforeDate(
            Integer raceSeriesId,
            Integer fleetId,
            Integer boatId,
            Date    thisRaceDate )
    {
        return raceResultRepo.getWinsForBoat(raceSeriesId, boatId, fleetId, thisRaceDate);
    }

	private void checkAuthorized(Integer raceId) throws NoSuchUser, UserNotPermitted 
	{
		//
		//	This will throw if the user is not permitted to manage this race
		//
		raceService.getRaceByID(raceId);
	}

    private void auditEvent(RaceResult result, AuditEventType eventType)
            throws NoSuchUser, UserNotPermitted
    {
        Race race = raceService.getRaceByID(result.getRaceId());
        String text = String.format(
                "Race Result for boat %s in race name %s, fleet %s",
                result.getBoat().getName(),
                race.getName(),
                race.getFleet().getFleetName());
                
        audit.auditEventFreeForm(eventType, race.getRaceSeriesId(), text);
    }

	private static final String RESULT_OBJECT_NAME = "Race result";
	
	@Autowired private RaceService            raceService;
	@Autowired private BoatService            boatService;
	@Autowired private RaceResultRepository   raceResultRepo;
	@Autowired private TimeService            timeService;
	@Autowired private RaceResultPlaceUpdater raceResultPlaceUpdater;
	@Autowired private AuditService           audit;
}
