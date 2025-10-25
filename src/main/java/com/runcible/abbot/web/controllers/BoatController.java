package com.runcible.abbot.web.controllers;


import java.util.List;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.runcible.abbot.model.Boat;
import com.runcible.abbot.service.BoatService;
import com.runcible.abbot.service.RaceSeriesService;
import com.runcible.abbot.service.exceptions.NoSuchBoat;
import com.runcible.abbot.service.exceptions.NoSuchCompetition;
import com.runcible.abbot.service.exceptions.NoSuchFleet;
import com.runcible.abbot.service.exceptions.NoSuchRaceSeries;
import com.runcible.abbot.service.exceptions.NoSuchUser;
import com.runcible.abbot.service.exceptions.UserNotPermitted;
import com.runcible.abbot.web.model.ValidationResponse;

@RestController
public class BoatController
{
    
    @GetMapping("/raceseries/{id}/boats")
    public ModelAndView showPage(@PathVariable("id") Integer raceSeriesId) throws NoSuchUser, UserNotPermitted
    {
        ModelAndView mav = new ModelAndView("boatlist");
        mav.addObject("raceSeries",raceSeriesService.findByID(raceSeriesId));
        return mav;
    }

    @GetMapping("/raceseries/{id}/boatlist.json")
    public @ResponseBody Page<Boat> showList(
            @PathVariable("id") Integer raceSeriesId,
            Pageable                    p) throws NoSuchUser, UserNotPermitted
    {
        return boatService.getAllBoatsForSeries(raceSeriesId, p);
    }

    @GetMapping("/raceseries/{id}/fleet/{fleetId}/boatlist.json/all")
    public @ResponseBody List<Boat> getBoatsInFleet(
            @PathVariable("id") Integer raceSeriesId,
            @PathVariable("fleetId") Integer fleetId,
            Pageable                    p) throws NoSuchUser, UserNotPermitted, NoSuchFleet
    {
        return boatService.getAllBoatsInFleetForSeries(raceSeriesId, fleetId);
    }

    @PostMapping(value="/raceseries/{id}/boat.json")
    public @ResponseBody ValidationResponse save(
                @Valid @RequestBody Boat        boat,
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
            if ( boat.getId() != null && boat.getId() != 0 )
            {
                boatService.updateBoat(boat);
            }
            else
            {
                boatService.addBoat(raceSeriesId,boat);
            }
            response.setStatus("SUCCESS");
        }
        return response;
    }
    
    @GetMapping("/raceseries/{raceSeriesId}/boat.json/{boatId}")
    public @ResponseBody Boat getBoat(
                @PathVariable("raceSeriesId") Integer   raceSeriesId,
                @PathVariable("boatId") Integer        boatId ) throws NoSuchUser, UserNotPermitted, NoSuchBoat
    {
        return boatService.getBoatByID(boatId);
    }

    @DeleteMapping("/raceseries/{raceSeriesId}/boat.json/{boatId}")
    public @ResponseBody ValidationResponse removeBoatClass(
                @PathVariable("raceSeriesId") Integer   raceSeriesId,
                @PathVariable("boatId") Integer         boatId) throws NoSuchUser, UserNotPermitted, NoSuchCompetition, NoSuchBoat
    {
        ValidationResponse response = new ValidationResponse();
        boatService.removeBoat(boatId);
        response.setStatus("SUCCESS");
        return response;
    }

    @Autowired
    private BoatService boatService;
    
    @Autowired
    private RaceSeriesService raceSeriesService;

}
