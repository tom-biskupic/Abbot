package com.runcible.abbot.web.controllers;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.runcible.abbot.model.Fleet;
import com.runcible.abbot.service.FleetService;
import com.runcible.abbot.service.exceptions.NoSuchFleet;
import com.runcible.abbot.service.exceptions.NoSuchRaceSeries;
import com.runcible.abbot.service.exceptions.NoSuchUser;
import com.runcible.abbot.service.exceptions.UserNotPermitted;
import com.runcible.abbot.web.model.ValidationResponse;

@Controller
public class FleetController 
{
    @RequestMapping(value="/raceseries/{id}/fleetlist.json",method=GET)
    public @ResponseBody Page<Fleet> showList(
            @PathVariable("id") Integer raceSeriesId,
            Pageable                    p) throws NoSuchUser, UserNotPermitted
    {
        return fleetService.getAllFleetsForSeries(raceSeriesId, p);
    }

    
    @RequestMapping(value="/raceseries/{id}/fleetlist.json/all",method=GET)
    public @ResponseBody List<Fleet> getAll(
            @PathVariable("id") Integer raceSeriesId ) throws NoSuchUser, UserNotPermitted
    {
        return fleetService.getAllFleetsForSeries(raceSeriesId);
    }

    @RequestMapping(value="/raceseries/{id}/fleet.json",method=POST)
    public @ResponseBody ValidationResponse save(
    			@Valid @RequestBody Fleet   	fleet,
    			BindingResult                   result,
                @PathVariable("id") Integer     raceSeriesId ) throws NoSuchUser, UserNotPermitted, NoSuchRaceSeries
    {
        ValidationResponse response = new ValidationResponse();
        if ( result.hasErrors() )
        {
            response.setErrorMessageList(result.getAllErrors());
            response.setStatus("FAIL");
        }
        else
        {
            if ( fleet.getId() != null && fleet.getId() != 0 )
            {
                fleetService.updateFleet(fleet);
            }
            else
            {
                fleetService.addFleet(raceSeriesId,fleet);
            }
            response.setStatus("SUCCESS");
        }
        return response;
    }
    
    @RequestMapping(value="/raceseries/{raceSeriesId}/fleet.json/{fleetId}",method=GET)
    public @ResponseBody Fleet getFleet(
                @PathVariable("raceSeriesId") Integer	raceSeriesId,
                @PathVariable("fleetId") Integer		fleetId ) throws NoSuchFleet, NoSuchUser, UserNotPermitted
    {
        return fleetService.getFleetByID(fleetId);
    }

    @RequestMapping(value="/raceseries/{raceSeriesId}/fleet.json/{fleetId}",method={RequestMethod.DELETE})
    public @ResponseBody ValidationResponse removeBoatClass(
                @PathVariable("raceSeriesId") Integer   raceSeriesId,
                @PathVariable("fleetId") Integer    	fleetId) throws NoSuchFleet, NoSuchUser, UserNotPermitted
    {
    	ValidationResponse response = new ValidationResponse();
        fleetService.removeFleet(fleetId);
        response.setStatus("SUCCESS");
        return response;
    }
    
    @Autowired
    FleetService fleetService;    
}
