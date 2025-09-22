package com.runcible.abbot.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.runcible.abbot.model.Boat;
import com.runcible.abbot.model.Handicap;
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
	private static final float TARGET_YARDSTICK = 110;
	
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
		Optional<RaceResult> result = raceResultRepo.findById(resultId);
		if ( ! result.isPresent() )
		{
			throw new NoSuchRaceResult();
		}
		
		checkAuthorized(result.get().getRaceId());
		
		return result.get();
	}
	
	@Override
	public void addResult(Integer raceId,RaceResult result) 
	        throws NoSuchUser, UserNotPermitted, NoSuchBoat, DuplicateResult 
	{
	    Race race = raceService.getRaceByID(raceId);
	    
		result.setRaceId(raceId);
		
		updateCalculatedDurations(result,adjustResultsOnYardStick(race));
		
        updateRacePlaces(raceId, result);
        
        addResultInternal(result);
        
        auditEvent(result, race, AuditEventType.CREATED);
	}

    private Boolean adjustResultsOnYardStick(Race race)
    {
        return race.getFleet().getCompeteOnYardstick();
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
		if ( ! raceResultRepo.findById(result.getId()).isPresent())
		{
			throw new NoSuchRaceResult();
		}

		Race race = raceService.getRaceByID(result.getRaceId());

		updateCalculatedDurations(result,adjustResultsOnYardStick(race));

		updateRacePlaces(result.getRaceId(), result);
		
		raceResultRepo.save(result);
		
		auditEvent(result, race, AuditEventType.UPDATED);
	}

	public void removeResult(Integer resultId) throws NoSuchRaceResult, NoSuchUser, UserNotPermitted
	{
        Optional<RaceResult> found = raceResultRepo.findById(resultId);
        if ( ! found.isPresent() )
        {
            throw new NoSuchRaceResult();
        }
        
        Race race = raceService.getRaceByID(found.get().getRaceId());
        
	    raceResultRepo.deleteById(found.get().getId());
	    
	    try
	    {
	        updateRacePlaces(found.get().getRaceId(), null);
	    }
	    catch(DuplicateResult e)
	    {
	        //
	        // This is not possible
	        //
	    }

	    auditEvent(found.get(), race, AuditEventType.DELETED);
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
        List<Handicap> handicaps = this.handicapService.getHandicapsForFleet(
                race.getRaceSeriesId(),race.getFleet().getId(),race.getId());
        
        List<Boat> boats = findBoatsNotInRace(race);
        for(Boat boat : boats)
        {
            Float boatHandicap = findHandicapForBoat(boat,handicaps);
            
            logger.info("Adding non-starter result with handicap for boat "+boat.getName()+" set to "+boatHandicap);
            
            RaceResult newNonStarterResult = makeResult(boat,raceID,resultStatus,boatHandicap);
            addResultInternal(newNonStarterResult);
            auditEvent(newNonStarterResult, race, AuditEventType.CREATED);
        }
    }
    
    
    private Float findHandicapForBoat(Boat boat, List<Handicap> handicaps)
    {
        for(Handicap nextHandicap : handicaps)
        {
            if ( nextHandicap.getBoatID().equals(boat.getId()) )
            {
                return nextHandicap.getValue();
            }
        }
        
        //
        //  We don't have this guy for some reason - maybe new addition
        //  Just make a zero handicap
        //
        return 0.0f;
    }

    private RaceResult makeResult(Boat boat,Integer raceID,ResultStatus resultStatus, Float handicap)
    {
        return new RaceResult(raceID,boat,handicap,false,null,null,resultStatus);
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
	
    private void updateCalculatedDurations(RaceResult result, boolean useYardstick)
    {
        if ( result.getStatus().isFinished() )
        {
            int sailingTime = timeService.subtractTime(result.getStartTime(), result.getFinishTime());
            
            if ( useYardstick )
            {
                float yardstick = findYardStick(result);
                sailingTime = correctForYardstick(sailingTime,yardstick);
            }
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


    private int correctForYardstick(int sailingTime, float yardstick)
    {
        //
        //  Adjust the time to cacluate an equivalent sailing time
        //  if the vessel had a yardstick of 110. This is a middle
        //  of-the-road yardstick for sailing vessels
        //
        
        return (int)((float)sailingTime * TARGET_YARDSTICK / yardstick);
    }

    private float findYardStick(RaceResult result)
    {
        Boat boat  = result.getBoat();
        if ( boat.getDivision() != null && boat.getDivision().getYardStick() != 0)
        {
            return boat.getDivision().getYardStick();
        }
        else
        {
            return boat.getBoatClass().getYardStick();
        }
    }

    public int getWinsForBoatBeforeDate(
            Integer raceSeriesId,
            Integer fleetId,
            Integer boatId,
            Date    thisRaceDate,
            boolean shortCourse)
    {
        return raceResultRepo.getWinsForBoat(raceSeriesId, boatId, fleetId, thisRaceDate, shortCourse);
    }

	private void checkAuthorized(Integer raceId) throws NoSuchUser, UserNotPermitted 
	{
		//
		//	This will throw if the user is not permitted to manage this race
		//
		raceService.getRaceByID(raceId);
	}

    private void auditEvent(
            RaceResult      result,
            Race            race,
            AuditEventType  eventType)
            throws NoSuchUser, UserNotPermitted
    {
        String text = String.format(
                "Race Result for boat %s in race name %s, fleet %s",
                result.getBoat().getName(),
                race.getName(),
                race.getFleet().getFleetName());
                
        audit.auditEventFreeForm(eventType, race.getRaceSeriesId(), text);
    }

	private static final String RESULT_OBJECT_NAME = "Race result";

	private static final Logger logger = LogManager.getLogger(RaceResultServiceImpl.class);
	
	@Autowired private RaceService            raceService;
	@Autowired private BoatService            boatService;
	@Autowired private RaceResultRepository   raceResultRepo;
	@Autowired private TimeService            timeService;
	@Autowired private RaceResultPlaceUpdater raceResultPlaceUpdater;
	@Autowired private AuditService           audit;
	@Autowired private HandicapService        handicapService;
}
