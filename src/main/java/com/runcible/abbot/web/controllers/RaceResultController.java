package com.runcible.abbot.web.controllers;

import java.util.Collection;

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

import com.runcible.abbot.model.Boat;
import com.runcible.abbot.model.RaceResult;
import com.runcible.abbot.model.ResultStatus;
import com.runcible.abbot.service.HandicapService;
import com.runcible.abbot.service.RaceResultService;
import com.runcible.abbot.service.RaceSeriesService;
import com.runcible.abbot.service.exceptions.DuplicateResult;
import com.runcible.abbot.service.exceptions.NoSuchBoat;
import com.runcible.abbot.service.exceptions.NoSuchCompetition;
import com.runcible.abbot.service.exceptions.NoSuchFleet;
import com.runcible.abbot.service.exceptions.NoSuchRaceResult;
import com.runcible.abbot.service.exceptions.NoSuchUser;
import com.runcible.abbot.service.exceptions.UserNotPermitted;
import com.runcible.abbot.web.model.ValidationResponse;

@RestController
public class RaceResultController 
{
    @GetMapping(value="/raceseries/{raceseriesid}/race/{raceid}/resultlist.json")
    public @ResponseBody Page<RaceResult> getResultsForRace(
            @PathVariable("raceseriesid")  Integer raceSeriesId,
            @PathVariable("raceid")        Integer raceId,
            Pageable                    		p) throws NoSuchUser, UserNotPermitted
    {
        return raceResultService.findAll(raceId,p);
    }

    @GetMapping(value="/raceseries/{raceseriesid}/race/{raceid}/result.json/{raceresultid}")
    public @ResponseBody RaceResult getResult(
            @PathVariable("raceseriesid")  Integer raceSeriesId,
            @PathVariable("raceid")        Integer raceId,
            @PathVariable("raceresultid")  Integer resultId) throws NoSuchUser, UserNotPermitted, NoSuchRaceResult
    {
        return raceResultService.getResultByID(resultId);
    }

    @PostMapping(value="/raceseries/{raceseriesid}/race/{raceid}/result.json")
    public @ResponseBody ValidationResponse save(
                @Valid @RequestBody RaceResult	raceResult,
                BindingResult                   bindingResult,
                @PathVariable("raceseriesid") Integer raceSeriesId,
                @PathVariable("raceid") Integer raceId) throws NoSuchUser, UserNotPermitted, NoSuchRaceResult, NoSuchBoat, DuplicateResult
    {
        ValidationResponse response = new ValidationResponse();
        if ( bindingResult.hasErrors() )
        {
            response.setErrorMessageList(bindingResult.getAllErrors());
            response.setStatus("FAIL");
        }
        else
        {
            //
            //  Now do some more complex validation
            //
            if ( competitorStarted(raceResult) )
            {
                if ( raceResult.getStartTime() == null )
                {
                    addValidationFailure(
                            bindingResult, 
                            response, 
                            bindingResult.getObjectName(),
                            "startTime", 
                            raceResult.getStartTime(),
                            "The start time must be provided if the competitor started or finished");
                    
                    return response;
                }
            }
            
            if ( competitorFinished(raceResult) )
            {
                if ( raceResult.getFinishTime() == null )
                {
                    addValidationFailure(
                            bindingResult, 
                            response, 
                            bindingResult.getObjectName(),
                            "finishTime", 
                            raceResult.getFinishTime(),
                            "The finish time must be provided if the competitor finished");
                    
                    return response;
                }
            }
            
            if ( raceResult.getId() != null && raceResult.getId() != 0 )
            {
            	raceResultService.updateResult(raceResult);
            }
            else
            {
            	raceResultService.addResult(raceId,raceResult);    
            }
            
            response.setStatus("SUCCESS");
        }
        return response;
    }

    private boolean competitorFinished(RaceResult raceResult)
    {
        return  competitorStarted(raceResult)
                &&
                raceResult.getStatus() != ResultStatus.DNF
                &&
                raceResult.getStatus() != ResultStatus.RDG;
    }

    private boolean competitorStarted(RaceResult raceResult)
    {
        return raceResult.getStatus() != ResultStatus.DNC 
                &&
                raceResult.getStatus() != ResultStatus.DNS;
    }

    private void addValidationFailure(
            BindingResult       bindingResult,
            ValidationResponse  response, 
            String              objectName, 
            String              fieldName,
            Object              field,
            String              message)
    {
        bindingResult.addError(new FieldError(
                objectName,
                fieldName,
                field,
                false,
                null,
                null,
                message));
        response.setErrorMessageList(bindingResult.getAllErrors());
        response.setStatus("FAIL");
    }

    @DeleteMapping(value="/raceseries/{raceseriesid}/race/{raceid}/result.json/{raceresultid}")
    public @ResponseBody ValidationResponse removeRace(
                @PathVariable("raceseriesid") Integer   raceSeriesId,
                @PathVariable("raceid") Integer         raceId,
                @PathVariable("raceresultid") Integer   raceResultId) 
                        throws NoSuchUser, UserNotPermitted, NoSuchCompetition, NoSuchRaceResult
    {
        ValidationResponse response = new ValidationResponse();
        raceResultService.removeResult(raceResultId);
        response.setStatus("SUCCESS");
        return response;
    }

    @GetMapping(value="/raceseries/{raceseriesid}/race/{raceid}/boatsnotselected.json")
    public @ResponseBody Collection<Boat> getUnaddedBoats(
                @PathVariable("raceseriesid") Integer   raceSeriesId,
                @PathVariable("raceid") Integer         raceId) 
                        throws NoSuchUser, UserNotPermitted, NoSuchFleet
    {
        return raceResultService.findBoatsNotInRace(raceId);
    }   
    
    @PostMapping(value="/raceseries/{raceSeriesId}/race/{raceid}/addnonstarters.json")
    public @ResponseBody ValidationResponse updateRaceStatus(
            @RequestBody                    String  resultStatusString,
            @PathVariable("raceSeriesId")   Integer raceSeriesID,
            @PathVariable("raceid")         Integer raceID ) throws NoSuchUser, UserNotPermitted, NoSuchFleet, NoSuchBoat
    {
        ValidationResponse response = new ValidationResponse();
        ResultStatus resultStatus = ResultStatus.valueOf(resultStatusString);
        raceResultService.addNonStartersToRace(raceID, resultStatus);
        response.setStatus("SUCCESS");
        return response;
    }

    @PostMapping(value="/raceseries/{raceSeriesId}/race/{raceid}/updatehandicaps.json")
    public @ResponseBody ValidationResponse updateRaceResultHandicaps(
            @RequestBody                    String body,
            @PathVariable("raceSeriesId")   Integer raceSeriesID,
            @PathVariable("raceid")         Integer raceID ) throws NoSuchUser, UserNotPermitted, NoSuchRaceResult, DuplicateResult, NoSuchFleet
    {
        ValidationResponse response = new ValidationResponse();
        handicapService.updateHandicapsFromPreviousRace(raceID);
        response.setStatus("SUCCESS");
        return response;
    }
    
    @Autowired RaceResultService	raceResultService;
    @Autowired RaceSeriesService 	raceSeriesService;
    @Autowired HandicapService      handicapService;
}
