package com.runcible.abbot.controllers;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.Charset;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.runcible.abbot.model.RaceSeries;

import com.runcible.abbot.service.LoggedOnUserService;
import com.runcible.abbot.service.RaceSeriesService;

import com.runcible.abbot.web.controllers.RaceSeriesController;

@WebMvcTest(controllers = RaceSeriesController.class)
public class RaceSeriesControllerTest extends MvcTestWithJSON
{
    @BeforeEach    
    public void setup()
    {        
        RaceSeries[] raceSeriesArray = new RaceSeries[1];
        raceSeriesArray[0] = raceSeries;
        testRaceSeriesPage = new PageImpl<RaceSeries>(
            new java.util.ArrayList<>(Arrays.asList(raceSeriesArray)), 
            PageRequest.of(0,1),
            1);
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    public void testGetPageableList() throws Exception
    {
    	when(loggedOnUserService.getLoggedOnUser()).thenReturn(user);
    	when(raceSeriesService.findAll(any(Pageable.class),eq(user))).thenReturn(testRaceSeriesPage);
    	
    	mockMvc.perform(get("/raceserieslist.json?page=1&size=3")
                    .contentType(contentType)
                    .accept(MediaType.APPLICATION_JSON))
    			.andExpect(status().isOk())
    			.andExpect(jsonPath("$.totalPages",is(1)))
    			.andExpect(jsonPath("$.number",is(0)))
    			.andExpect(jsonPath("$.content[0].name",is(SERIES_NAME)));
    }
    
    private MediaType contentType = new MediaType(
	   		MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    @MockitoBean
    private RaceSeriesService raceSeriesService;
    
    @MockitoBean
    private LoggedOnUserService loggedOnUserService;
    
    @Autowired
    private MockMvc mockMvc;
    
    private Page<RaceSeries> testRaceSeriesPage;
    
}
