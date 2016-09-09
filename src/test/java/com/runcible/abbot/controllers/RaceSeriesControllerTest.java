package com.runcible.abbot.controllers;

import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.Charset;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.runcible.abbot.model.RaceSeries;
import com.runcible.abbot.model.RaceSeriesType;
import com.runcible.abbot.model.User;
import com.runcible.abbot.service.LoggedOnUserService;
import com.runcible.abbot.service.RaceSeriesService;
import com.runcible.abbot.service.exceptions.NoSuchUser;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration("file:src/test/java/com/runcible/abbot/controllers/TestApplicationContext.xml")
public class RaceSeriesControllerTest extends MvcTestWithJSON
{
    @Before
    public void setup()
    {
        MockitoAnnotations.initMocks(this);
        setupMockMVC();
        
        RaceSeries[] raceSeriesArray = new RaceSeries[1];
        raceSeriesArray[0] = raceSeries;
        testPage = new PageImpl<RaceSeries>(Arrays.asList(raceSeriesArray));
    }

    @Test
    public void testGetPageableList() throws Exception
    {
    	when(loggedOnUserService.getLoggedOnUser()).thenReturn(user);
    	when(raceSeriesService.findAll(any(Pageable.class),eq(user))).thenReturn(testPage);
    	
    	mockMvc.perform(get("/raceserieslist.json?page=1&size=3")
    			.contentType(contentType))
    			.andExpect(status().isOk())
    			.andExpect(jsonPath("$.totalPages",is(1)))
    			.andExpect(jsonPath("$.number",is(0)))
    			.andExpect(jsonPath("$.content[0].name",is(SERIES_NAME)));
    }
    
    private MediaType contentType = new MediaType(
	   		MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    @Autowired
    private RaceSeriesService raceSeriesService;
    
    @Autowired
    private LoggedOnUserService loggedOnUserService;
    
    
    private Page<RaceSeries> testPage;
}
