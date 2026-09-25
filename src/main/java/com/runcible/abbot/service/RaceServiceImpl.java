package com.runcible.abbot.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.runcible.abbot.model.Competition;
import com.runcible.abbot.model.Race;
import com.runcible.abbot.model.RaceDay;
import com.runcible.abbot.repository.RaceRespository;
import com.runcible.abbot.service.audit.AuditEventType;
import com.runcible.abbot.service.audit.AuditService;
import com.runcible.abbot.service.exceptions.NoSuchUser;
import com.runcible.abbot.service.exceptions.UserNotPermitted;

@Service
public class RaceServiceImpl  extends AuthorizedService implements RaceService
{

    @Override
    public Page<Race> getAllRacesForSeries(Integer seriesId, Pageable p) throws NoSuchUser, UserNotPermitted
    {
        throwIfUserNotPermitted(seriesId);
        return raceRepo.findByRaceSeries(seriesId, p);
    }

    @Override
    public void addRace(Integer raceSeriesId, Race race) throws NoSuchUser, UserNotPermitted
    {
        throwIfUserNotPermitted(raceSeriesId);
        race.setRaceSeriesId(raceSeriesId);
        raceRepo.save(race);
        
        auditEvent(race, AuditEventType.CREATED);
    }

    @Override
    public void updateRace(Race race) throws NoSuchUser, UserNotPermitted
    {
        throwIfUserNotPermitted(race.getRaceSeriesId());
        raceRepo.save(race);
        auditEvent(race, AuditEventType.UPDATED);
    }

    @Override
    public Race getRaceByID(Integer raceID) throws NoSuchUser, UserNotPermitted
    {
        Optional<Race> found = raceRepo.findById(raceID);
        throwIfUserNotPermitted(found.get().getRaceSeriesId());
        return found.get();
    }

    @Override
    public void removeRace(Integer raceID) throws NoSuchUser, UserNotPermitted
    {
        Optional<Race> found = raceRepo.findById(raceID);
        throwIfUserNotPermitted(found.get().getRaceSeriesId());
        raceRepo.deleteById(found.get().getId());
        auditEvent(found.get(), AuditEventType.DELETED);
    }

    @Override
	public List<RaceDay> getRaceDays(Integer raceSeriesId) throws NoSuchUser, UserNotPermitted
	{
    	List<RaceDay> raceDays = new ArrayList<RaceDay>();
    	
		throwIfUserNotPermitted(raceSeriesId);
		List<Race> races = raceRepo.findRacesOrderByDate(raceSeriesId);
		if ( ! races.isEmpty() )
		{
			RaceDay nextRaceDay = new RaceDay(races.get(0).getRaceDate());
			raceDays.add(nextRaceDay);
			
			for(Race r : races)
			{
				if (r.getRaceDate().equals(nextRaceDay.getDay()))
				{
					nextRaceDay.getRaces().add(r);
				}
				else
				{
					nextRaceDay = new RaceDay(r.getRaceDate());
					raceDays.add(nextRaceDay);
					nextRaceDay.getRaces().add(r);
				}
			}
		}
		
		return raceDays;
	}
	
    @Override
    public List<Race> getRacesInCompetition(Competition competition)
    {
        List<Race> result = raceRepo.findRacesInCompetition(competition.getRaceSeriesId(), competition.getId());
        
        return result;
    }

    public List<Race> getRacesForFleet(Integer raceSeriesID, Integer fleetID) throws NoSuchUser, UserNotPermitted
    {
        throwIfUserNotPermitted(raceSeriesID);
        List<Race> result = raceRepo.findRacesForFleet(raceSeriesID, fleetID);
        
        return result;
    }

    @Override
    public Integer findPreviousFinishedRaceId(
            Integer raceID ) throws NoSuchUser, UserNotPermitted
    {
        Race thisRace = this.getRaceByID(raceID);

        return raceRepo.findPreviousRaceID(
                thisRace.getRaceSeriesId(),
                thisRace.getRaceDate(),
                thisRace.getRaceNumber() != null ? thisRace.getRaceNumber() : 0,
                thisRace.getFleet().getId(),
                thisRace.isShortCourseRace() ? 1 : 0);
    }

    private void auditEvent(Race race, AuditEventType eventType) throws NoSuchUser, UserNotPermitted
    {
        audit.auditEvent(
                eventType,
                race.getRaceSeriesId(), 
                RACE_OBJECT_NAME, 
                race.getName());
    }

    private static final String RACE_OBJECT_NAME = "Race";
    
	@Autowired
    private RaceRespository raceRepo;
    
    @Autowired
    private AuditService audit;
}
