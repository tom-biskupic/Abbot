package com.runcible.abbot.controllers;

import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.reset;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.runcible.abbot.model.HandicapLimit;
import com.runcible.abbot.service.HandicapService;
import com.runcible.abbot.service.exceptions.HandicapLimitAlreadyPresent;
import com.runcible.abbot.service.exceptions.InvalidUpdate;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration("file:src/test/java/com/runcible/abbot/controllers/TestApplicationContext.xml")
public class HandicapControllerTest extends MvcTestWithJSON
{
    @Before
    public void setup()
    {
        MockitoAnnotations.initMocks(this);
        setupMockMVC();
        
        HandicapLimit[] handicapLimitArray = new HandicapLimit[1];
        handicapLimitArray[0] = testHandicapLimit;
        testHandicapLimitPage = new PageImpl<HandicapLimit>(Arrays.asList(handicapLimitArray));
        
        reset(handicapService);
    }

    @Test
    public void testGetHandicapsForFleet() throws Exception
    {
        when(handicapService.getHandicapsForFleet(TEST_RACE_SERIES_ID, TEST_FLEET_ID, TEST_RACE_ID)).thenReturn(
                testHandicapList);
        
        mockMvc.perform(get("/raceseries/"+TEST_RACE_SERIES_ID+"/fleet/"+TEST_FLEET_ID+"/"+ TEST_RACE_ID + "/handicaplist.json"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(contentType))
            .andExpect(jsonPath("$[0].id", is(TEST_HANDICAP_ID)))
            .andExpect(jsonPath("$[0].boatID", is(TEST_BOAT_ID)))
            .andExpect(jsonPath("$[0].value", closeTo(TEST_HANDICAP_VALUE,0.1f)));
    }
    
    @Test
    public void testGetHandicapLimits() throws Exception
    {
        when(handicapService.getHandicapLimits(eq(TEST_RACE_SERIES_ID), any(Pageable.class)))
            .thenReturn(testHandicapLimitPage);
        
        mockMvc.perform(get("/raceseries/"+TEST_RACE_SERIES_ID+"/handicaplimitlist.json?page=1&size=3"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(contentType))
            .andExpect(jsonPath("$.totalPages",is(1)))
            .andExpect(jsonPath("$.number",is(0)))
            .andExpect(jsonPath("$.content[0].id",is(TEST_HANDICAP_LIMIT_ID)))
            .andExpect(jsonPath("$.content[0].fleet.id",is(TEST_FLEET_ID)))
            .andExpect(jsonPath("$.content[0].limit",closeTo(TEST_HANDICAP_LIMIT_VALUE,0.1)));

    }
    
    @Test
    public void testGetHandicapLimit() throws Exception
    {
        when(handicapService.getHandicapLimit(TEST_RACE_SERIES_ID, TEST_HANDICAP_LIMIT_ID))
            .thenReturn(testHandicapLimit);
        
        mockMvc.perform(get("/raceseries/"+TEST_RACE_SERIES_ID+"/handicaplimit.json/"+TEST_HANDICAP_LIMIT_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.id",is(TEST_HANDICAP_LIMIT_ID)))
                .andExpect(jsonPath("$.fleet.id",is(TEST_FLEET_ID)))
                .andExpect(jsonPath("$.limit",closeTo(TEST_HANDICAP_LIMIT_VALUE,0.1)));
    }
    
    @Test
    public void testDeleteHandicapLimit() throws Exception
    {
        
        mockMvc.perform(delete("/raceseries/"+TEST_RACE_SERIES_ID+"/handicaplimit.json/"+TEST_HANDICAP_LIMIT_ID))
            .andExpect(status().isOk())
            .andExpect(content().contentType(contentType))
            .andExpect(jsonPath("$.status",is("SUCCESS")));
        
        verify(handicapService).removeHandicapLimit(TEST_HANDICAP_LIMIT_ID);
    }
    
    @Test
    public void testSaveHandicapLimitUpdate() throws Exception
    {
        mockMvc.perform(post("/raceseries/"+TEST_RACE_SERIES_ID+"/handicaplimit.json")
            .content(convertObjectToJsonBytes(testHandicapLimit))
            .contentType(contentType))
            .andExpect(status().isOk());
        
        ArgumentCaptor<HandicapLimit> limitCaptor = ArgumentCaptor.forClass(HandicapLimit.class);
        
        verify(handicapService).updateHandicapLimit(limitCaptor.capture());
        HandicapLimit updated = limitCaptor.getValue();
        assertEquals(TEST_HANDICAP_LIMIT_ID,updated.getId());
        assertEquals(TEST_FLEET_ID,updated.getFleet().getId());
        assertEquals(TEST_HANDICAP_LIMIT_VALUE,updated.getLimit());
    }

    @Test
    public void testSaveHandicapLimitAdd() throws Exception
    {
        HandicapLimit newLimit = new HandicapLimit(null,TEST_RACE_SERIES_ID,testFleet,TEST_HANDICAP_LIMIT_VALUE);
        
        mockMvc.perform(post("/raceseries/"+TEST_RACE_SERIES_ID+"/handicaplimit.json")
            .content(convertObjectToJsonBytes(newLimit))
            .contentType(contentType))
            .andExpect(status().isOk());
        
        ArgumentCaptor<HandicapLimit> limitCaptor = ArgumentCaptor.forClass(HandicapLimit.class);
        
        verify(handicapService).addHandicapLimit(eq(TEST_RACE_SERIES_ID),limitCaptor.capture());
        HandicapLimit updated = limitCaptor.getValue();
        assertEquals(null,updated.getId());
        assertEquals(TEST_FLEET_ID,updated.getFleet().getId());
        assertEquals(TEST_HANDICAP_LIMIT_VALUE,updated.getLimit());
    }

    @Test
    public void testSaveHandicapLimitNoLimit() throws Exception
    {
        HandicapLimit newLimit = new HandicapLimit(null,TEST_RACE_SERIES_ID,testFleet,0.0f);
        
        mockMvc.perform(post("/raceseries/"+TEST_RACE_SERIES_ID+"/handicaplimit.json")
            .content(convertObjectToJsonBytes(newLimit))
            .contentType(contentType))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status", is("FAIL")))
            .andExpect(jsonPath("$.errorMessageList[0].field",is("limit")))
            .andExpect(jsonPath(
                    "$.errorMessageList[0].defaultMessage",
                    is("A positive, non-zero limit value must be specified")));
    }

    @Test
    public void testSaveHandicapLimitDuplicate() throws Exception
    {
        HandicapLimit newLimit = new HandicapLimit(null,TEST_RACE_SERIES_ID,testFleet,TEST_HANDICAP_LIMIT_VALUE);
        doThrow(new HandicapLimitAlreadyPresent("")).when(
                handicapService).addHandicapLimit(eq(TEST_RACE_SERIES_ID), any(HandicapLimit.class));
        
        postAndCheckForDuplicateError(newLimit);
    }

    @Test
    public void testSaveHandicapLimitDuplicateUpdate() throws Exception
    {
        doThrow(new InvalidUpdate()).when(
                handicapService).updateHandicapLimit(any(HandicapLimit.class));
        
        postAndCheckForDuplicateError(testHandicapLimit);
    }

    private void postAndCheckForDuplicateError(HandicapLimit newLimit)
            throws Exception, IOException
    {
        mockMvc.perform(post("/raceseries/"+TEST_RACE_SERIES_ID+"/handicaplimit.json")
            .content(convertObjectToJsonBytes(newLimit))
            .contentType(contentType))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status", is("FAIL")))
            .andExpect(jsonPath("$.errorMessageList[0].field",is("fleet")))
            .andExpect(jsonPath(
                    "$.errorMessageList[0].defaultMessage",
                    is("A handicap limit has already been specified for this fleet")));
    }

    @Autowired
    private HandicapService handicapService;
    
    private Page<HandicapLimit> testHandicapLimitPage;
}
