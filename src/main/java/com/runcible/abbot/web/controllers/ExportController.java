package com.runcible.abbot.web.controllers;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.runcible.abbot.service.ExportService;
import com.runcible.abbot.service.exceptions.NoSuchCompetition;
import com.runcible.abbot.service.exceptions.NoSuchFleet;
import com.runcible.abbot.service.exceptions.NoSuchUser;
import com.runcible.abbot.service.exceptions.UserNotPermitted;

@Controller
public class ExportController
{
    @RequestMapping(value="/raceseries/{raceSeriesId}/exportPoints.json/{competitionId}",method=GET)
    public ResponseEntity<ByteArrayResource> exportPointsHTML(
                @PathVariable("raceSeriesId") Integer   raceSeriesId,
                @PathVariable("competitionId")Integer   competitionId ) 
                        throws NoSuchUser, UserNotPermitted, NoSuchCompetition, NoSuchFleet
    {
        String pointsAsHTML = exportService.exportCompetition(raceSeriesId, competitionId);
        return makeResponseEntity(pointsAsHTML.getBytes());
        
    }

    @RequestMapping(value="/raceseries/{raceSeriesId}/exportRaces.json/{fleetId}",method=GET)
    public ResponseEntity<ByteArrayResource> exportRacesHTML(
                @PathVariable("raceSeriesId") Integer   raceSeriesID,
                @PathVariable("fleetId")Integer   fleetID ) 
                        throws NoSuchUser, UserNotPermitted, NoSuchCompetition, NoSuchFleet
    {
        String pointsAsHTML = exportService.exportRaces(raceSeriesID, fleetID);
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
