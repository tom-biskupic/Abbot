package com.runcible.abbot.web.controllers;

import java.util.Collection;
import java.util.List;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.runcible.abbot.model.ExportConfig;
import com.runcible.abbot.service.ExportConfigService;
import com.runcible.abbot.service.ExportService;
import com.runcible.abbot.service.exceptions.NoSuchCompetition;
import com.runcible.abbot.service.exceptions.NoSuchExportConfig;
import com.runcible.abbot.service.exceptions.NoSuchFleet;
import com.runcible.abbot.service.exceptions.NoSuchUser;
import com.runcible.abbot.service.exceptions.UserNotPermitted;
import com.runcible.abbot.web.model.ValidationResponse;

@RestController
public class ExportController
{
    // -------------------------------------------------------------------------
    // ExportConfig CRUD
    // -------------------------------------------------------------------------

    @GetMapping(value="/raceseries/{id}/exportconfiglist.json")
    public @ResponseBody Page<ExportConfig> listPaged(
            @PathVariable("id") Integer raceSeriesId,
            Pageable                    pageable) throws NoSuchUser, UserNotPermitted
    {
        return exportConfigService.getAllForSeries(raceSeriesId, pageable);
    }

    @GetMapping(value="/raceseries/{id}/exportconfiglist.json/all")
    public @ResponseBody List<ExportConfig> listAll(
            @PathVariable("id") Integer raceSeriesId) throws NoSuchUser, UserNotPermitted
    {
        return exportConfigService.getAllForSeries(raceSeriesId);
    }

    @GetMapping(value="/raceseries/{raceSeriesId}/exportconfig.json/{exportConfigId}")
    public @ResponseBody ExportConfig getExportConfig(
            @PathVariable("raceSeriesId")    Integer raceSeriesId,
            @PathVariable("exportConfigId")  Integer exportConfigId)
                    throws NoSuchExportConfig, NoSuchUser, UserNotPermitted
    {
        return exportConfigService.getById(exportConfigId);
    }

    @PostMapping(value="/raceseries/{id}/exportconfig.json")
    public @ResponseBody ValidationResponse saveExportConfig(
            @Valid @RequestBody ExportConfig exportConfig,
            BindingResult                    result,
            @PathVariable("id") Integer      raceSeriesId) throws NoSuchExportConfig, NoSuchUser, UserNotPermitted
    {
        ValidationResponse response = new ValidationResponse();
        if (result.hasErrors())
        {
            response.setErrorMessageList(result.getAllErrors());
            response.setStatus("FAIL");
        }
        else
        {
            if (exportConfig.getId() != null && exportConfig.getId() != 0)
            {
                exportConfigService.updateExportConfig(exportConfig);
            }
            else
            {
                exportConfigService.addExportConfig(raceSeriesId, exportConfig);
            }
            response.setStatus("SUCCESS");
        }
        return response;
    }

    @DeleteMapping(value="/raceseries/{raceSeriesId}/exportconfig.json/{exportConfigId}")
    public @ResponseBody ValidationResponse deleteExportConfig(
            @PathVariable("raceSeriesId")   Integer raceSeriesId,
            @PathVariable("exportConfigId") Integer exportConfigId)
                    throws NoSuchExportConfig, NoSuchUser, UserNotPermitted
    {
        exportConfigService.removeExportConfig(exportConfigId);
        ValidationResponse response = new ValidationResponse();
        response.setStatus("SUCCESS");
        return response;
    }

    // -------------------------------------------------------------------------
    // Export generation
    // -------------------------------------------------------------------------

    @GetMapping(value="/raceseries/{raceSeriesId}/exportAll.json/{exportConfigId}")
    public ResponseEntity<ByteArrayResource> exportAll(
                @PathVariable("raceSeriesId")    Integer raceSeriesId,
                @PathVariable("exportConfigId")  Integer exportConfigId)
                        throws NoSuchExportConfig, NoSuchUser, UserNotPermitted, NoSuchCompetition, NoSuchFleet
    {
        ExportConfig config = exportConfigService.getById(exportConfigId);
        String html = exportService.exportAll(raceSeriesId, config);
        return makeResponseEntity(html.getBytes());
    }

    @GetMapping(value="/raceseries/{raceSeriesId}/exportPoints.json")
    public ResponseEntity<ByteArrayResource> exportPointsHTML(
                @PathVariable("raceSeriesId")   Integer             raceSeriesId,
                @RequestParam("competition")    Collection<Integer> competitionIds)
                        throws NoSuchUser, UserNotPermitted, NoSuchCompetition, NoSuchFleet
    {
        String pointsAsHTML = exportService.exportCompetitions(raceSeriesId, competitionIds);
        return makeResponseEntity(pointsAsHTML.getBytes());
    }

    @GetMapping(value="/raceseries/{raceSeriesId}/exportRaces.json/{fleetId}")
    public ResponseEntity<ByteArrayResource> exportRacesHTML(
                @PathVariable("raceSeriesId") Integer raceSeriesID,
                @PathVariable("fleetId")      Integer fleetID)
                        throws NoSuchUser, UserNotPermitted, NoSuchCompetition, NoSuchFleet
    {
        String pointsAsHTML = exportService.exportRaces(raceSeriesID, fleetID);
        return makeResponseEntity(pointsAsHTML.getBytes());
    }

    @GetMapping(value="/raceseries/{raceSeriesId}/exportHandicaps.json/{fleetId}")
    public ResponseEntity<ByteArrayResource> exportHandicapsHTML(
                @PathVariable("raceSeriesId") Integer raceSeriesID,
                @PathVariable("fleetId")      Integer fleetID)
                        throws NoSuchUser, UserNotPermitted, NoSuchCompetition, NoSuchFleet
    {
        String pointsAsHTML = exportService.exportHandicapTable(raceSeriesID, fleetID, false);
        return makeResponseEntity(pointsAsHTML.getBytes());
    }

    @GetMapping(value="/raceseries/{raceSeriesId}/exportShortCourseHandicaps.json/{fleetId}")
    public ResponseEntity<ByteArrayResource> exportShortCourseHandicapsHTML(
                @PathVariable("raceSeriesId") Integer raceSeriesID,
                @PathVariable("fleetId")      Integer fleetID)
                        throws NoSuchUser, UserNotPermitted, NoSuchCompetition, NoSuchFleet
    {
        String pointsAsHTML = exportService.exportHandicapTable(raceSeriesID, fleetID, true);
        return makeResponseEntity(pointsAsHTML.getBytes());
    }

    private ResponseEntity<ByteArrayResource> makeResponseEntity(byte[] asBytes)
    {
        ByteArrayResource resource = new ByteArrayResource(asBytes);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=export.html")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .contentLength(asBytes.length)
                .body(resource);
    }

    @Autowired
    private ExportConfigService exportConfigService;

    @Autowired
    private ExportService exportService;
}
