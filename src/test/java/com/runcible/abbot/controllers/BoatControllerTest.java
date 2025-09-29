package com.runcible.abbot.controllers;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;


import com.runcible.abbot.service.BoatService;
import com.runcible.abbot.service.RaceSeriesService;
import com.runcible.abbot.web.controllers.BoatController;

import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;


@WebMvcTest(controllers = BoatController.class)
public class BoatControllerTest extends MvcTestWithJSON
{

    @Test
    public void testGetBoat() throws Exception
    {
        when(boatService.getBoatByID(TEST_BOAT_ID)).thenReturn(testBoat);
        
        mockMvc.perform(get("/raceseries/"+TEST_RACE_SERIES_ID+"/boat.json/"+TEST_BOAT_ID))
            .andExpect(status().isOk())
            .andExpect(content().contentType(contentType))
            .andExpect(jsonPath("$.sailNumber",is(TEST_SAIL_NUMBER)))
            .andExpect(jsonPath("$.name",is(TEST_BOAT_NAME)))
            .andExpect(jsonPath("$.id",is(TEST_BOAT_ID)));
    }
    
    @MockitoBean
    private BoatService boatService;

    @MockitoBean
    private RaceSeriesService raceSeriesService;

    @Autowired
    private MockMvc mockMvc;
}
