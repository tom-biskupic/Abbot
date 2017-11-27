package com.runcible.abbot.web.controllers;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.runcible.abbot.model.Boat;
import com.runcible.abbot.model.Handicap;
import com.runcible.abbot.model.HandicapLimit;
import com.runcible.abbot.service.HandicapService;
import com.runcible.abbot.service.exceptions.HandicapLimitAlreadyPresent;
import com.runcible.abbot.service.exceptions.InvalidUpdate;
import com.runcible.abbot.service.exceptions.NoSuchFleet;
import com.runcible.abbot.service.exceptions.NoSuchUser;
import com.runcible.abbot.service.exceptions.UserNotPermitted;
import com.runcible.abbot.service.extensions.NoSuchHandicapLimit;
import com.runcible.abbot.web.model.ValidationResponse;

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

    @RequestMapping(value="/raceseries/{raceseriesid}/handicapLimitList.json",method=GET)
    public @ResponseBody Page<HandicapLimit> getHandicapsLimits(
            @PathVariable("raceseriesid") Integer raceSeriesID,
            Pageable p ) 
    {
        return handicapService.getHandicapLimits(raceSeriesID, p);
    }

    @RequestMapping(value="/raceseries/{raceseriesid}/handicapLimit.json/{id}",method=GET)
    public @ResponseBody HandicapLimit getHandicapsLimit(
            @PathVariable("raceseriesid") Integer raceSeriesID,
            @PathVariable("id") Integer id) throws NoSuchUser, UserNotPermitted 
    {
    	return handicapService.getHandicapLimit(raceSeriesID, id);
    }

    @RequestMapping(value="/raceseries/{raceseriesid}/handicapLimit.json/{id}",method=DELETE)
    public @ResponseBody ValidationResponse deleteHandicapsLimit(
            @PathVariable("raceseriesid") Integer raceSeriesID,
            @PathVariable("id") Integer id) throws NoSuchUser, UserNotPermitted, NoSuchHandicapLimit 
    {
        ValidationResponse response = new ValidationResponse();
        handicapService.removeHandicapLimit(id);
        response.setStatus("SUCCESS");
        return response;
    }

    @RequestMapping(value="/raceseries/{raceseriesid}/handicapLimit.json",method=POST)
    public @ResponseBody ValidationResponse save(
            @Valid @RequestBody HandicapLimit       limit,
            BindingResult                   		result,
            @PathVariable("raceseriesid") Integer 	raceSeriesID ) throws NoSuchUser, UserNotPermitted, InvalidUpdate, HandicapLimitAlreadyPresent 
    {
        ValidationResponse response = new ValidationResponse();
        if ( result.hasErrors() )
        {
            response.setErrorMessageList(result.getAllErrors());
            response.setStatus("FAIL");
        }
        else
        {
        	if (limit.getId() != null )
        	{
        		handicapService.updateHandicapLimit(limit);
        	}
        	else
        	{
        		handicapService.addHandicapLimit(raceSeriesID,limit);
        	}
        }
        
        return response;
    }

    @Autowired
    private HandicapService handicapService;
}
