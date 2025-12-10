package com.runcible.abbot.web.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.runcible.abbot.model.PointsTable;
import com.runcible.abbot.service.PointsService;
import com.runcible.abbot.service.exceptions.NoSuchCompetition;
import com.runcible.abbot.service.exceptions.NoSuchFleet;
import com.runcible.abbot.service.exceptions.NoSuchUser;
import com.runcible.abbot.service.exceptions.UserNotPermitted;

@RestController
public class PointsController
{
    @GetMapping(value="/raceseries/{id}/pointstable.json/{competitionid}")
    public @ResponseBody PointsTable getPoints(
            @PathVariable("id")             Integer raceSeriesID,
            @PathVariable("competitionid")  Integer competitionID) throws NoSuchCompetition, NoSuchUser, UserNotPermitted, NoSuchFleet
    {
        return pointsService.generatePointsTable(raceSeriesID,competitionID);
    }

    @Autowired
    private PointsService pointsService;
}
