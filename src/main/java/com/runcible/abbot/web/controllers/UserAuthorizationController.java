package com.runcible.abbot.web.controllers;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.runcible.abbot.model.UserSummary;
import com.runcible.abbot.service.LoggedOnUserService;
import com.runcible.abbot.service.RaceSeriesAuthorizationService;
import com.runcible.abbot.service.exceptions.CannotDeAuthorizeLastUser;
import com.runcible.abbot.service.exceptions.NoSuchRaceSeries;
import com.runcible.abbot.service.exceptions.NoSuchUser;
import com.runcible.abbot.service.exceptions.UserNotPermitted;
import com.runcible.abbot.web.model.UserToAuthorize;
import com.runcible.abbot.web.model.ValidationResponse;

@RestController
public class UserAuthorizationController
{
    @GetMapping(value="/raceseries/{id}/authorizeduserlist.json")
    public @ResponseBody Page<UserSummary> getAll(
            @PathVariable("id") Integer raceSeriesId,
            Pageable					page) throws NoSuchUser, UserNotPermitted
    {
        return raceSeriesAuthService.getAuthorizedUsers(raceSeriesId,page);
    }

    @PostMapping(value="/raceseries/{id}/authorizeduser.json")
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
    
    @DeleteMapping(value="/raceseries/{raceSeriesId}/authorizeduser.json/{userId}")
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
