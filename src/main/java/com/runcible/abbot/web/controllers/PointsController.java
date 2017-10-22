package com.runcible.abbot.web.controllers;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.runcible.abbot.model.PointsTable;
import com.runcible.abbot.service.PointsService;
import com.runcible.abbot.service.exceptions.NoSuchCompetition;
import com.runcible.abbot.service.exceptions.NoSuchFleet;
import com.runcible.abbot.service.exceptions.NoSuchUser;
import com.runcible.abbot.service.exceptions.UserNotPermitted;

@Controller
public class PointsController
{
    @RequestMapping(value="/raceseries/{id}/pointstable.json/{competitionid}",method=GET)
    public @ResponseBody PointsTable getPoints(
            @PathVariable("id")             Integer raceSeriesID,
            @PathVariable("competitionid")  Integer competitionID) throws NoSuchCompetition, NoSuchUser, UserNotPermitted, NoSuchFleet
    {
        return pointsService.generatePointsTable(raceSeriesID,competitionID);
    }

    @Autowired
    private PointsService pointsService;
}
