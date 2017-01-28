package com.runcible.abbot.web.controllers;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.runcible.abbot.service.RaceSeriesService;
import com.runcible.abbot.service.exceptions.NoSuchUser;
import com.runcible.abbot.service.exceptions.UserNotPermitted;

@Controller
public class RaceResultController 
{
    @RequestMapping(value="/raceseries/{raceSeriesID}/race/{raceID}/resultlist",method=GET)
    public ModelAndView showPage(
    		@PathVariable("raceSeriesID") Integer raceSeriesId,
    		@PathVariable("raceID") Integer raceID) throws NoSuchUser, UserNotPermitted
    {
    	ModelAndView mav = new ModelAndView("raceresults");
    	mav.addObject("raceSeries",raceSeriesService.findByID(raceSeriesId));
        return mav;
    }

    @Autowired RaceSeriesService raceSeriesService;
}
