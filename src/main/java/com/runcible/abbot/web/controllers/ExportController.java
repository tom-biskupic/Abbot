package com.runcible.abbot.web.controllers;


import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.runcible.abbot.service.ExportService;
import com.runcible.abbot.service.exceptions.NoSuchCompetition;
import com.runcible.abbot.service.exceptions.NoSuchFleet;
import com.runcible.abbot.service.exceptions.NoSuchUser;
import com.runcible.abbot.service.exceptions.UserNotPermitted;

@RestController
public class ExportController
{
    @GetMapping(value="/raceseries/{raceSeriesId}/exportPoints.json")
    public ResponseEntity<ByteArrayResource> exportPointsHTML(
                @PathVariable("raceSeriesId")   Integer       raceSeriesId,
                @RequestParam("competition")  Collection<Integer>   competitionIds ) 
                        throws NoSuchUser, UserNotPermitted, NoSuchCompetition, NoSuchFleet
    {
        String pointsAsHTML = exportService.exportCompetitions(raceSeriesId, competitionIds);
        return makeResponseEntity(pointsAsHTML.getBytes());
        
    }

    @GetMapping(value="/raceseries/{raceSeriesId}/exportRaces.json/{fleetId}")
    public ResponseEntity<ByteArrayResource> exportRacesHTML(
                @PathVariable("raceSeriesId") Integer   raceSeriesID,
                @PathVariable("fleetId")Integer   fleetID ) 
                        throws NoSuchUser, UserNotPermitted, NoSuchCompetition, NoSuchFleet
    {
        String pointsAsHTML = exportService.exportRaces(raceSeriesID, fleetID);
        return makeResponseEntity(pointsAsHTML.getBytes());
    }

    @GetMapping(value="/raceseries/{raceSeriesId}/exportHandicaps.json/{fleetId}")
    public ResponseEntity<ByteArrayResource> exportHandicapsHTML(
                @PathVariable("raceSeriesId") Integer   raceSeriesID,
                @PathVariable("fleetId") Integer        fleetID ) 
                        throws NoSuchUser, UserNotPermitted, NoSuchCompetition, NoSuchFleet
    {
        String pointsAsHTML = exportService.exportHandicapTable(raceSeriesID, fleetID, false);
        return makeResponseEntity(pointsAsHTML.getBytes());
    }

    @GetMapping(value="/raceseries/{raceSeriesId}/exportShortCourseHandicaps.json/{fleetId}")
    public ResponseEntity<ByteArrayResource> exportShortCourseHandicapsHTML(
                @PathVariable("raceSeriesId") Integer   raceSeriesID,
                @PathVariable("fleetId") Integer        fleetID ) 
                        throws NoSuchUser, UserNotPermitted, NoSuchCompetition, NoSuchFleet
    {
        String pointsAsHTML = exportService.exportHandicapTable(raceSeriesID, fleetID, true);
        return makeResponseEntity(pointsAsHTML.getBytes());
    }

    private ResponseEntity<ByteArrayResource> makeResponseEntity(byte[] asBytes)
    {
        ByteArrayResource resource = new ByteArrayResource(asBytes);
        
        String fileName = "export.html";
        
        return ResponseEntity.ok()
                .header(    HttpHeaders.CONTENT_DISPOSITION, 
                            "attachment;filename="+fileName)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .contentLength(asBytes.length)
                .body(resource);
    }

    @Autowired
    private ExportService exportService;
}
