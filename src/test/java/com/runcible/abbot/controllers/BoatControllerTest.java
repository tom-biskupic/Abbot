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
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.runcible.abbot.model.Boat;
import com.runcible.abbot.service.BoatService;
import com.runcible.abbot.service.exceptions.NoSuchBoat;
import com.runcible.abbot.service.exceptions.NoSuchUser;
import com.runcible.abbot.service.exceptions.UserNotPermitted;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration("file:src/test/java/com/runcible/abbot/controllers/TestApplicationContext.xml")
public class BoatControllerTest extends MvcTestWithJSON
{
    @Before
    public void setup()
    {
        MockitoAnnotations.initMocks(this);
        setupMockMVC();
        //reset(userService);
    }

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
    
    @Autowired
    private BoatService boatService;
}
