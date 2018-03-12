package com.runcible.abbot.web.controllers;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.Collection;

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

import com.runcible.abbot.model.Boat;
import com.runcible.abbot.model.RaceResult;
import com.runcible.abbot.model.ResultStatus;
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

@Controller
public class RaceResultController 
{
    @RequestMapping(value="/raceseries/{raceseriesid}/race/{raceid}/resultlist.json",method=GET)
    public @ResponseBody Page<RaceResult> getResultsForRace(
            @PathVariable("raceseriesid")  Integer raceSeriesId,
            @PathVariable("raceid")        Integer raceId,
            Pageable                    		p) throws NoSuchUser, UserNotPermitted
    {
        return raceResultService.findAll(raceId,p);
    }

    @RequestMapping(value="/raceseries/{raceseriesid}/race/{raceid}/result.json/{raceresultid}",method=GET)
    public @ResponseBody RaceResult getResult(
            @PathVariable("raceseriesid")  Integer raceSeriesId,
            @PathVariable("raceid")        Integer raceId,
            @PathVariable("raceresultid")  Integer resultId) throws NoSuchUser, UserNotPermitted, NoSuchRaceResult
    {
        return raceResultService.getResultByID(resultId);
    }

    @RequestMapping(value="/raceseries/{raceseriesid}/race/{raceid}/result.json",method=POST)
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
                            "The start time must be provided if the competitor finished");
                    
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
                raceResult.getStatus() != ResultStatus.DNF;
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

    @RequestMapping(value="/raceseries/{raceseriesid}/race/{raceid}/result.json/{raceresultid}",method={RequestMethod.DELETE})
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

    @RequestMapping(value="/raceseries/{raceseriesid}/race/{raceid}/boatsnotselected.json",method={RequestMethod.GET})
    public @ResponseBody Collection<Boat> getUnaddedBoats(
                @PathVariable("raceseriesid") Integer   raceSeriesId,
                @PathVariable("raceid") Integer         raceId) 
                        throws NoSuchUser, UserNotPermitted, NoSuchFleet
    {
        return raceResultService.findBoatsNotInRace(raceId);
    }   
    
    @RequestMapping(value="/raceseries/{raceSeriesId}/race/{raceid}/addnonstarters.json",method= {RequestMethod.POST})
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

    @Autowired RaceResultService	raceResultService;
    @Autowired RaceSeriesService 	raceSeriesService;
}
