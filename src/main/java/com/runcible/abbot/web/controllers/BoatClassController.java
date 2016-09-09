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
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.runcible.abbot.model.BoatClass;
import com.runcible.abbot.model.BoatDivision;
import com.runcible.abbot.service.BoatClassService;
import com.runcible.abbot.service.exceptions.DuplicateDivision;
import com.runcible.abbot.service.exceptions.NoSuchBoatClass;
import com.runcible.abbot.service.exceptions.NoSuchDivision;
import com.runcible.abbot.service.exceptions.NoSuchUser;
import com.runcible.abbot.service.exceptions.UserNotPermitted;
import com.runcible.abbot.web.model.ValidationResponse;

@Controller
public class BoatClassController 
{
    @RequestMapping(value="/raceseries/{id}/boatclasslist.json",method=GET)
    public @ResponseBody Page<BoatClass> showList(
            @PathVariable("id") Integer raceSeriesId,
            Pageable                    p) throws NoSuchUser, UserNotPermitted
    {
        return boatClassService.getAllBoatClassesForSeries(raceSeriesId, p);
    }

    @RequestMapping(value="/raceseries/{id}/boatclasslist.json/all",method=GET)
    public @ResponseBody List<BoatClass> getAll(
            @PathVariable("id") Integer raceSeriesId ) throws NoSuchUser, UserNotPermitted
    {
        return boatClassService.getAllBoatClassesForSeries(raceSeriesId);
    }

    @RequestMapping(value="/raceseries/{id}/boatclass.json",method=POST)
    public @ResponseBody ValidationResponse save(
    			@Valid @RequestBody BoatClass   boatClass,
    			BindingResult                   result,
                @PathVariable("id") Integer     raceSeriesId ) throws NoSuchUser, UserNotPermitted
    {
        ValidationResponse response = new ValidationResponse();
        if ( result.hasErrors() )
        {
            response.setErrorMessageList(result.getAllErrors());
            response.setStatus("FAIL");
        }
        else
        {
            if ( boatClass.getId() != null && boatClass.getId() != 0 )
            {
                boatClassService.updateBoatClass(boatClass);
            }
            else
            {
                boatClassService.addBoatClass(raceSeriesId,boatClass);
            }
            response.setStatus("SUCCESS");
        }
        return response;
    }

    @RequestMapping(value="/raceseries/{raceSeriesId}/boatclass.json/{boatClassId}",method={RequestMethod.DELETE})
    public @ResponseBody ValidationResponse removeBoatClass(
                @PathVariable("raceSeriesId") Integer   raceSeriesId,
                @PathVariable("boatClassId") Integer    boatClassId) throws NoSuchBoatClass, NoSuchUser, UserNotPermitted
    {
    	ValidationResponse response = new ValidationResponse();
        boatClassService.removeBoatClass(boatClassId);
        response.setStatus("SUCCESS");
        return response;
    }

    @RequestMapping(value="/raceseries/{raceSeriesId}/boatclass.json/{boatClassId}",method=GET)
    public @ResponseBody BoatClass getBoatClass(
                @PathVariable("raceSeriesId") Integer   raceSeriesId,
                @PathVariable("boatClassId") Integer    boatClassId ) throws NoSuchBoatClass, NoSuchUser, UserNotPermitted
    {
        return boatClassService.getBoatClassByID(boatClassId);
    }

    @RequestMapping(value="/raceseries/{raceSeriesId}/boatclass.json/{boatClassId}/division.json/{divisionId}",method=GET)
    public @ResponseBody BoatDivision getDivision(
                @PathVariable("raceSeriesId") Integer   raceSeriesId,
                @PathVariable("boatClassId") Integer    boatClassId,
                @PathVariable("divisionId") Integer    divisionId) throws NoSuchBoatClass, NoSuchUser, UserNotPermitted
    {
        return boatClassService.getBoatClassByID(boatClassId).getDivision(divisionId);
    }

    @RequestMapping(value="/raceseries/{raceSeriesId}/boatclass.json/{boatClassId}/division.json/{divisionId}",method={RequestMethod.DELETE})
    public @ResponseBody ValidationResponse removeDivision(
                @PathVariable("raceSeriesId") Integer   raceSeriesId,
                @PathVariable("boatClassId") Integer    boatClassId,
                @PathVariable("divisionId") Integer    	divisionId) throws NoSuchBoatClass, NoSuchDivision, NoSuchUser, UserNotPermitted
    {
    	ValidationResponse response = new ValidationResponse();
        boatClassService.removeDivision(boatClassId, divisionId);
        response.setStatus("SUCCESS");
        return response;
    }
    
    @RequestMapping(value="/raceseries/{raceSeriesId}/boatclass.json/{boatClassId}/division.json",method=POST)
    public @ResponseBody ValidationResponse addDivision(
                @PathVariable("raceSeriesId") Integer   raceSeriesId,
                @PathVariable("boatClassId") Integer    boatClassId,
                @RequestBody @Valid BoatDivision        division,
                BindingResult                           bindingResult ) throws NoSuchBoatClass, NoSuchDivision, NoSuchUser, UserNotPermitted
    {
        ValidationResponse response = new ValidationResponse();
        if ( bindingResult.hasErrors() )
        {
            response.setErrorMessageList(bindingResult.getAllErrors());
            response.setStatus("FAIL");
        }
        else
        {
        	try
        	{
	        	if ( division.getId() != null && division.getId() != 0 )
	        	{
	        		boatClassService.updateDivision(boatClassId,division);
	        	}
	        	else
	        	{
	        		boatClassService.addDivision(boatClassId,division);
	        	}

	        	response.setStatus("SUCCESS");
        	}
        	catch(DuplicateDivision e)
        	{
        		bindingResult.addError(new FieldError(
        				bindingResult.getObjectName(),
                        "name",
                        division.getName(),
                        false,
                        null,
                        null,
                        "A division with the same name already exists for this boat class"));
                response.setErrorMessageList(bindingResult.getAllErrors());
                response.setStatus("FAIL");
        	} 
        }
        return response;
    }

    @Autowired
    BoatClassService boatClassService;    
}
