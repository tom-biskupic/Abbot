package com.runcible.abbot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.runcible.abbot.model.Boat;
import com.runcible.abbot.repository.BoatRepository;
import com.runcible.abbot.service.exceptions.NoSuchBoat;
import com.runcible.abbot.service.exceptions.NoSuchCompetition;
import com.runcible.abbot.service.exceptions.NoSuchRaceSeries;
import com.runcible.abbot.service.exceptions.NoSuchUser;
import com.runcible.abbot.service.exceptions.UserNotPermitted;

@Component
@Transactional
public class BoatServiceImpl extends AuthorizedService implements BoatService
{
    @Override
    public Page<Boat> getAllBoatsForSeries(Integer raceSeriesId, Pageable p) throws NoSuchUser, UserNotPermitted
    {
        throwIfUserNotPermitted(raceSeriesId);
        return boatRepo.findByRaceSeries(raceSeriesId, p);
    }

    @Override
    public void updateBoat(Boat boat) throws NoSuchUser, UserNotPermitted
    {
        throwIfUserNotPermitted(boat.getRaceSeriesID());
        boatRepo.save(boat);
    }

    @Override
    public void addBoat(Integer raceSeriesId, Boat boat) throws NoSuchRaceSeries, NoSuchUser, UserNotPermitted
    {
        throwIfUserNotPermitted(raceSeriesId);
        boat.setRaceSeriesID(raceSeriesId);
        boatRepo.save(boat);
    }

    @Override
    public Boat getBoatByID(Integer boatId) throws NoSuchBoat, NoSuchUser, UserNotPermitted
    {
        Boat found = boatRepo.findOne(boatId);
        throwIfUserNotPermitted(found.getRaceSeriesID());
        return found;
    }

    @Override
    public void removeBoat(Integer boatId) throws NoSuchCompetition, NoSuchUser, UserNotPermitted
    {
        Boat found = boatRepo.findOne(boatId);
        throwIfUserNotPermitted(found.getRaceSeriesID());
        boatRepo.delete(found);
    }

    @Autowired
    private BoatRepository boatRepo;
}
