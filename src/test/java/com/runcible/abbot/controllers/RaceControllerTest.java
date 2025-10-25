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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.runcible.abbot.service.HandicapService;
import com.runcible.abbot.service.RaceResultService;
import com.runcible.abbot.service.RaceSeriesService;
import com.runcible.abbot.service.RaceService;
import com.runcible.abbot.web.controllers.RaceController;

@WebMvcTest(controllers = RaceController.class)
public class RaceControllerTest extends MvcTestWithJSON
{

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    public void testGetRace() throws Exception
    {
        when(raceService.getRaceByID(TEST_RACE_ID)).thenReturn(testRace);
        
        mockMvc.perform(get("/raceseries/"+TEST_RACE_SERIES_ID+"/race.json/"+TEST_RACE_ID))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name",is(TEST_RACE_NAME)))
            .andExpect(jsonPath("$.id",is(TEST_RACE_ID)));
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    public void testGetRaceDates() throws Exception
    {
        when(raceService.getRaceDays(TEST_RACE_SERIES_ID)).thenReturn(testRaceDayList);
        
        mockMvc.perform(get("/raceseries/"+TEST_RACE_SERIES_ID+"/racedays.json"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].day",is(testRaceDayList.get(0).getDay().getTime())));
    }

    @MockitoBean
    private RaceService raceService;

    @MockitoBean
    private RaceSeriesService raceSeriesService;

    @MockitoBean
    private RaceResultService raceResultService;

    @MockitoBean
    private HandicapService handicapService;
    
    @Autowired
    private MockMvc mockMvc;
}
