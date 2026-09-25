package com.runcible.abbot.controllers;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashSet;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.runcible.abbot.model.Fleet;
import com.runcible.abbot.service.FleetService;
import com.runcible.abbot.service.RaceService;
import com.runcible.abbot.service.exceptions.FleetInUse;
import com.runcible.abbot.web.controllers.FleetController;

@WebMvcTest(controllers = FleetController.class)
public class FleetControllerTest extends MvcTestWithJSON
{
    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    public void testSaveFleetAdd() throws Exception
    {
        Fleet newFleet = new Fleet(null,TEST_RACE_SERIES_ID,TEST_FLEET_NAME,TEST_FLEET_SELECTORS,false);

        postFleet(newFleet)
            .andExpect(jsonPath("$.status", is("SUCCESS")));

        verify(fleetService).addFleet(eq(TEST_RACE_SERIES_ID), any(Fleet.class));
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    public void testSaveFleetNoClasses() throws Exception
    {
        Fleet newFleet = new Fleet(null,TEST_RACE_SERIES_ID,TEST_FLEET_NAME,new HashSet<>(),false);

        postFleet(newFleet)
            .andExpect(jsonPath("$.status", is("FAIL")))
            .andExpect(jsonPath("$.errorMessageList[0].field",is("fleetClasses")))
            .andExpect(jsonPath(
                    "$.errorMessageList[0].defaultMessage",
                    is("At least one class must be added to the fleet")));

        verify(fleetService, never()).addFleet(any(), any());
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    public void testSaveFleetUpdateNoClasses() throws Exception
    {
        Fleet updated = new Fleet(TEST_FLEET_ID,TEST_RACE_SERIES_ID,TEST_FLEET_NAME,new HashSet<>(),false);

        postFleet(updated)
            .andExpect(jsonPath("$.status", is("FAIL")))
            .andExpect(jsonPath("$.errorMessageList[0].field",is("fleetClasses")));

        verify(fleetService, never()).updateFleet(any());
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    public void testSaveFleetNoName() throws Exception
    {
        Fleet newFleet = new Fleet(null,TEST_RACE_SERIES_ID," ",TEST_FLEET_SELECTORS,false);

        postFleet(newFleet)
            .andExpect(jsonPath("$.status", is("FAIL")))
            .andExpect(jsonPath("$.errorMessageList[0].field",is("fleetName")))
            .andExpect(jsonPath(
                    "$.errorMessageList[0].defaultMessage",
                    is("A fleet name must be provided")));

        verify(fleetService, never()).addFleet(any(), any());
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"ADMIN"})
    public void testDeleteFleetInUse() throws Exception
    {
        doThrow(new FleetInUse("in use")).when(fleetService).removeFleet(TEST_FLEET_ID);

        mockMvc.perform(
            delete("/raceseries/"+TEST_RACE_SERIES_ID+"/fleet.json/"+TEST_FLEET_ID)
                .with(csrf())
                .contentType(contentType)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status",is("FAIL")))
            .andExpect(jsonPath("$.generalErrorText",is("in use")));
    }

    private ResultActions postFleet(Fleet fleet) throws Exception
    {
        return mockMvc.perform(post("/raceseries/"+TEST_RACE_SERIES_ID+"/fleet.json")
                .with(csrf())
                .content(convertObjectToJsonBytes(fleet))
                .contentType(contentType)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }

    @MockitoBean
    private FleetService fleetService;

    @MockitoBean
    private RaceService raceService;

    @Autowired
    private MockMvc mockMvc;
}
