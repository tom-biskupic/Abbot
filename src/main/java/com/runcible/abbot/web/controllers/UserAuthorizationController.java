package com.runcible.abbot.web.controllers;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import jakarta.validation.Valid;

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

import com.runcible.abbot.model.UserSummary;
import com.runcible.abbot.service.LoggedOnUserService;
import com.runcible.abbot.service.RaceSeriesAuthorizationService;
import com.runcible.abbot.service.exceptions.CannotDeAuthorizeLastUser;
import com.runcible.abbot.service.exceptions.NoSuchRaceSeries;
import com.runcible.abbot.service.exceptions.NoSuchUser;
import com.runcible.abbot.service.exceptions.UserNotPermitted;
import com.runcible.abbot.web.model.UserToAuthorize;
import com.runcible.abbot.web.model.ValidationResponse;

@Controller
public class UserAuthorizationController
{
    @RequestMapping(value="/raceseries/{id}/authorizeduserlist.json",method=GET)
    public @ResponseBody Page<UserSummary> getAll(
            @PathVariable("id") Integer raceSeriesId,
            Pageable					page) throws NoSuchUser, UserNotPermitted
    {
        return raceSeriesAuthService.getAuthorizedUsers(raceSeriesId,page);
    }

    @RequestMapping(value="/raceseries/{id}/authorizeduser.json",method=POST)
    public @ResponseBody ValidationResponse authorizeUser(
    		@RequestBody @Valid UserToAuthorize   userToAuth,
    		BindingResult                         result,
            @PathVariable("id") Integer           raceSeriesId ) throws UserNotPermitted, NoSuchUser, NoSuchRaceSeries
    {
    	ValidationResponse response = new ValidationResponse();
    	response.setStatus("SUCCESS");
        if ( result.hasErrors() )
        {
            response.setErrorMessageList(result.getAllErrors());
            response.setStatus("FAIL");
        }
        else
        {
        	try
        	{
        	    raceSeriesAuthService.authorizeUserForRaceSeries(raceSeriesId,userToAuth.getEmailAddress());
        	}
        	catch( NoSuchUser e)
        	{
                result.addError(new FieldError(
                        result.getObjectName(),
                        "emailAddress",
                        userToAuth.getEmailAddress(),
                        false,
                        null,
                        null,
                        "The email address specified is not registered"));
                response.setErrorMessageList(result.getAllErrors());
                response.setStatus("FAIL");
        	}
        }
        
    	return response;
    }
    
    @RequestMapping(value="/raceseries/{raceSeriesId}/authorizeduser.json/{userId}",method={RequestMethod.DELETE})
    public @ResponseBody ValidationResponse removeBoatClass(
                @PathVariable("raceSeriesId") Integer   raceSeriesId,
                @PathVariable("userId") Integer         userId) throws CannotDeAuthorizeLastUser, NoSuchUser, UserNotPermitted
    {
        ValidationResponse response = new ValidationResponse();
        response.setStatus("SUCCESS");

        try
        {
            raceSeriesAuthService.deAuthorizeUserForRaceSeries(raceSeriesId, userId);
        }
        catch(CannotDeAuthorizeLastUser e)
        {
            response.setStatus("FAIL");
            response.setGeneralErrorText("Cannot de-authorize the last user");
        }
        
        return response;
    }

    @Autowired
    LoggedOnUserService loggedOnUserService;
    
    @Autowired
    RaceSeriesAuthorizationService raceSeriesAuthService;    
}
