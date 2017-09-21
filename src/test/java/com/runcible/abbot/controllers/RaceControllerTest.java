package com.runcible.abbot.controllers;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.runcible.abbot.service.BoatService;
import com.runcible.abbot.service.RaceService;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration("file:src/test/java/com/runcible/abbot/controllers/TestApplicationContext.xml")
public class RaceControllerTest extends MvcTestWithJSON
{
    @Before
    public void setup()
    {
        MockitoAnnotations.initMocks(this);
        setupMockMVC();
    }

    @Test
    public void testGetRace() throws Exception
    {
        when(raceService.getRaceByID(TEST_RACE_ID)).thenReturn(testRace);
        
        mockMvc.perform(get("/raceseries/"+TEST_RACE_SERIES_ID+"/race.json/"+TEST_RACE_ID))
            .andExpect(status().isOk())
            .andExpect(content().contentType(contentType))
            .andExpect(jsonPath("$.name",is(TEST_RACE_NAME)))
            .andExpect(jsonPath("$.id",is(TEST_RACE_ID)));
    }

    @Test
    public void testGetRaceDates() throws Exception
    {
        when(raceService.getRaceDays(TEST_RACE_SERIES_ID)).thenReturn(testRaceDayList);
        
        mockMvc.perform(get("/raceseries/"+TEST_RACE_SERIES_ID+"/racedays.json"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(contentType))
            .andExpect(jsonPath("$[0].day",is(testRaceDayList.get(0).getDay().getTime())));
    }

    @Autowired
    private RaceService raceService;
}
