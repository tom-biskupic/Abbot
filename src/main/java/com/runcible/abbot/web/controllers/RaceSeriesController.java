package com.runcible.abbot.web.controllers;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.runcible.abbot.model.RaceSeries;
import com.runcible.abbot.model.User;
import com.runcible.abbot.service.LoggedOnUserService;
import com.runcible.abbot.service.RaceSeriesService;
import com.runcible.abbot.service.exceptions.NoSuchUser;
import com.runcible.abbot.service.exceptions.UserNotPermitted;
import com.runcible.abbot.web.model.ValidationResponse;

@Controller
public class RaceSeriesController
{

    @RequestMapping(value="/raceserieslist.json",method=GET)
    public @ResponseBody Page<RaceSeries> showList(Pageable p)
    {
        User user;
        try
        {
            user = loggedOnUserService.getLoggedOnUser();
        }
        catch (NoSuchUser e)
        {
            return new PageImpl<RaceSeries>(null);
        }

        Page<RaceSeries> raceSerieses = raceSeriesService.findAll(p,user); 
        return raceSerieses;
    }

    @RequestMapping(value="/raceseries.json/{id}",method=GET)
    public @ResponseBody RaceSeries getRaceSeries(@PathVariable("id") Integer id) throws NoSuchUser, UserNotPermitted
    {
        return raceSeriesService.findByID(id);
    }

    @RequestMapping(value="/raceseries.json",method=POST)
    public @ResponseBody ValidationResponse save(
                @RequestBody @Valid RaceSeries  series,
                BindingResult                   result ) throws NoSuchUser, UserNotPermitted
    {
        ValidationResponse response = new ValidationResponse();
        
        if ( result.hasErrors() )
        {
            response.setErrorMessageList(result.getAllErrors());
            response.setStatus("FAIL");
        }
        else
        {
            if ( series.getId() != null && series.getId() != 0 )
            {
                raceSeriesService.update(series);
            }
            else
            {
                raceSeriesService.add(series);
            }
            response.setStatus("SUCCESS");
        }
        
        return response;
    }

    @Autowired
    private RaceSeriesService raceSeriesService;
    
    @Autowired
    private LoggedOnUserService loggedOnUserService;
    
}
