package com.runcible.abbot.web.controllers;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.runcible.abbot.model.Handicap;
import com.runcible.abbot.service.HandicapService;
import com.runcible.abbot.service.exceptions.NoSuchFleet;
import com.runcible.abbot.service.exceptions.NoSuchUser;
import com.runcible.abbot.service.exceptions.UserNotPermitted;

@Controller
public class HandicapController
{

    @RequestMapping(value="/raceseries/{raceseriesid}/fleet/{fleetid}/handicaplist.json",method=GET)
    public @ResponseBody List<Handicap> getHandicapsForFleet(
            @PathVariable("raceseriesid") Integer raceSeriesID,
            @PathVariable("fleetid") Integer fleetID ) throws NoSuchUser, UserNotPermitted, NoSuchFleet
    {
        return handicapService.getHandicapsForFleet(raceSeriesID, fleetID);
    }

    @Autowired
    private HandicapService handicapService;
}
