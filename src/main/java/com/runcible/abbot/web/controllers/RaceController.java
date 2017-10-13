package com.runcible.abbot.web.controllers;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.ArrayList;
import java.util.Date;
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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.runcible.abbot.model.Race;
import com.runcible.abbot.model.RaceDay;
import com.runcible.abbot.model.RaceStatus;
import com.runcible.abbot.service.RaceSeriesService;
import com.runcible.abbot.service.RaceService;
import com.runcible.abbot.service.exceptions.NoSuchCompetition;
import com.runcible.abbot.service.exceptions.NoSuchRaceSeries;
import com.runcible.abbot.service.exceptions.NoSuchUser;
import com.runcible.abbot.service.exceptions.UserNotPermitted;
import com.runcible.abbot.web.model.ValidationResponse;

@Controller
public class RaceController
{
    @RequestMapping(value="/raceseries/{id}/racelist",method=GET)
    public ModelAndView showPage(@PathVariable("id") Integer raceSeriesId) throws NoSuchUser, UserNotPermitted
    {
    	ModelAndView mav = new ModelAndView("racelist");
    	mav.addObject("raceSeries",raceSeriesService.findByID(raceSeriesId));
        return mav;
    }

    @RequestMapping(value="/raceseries/{id}/racelist.json",method=GET)
    public @ResponseBody Page<Race> showList(
            @PathVariable("id") Integer raceSeriesId,
            Pageable                    p) throws NoSuchUser, UserNotPermitted
    {
        return raceService.getAllRacesForSeries(raceSeriesId, p);
    }

    @RequestMapping(value="/raceseries/{id}/race.json",method=POST)
    public @ResponseBody ValidationResponse save(
                @Valid @RequestBody Race        race,
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
            if ( race.getId() != null && race.getId() != 0 )
            {
                raceService.updateRace(race);
            }
            else
            {
                raceService.addRace(raceSeriesId,race);
            }
            response.setStatus("SUCCESS");
        }
        return response;
    }

    @RequestMapping(value="/raceseries/{raceSeriesId}/race.json/{raceId}",method=GET)
    public @ResponseBody Race getRace(
                @PathVariable("raceSeriesId") Integer   raceSeriesId,
                @PathVariable("raceId") Integer         raceId ) throws NoSuchUser, UserNotPermitted
    {
        return raceService.getRaceByID(raceId);
    }

    @RequestMapping(value="/raceseries/{raceSeriesId}/race.json/{raceId}",method={RequestMethod.DELETE})
    public @ResponseBody ValidationResponse removeRace(
                @PathVariable("raceSeriesId") Integer   raceSeriesId,
                @PathVariable("raceId") Integer         raceId) throws NoSuchUser, UserNotPermitted, NoSuchCompetition
    {
        ValidationResponse response = new ValidationResponse();
        raceService.removeRace(raceId);
        response.setStatus("SUCCESS");
        return response;
    }

    @RequestMapping(value="/raceseries/{raceSeriesId}/racedays.json",method={RequestMethod.GET})
    public @ResponseBody List<RaceDay> getRaceDays(
                @PathVariable("raceSeriesId") Integer raceSeriesId) throws NoSuchUser, UserNotPermitted
    {
    	List<RaceDay> raceDays = raceService.getRaceDays(raceSeriesId);
    	return raceDays;
    }

    @RequestMapping(value="/raceseries/{raceSeriesId}/racestatus.json/{raceId}",method= {RequestMethod.POST})
    public @ResponseBody ValidationResponse updateRaceStatus(
            @RequestBody                    String  raceStatusString,
            @PathVariable("raceSeriesId")   Integer raceSeriesID,
            @PathVariable("raceId")         Integer raceID ) throws NoSuchUser, UserNotPermitted
    {
        ValidationResponse response = new ValidationResponse();
        Race race = raceService.getRaceByID(raceID);
        RaceStatus raceStatus = RaceStatus.valueOf(raceStatusString);
        race.setRaceStatus(raceStatus);
        raceService.updateRace(race);
        response.setStatus("SUCCESS");
        return response;
    }
    
    @Autowired
    private RaceService raceService;
    
    @Autowired
    private RaceSeriesService raceSeriesService;
}
