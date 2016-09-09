package com.runcible.abbot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.runcible.abbot.model.Boat;
import com.runcible.abbot.model.Race;
import com.runcible.abbot.repository.RaceRespository;
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
    public void addRace(Integer raceSeriesId,Race race) throws NoSuchUser, UserNotPermitted
    {
        throwIfUserNotPermitted(raceSeriesId);
        race.setRaceSeriesId(raceSeriesId);
        raceRepo.save(race);
    }

    @Override
    public void updateRace(Race race) throws NoSuchUser, UserNotPermitted
    {
        throwIfUserNotPermitted(race.getRaceSeriesId());
        raceRepo.save(race);
    }

    @Override
    public Race getRaceByID(Integer raceID) throws NoSuchUser, UserNotPermitted
    {
        Race found = raceRepo.findOne(raceID);
        throwIfUserNotPermitted(found.getRaceSeriesId());
        return found;
    }

    @Override
    public void removeRace(Integer raceID) throws NoSuchUser, UserNotPermitted
    {
        Race found = raceRepo.findOne(raceID);
        throwIfUserNotPermitted(found.getRaceSeriesId());
        raceRepo.delete(found);
    }

    @Autowired
    private RaceRespository raceRepo;
    
    @Autowired
    private RaceSeriesService raceSeriesService;
}
