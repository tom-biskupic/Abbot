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

/**
 * This class is only used as a way to get the current race series 
 * injected into the JSP. There are separate controllers for each
 * of the lists on the settings pages.
 */
@Controller
public class RaceSettingsController
{
    @RequestMapping(value="/raceseries/{id}/settings",method=GET)
    public ModelAndView showPage(@PathVariable("id") Integer raceSeriesId) throws NoSuchUser, UserNotPermitted
    {
        ModelAndView mav = new ModelAndView("raceseriessettings");
        mav.addObject("raceSeries",raceSeriesService.findByID(raceSeriesId));
        return mav;
    }
    
    @Autowired
    RaceSeriesService raceSeriesService;
}
