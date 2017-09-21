package com.runcible.abbot.web.controllers;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

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

import com.runcible.abbot.model.Competition;
import com.runcible.abbot.service.CompetitionService;
import com.runcible.abbot.service.exceptions.NoSuchCompetition;
import com.runcible.abbot.service.exceptions.NoSuchFleet;
import com.runcible.abbot.service.exceptions.NoSuchRaceSeries;
import com.runcible.abbot.service.exceptions.NoSuchUser;
import com.runcible.abbot.service.exceptions.UserNotPermitted;
import com.runcible.abbot.web.model.ValidationResponse;

@Controller
public class CompetitionController 
{
    @RequestMapping(value="/raceseries/{id}/competitionlist.json",method=GET)
    public @ResponseBody Page<Competition> showList(
            @PathVariable("id") Integer raceSeriesId,
            Pageable                    p) throws NoSuchUser, UserNotPermitted
    {
        return competitionService.getAllCompetitionsForSeries(raceSeriesId, p);
    }

    @RequestMapping(value="/raceseries/{id}/competitionlist.json/all",method=GET)
    public @ResponseBody List<Competition> getAll(
            @PathVariable("id") Integer raceSeriesId ) throws NoSuchUser, UserNotPermitted
    {
        return competitionService.getAllCompetitionsForSeries(raceSeriesId);
    }

    @RequestMapping(value="/raceseries/{id}/competition.json",method=POST)
    public @ResponseBody ValidationResponse save(
    			@Valid @RequestBody Competition	competition,
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
            if ( competition.getId() != null && competition.getId() != 0 )
            {
                competitionService.updateCompetition(competition);
            }
            else
            {
                competitionService.addCompetition(raceSeriesId,competition);
            }
            response.setStatus("SUCCESS");
        }
        return response;
    }	

    @RequestMapping(value="/raceseries/{raceSeriesId}/competition.json/{competitionId}",method=GET)
    public @ResponseBody Competition getCompetition(
                @PathVariable("raceSeriesId") Integer	raceSeriesId,
                @PathVariable("competitionId") Integer	competitionId ) throws NoSuchCompetition, NoSuchUser, UserNotPermitted
    {
        return competitionService.getCompetitionByID(competitionId);
    }

    @RequestMapping(value="/raceseries/{raceSeriesId}/competition.json/{competitionId}",method={RequestMethod.DELETE})
    public @ResponseBody ValidationResponse removeBoatClass(
                @PathVariable("raceSeriesId") Integer	raceSeriesId,
                @PathVariable("competitionId") Integer  competitionId) throws NoSuchFleet, NoSuchCompetition, NoSuchUser, UserNotPermitted
    {
    	ValidationResponse response = new ValidationResponse();
        competitionService.removeCompetition(competitionId);
        response.setStatus("SUCCESS");
        return response;
    }

    @Autowired
    CompetitionService competitionService;    
}
