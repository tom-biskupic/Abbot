package com.runcible.abbot.web.controllers;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import javax.validation.Valid;

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
import org.springframework.web.servlet.ModelAndView;

import com.runcible.abbot.model.Boat;
import com.runcible.abbot.service.BoatService;
import com.runcible.abbot.service.RaceSeriesService;
import com.runcible.abbot.service.exceptions.NoSuchBoat;
import com.runcible.abbot.service.exceptions.NoSuchCompetition;
import com.runcible.abbot.service.exceptions.NoSuchRaceSeries;
import com.runcible.abbot.service.exceptions.NoSuchUser;
import com.runcible.abbot.service.exceptions.UserNotPermitted;
import com.runcible.abbot.web.model.ValidationResponse;

@Controller
public class BoatController
{
    
    @RequestMapping(value="/raceseries/{id}/boats",method=GET)
    public ModelAndView showPage(@PathVariable("id") Integer raceSeriesId) throws NoSuchUser, UserNotPermitted
    {
        ModelAndView mav = new ModelAndView("boatlist");
        mav.addObject("raceSeries",raceSeriesService.findByID(raceSeriesId));
        return mav;
    }

    @RequestMapping(value="/raceseries/{id}/boatlist.json",method=GET)
    public @ResponseBody Page<Boat> showList(
            @PathVariable("id") Integer raceSeriesId,
            Pageable                    p) throws NoSuchUser, UserNotPermitted
    {
        return boatService.getAllBoatsForSeries(raceSeriesId, p);
    }

    @RequestMapping(value="/raceseries/{id}/boat.json",method=POST)
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
    
    @RequestMapping(value="/raceseries/{raceSeriesId}/boat.json/{boatId}",method=GET)
    public @ResponseBody Boat getBoat(
                @PathVariable("raceSeriesId") Integer   raceSeriesId,
                @PathVariable("boatId") Integer        boatId ) throws NoSuchUser, UserNotPermitted, NoSuchBoat
    {
        return boatService.getBoatByID(boatId);
    }

    @RequestMapping(value="/raceseries/{raceSeriesId}/boat.json/{boatId}",method={RequestMethod.DELETE})
    public @ResponseBody ValidationResponse removeBoatClass(
                @PathVariable("raceSeriesId") Integer   raceSeriesId,
                @PathVariable("boatId") Integer         boatId) throws NoSuchUser, UserNotPermitted, NoSuchCompetition
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
