package com.runcible.abbot.controllers;

import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.runcible.abbot.model.UserSummary;
import com.runcible.abbot.service.LoggedOnUserService;
import com.runcible.abbot.service.RaceSeriesAuthorizationService;
import com.runcible.abbot.service.exceptions.CannotDeAuthorizeLastUser;
import com.runcible.abbot.service.exceptions.NoSuchUser;
import com.runcible.abbot.web.model.UserToAuthorize;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration("file:src/test/java/com/runcible/abbot/controllers/TestApplicationContext.xml")
public class UserAuthorizationControllerTest extends MvcTestWithJSON
{
    @Before
    public void setup()
    {
        MockitoAnnotations.initMocks(this);
        setupMockMVC();
        reset(raceSeriesAuthService);
    }

    @Test
    public void testGetAuthorizedUsers() throws Exception
    {
        UserSummary testUser = new UserSummary(TEST_ID,TEST_NAME,TEST_EMAIL,true);
        List<UserSummary> testUserList = Arrays.asList(testUser);
        Pageable page = new PageRequest(0,3);
        Page<UserSummary> userSummaryPage = new PageImpl<UserSummary>(testUserList,page,1);
        
        when(raceSeriesAuthService.getAuthorizedUsers(eq(TEST_RACE_SERIES_ID),any(Pageable.class))).thenReturn(userSummaryPage);
        
        mockMvc.perform(get("/raceseries/"+TEST_RACE_SERIES_ID+"/authorizeduserlist.json"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(contentType))
        .andExpect(jsonPath("$.content[0].emailAddress",is(TEST_EMAIL)))
        .andExpect(jsonPath("$.content[0].name",is(TEST_NAME)))
        .andExpect(jsonPath("$.content[0].id",is(TEST_ID)));
    }

    @Test
    public void testAuthorizeUser() throws Exception
    {
        UserToAuthorize   userToAuth = new UserToAuthorize(TEST_EMAIL);
        
        mockMvc.perform(
                post("/raceseries/"+TEST_RACE_SERIES_ID+"/authorizeduser.json")
                    .content(convertObjectToJsonBytes(userToAuth))
                    .contentType(contentType))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status",is("SUCCESS")));

        verify(raceSeriesAuthService).authorizeUserForRaceSeries(TEST_RACE_SERIES_ID, TEST_EMAIL);
    }

    @Test
    public void testAuthorizeUserInvalidEmail() throws Exception
    {
        UserToAuthorize   userToAuth = new UserToAuthorize("jibberish");
        
        mockMvc.perform(
                post("/raceseries/"+TEST_RACE_SERIES_ID+"/authorizeduser.json")
                    .content(convertObjectToJsonBytes(userToAuth))
                    .contentType(contentType))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status",is("FAIL")))
                    .andExpect(jsonPath("$.errorMessageList[0].field",is("emailAddress")))
                    .andExpect(jsonPath("$.errorMessageList[0].defaultMessage",is("Email address is not valid")));

        verifyNoMoreInteractions(raceSeriesAuthService);
    }

    @Test
    public void testAuthorizeUserEmptyEmail() throws Exception
    {
        UserToAuthorize   userToAuth = new UserToAuthorize("");
        
        mockMvc.perform(
                post("/raceseries/"+TEST_RACE_SERIES_ID+"/authorizeduser.json")
                    .content(convertObjectToJsonBytes(userToAuth))
                    .contentType(contentType))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status",is("FAIL")))
                    .andExpect(jsonPath("$.errorMessageList[0].field",is("emailAddress")))
                    .andExpect(jsonPath("$.errorMessageList[0].defaultMessage",is("Email address cannot be empty")));

        verifyNoMoreInteractions(raceSeriesAuthService);
    }

    @Test
    public void testAuthorizeUserInvalidUser() throws Exception
    {
        UserToAuthorize   userToAuth = new UserToAuthorize(TEST_EMAIL);
        
        doThrow(new NoSuchUser()).when(raceSeriesAuthService).authorizeUserForRaceSeries(TEST_RACE_SERIES_ID, TEST_EMAIL);
        
        mockMvc.perform(
                post("/raceseries/"+TEST_RACE_SERIES_ID+"/authorizeduser.json")
                    .content(convertObjectToJsonBytes(userToAuth))
                    .contentType(contentType))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status",is("FAIL")))
                    .andExpect(jsonPath("$.errorMessageList[0].field",is("emailAddress")))
                    .andExpect(jsonPath("$.errorMessageList[0].defaultMessage",is("The email address specified is not registered")));
    }
    
    @Test
    public void testDeAuthorizeUser() throws Exception
    {
        mockMvc.perform(
                delete("/raceseries/"+TEST_RACE_SERIES_ID+"/authorizeduser.json/"+TEST_ID)
                    .contentType(contentType))
                    .andExpect(status().isOk());
        
        verify(raceSeriesAuthService).deAuthorizeUserForRaceSeries(TEST_RACE_SERIES_ID, TEST_ID);
    }

    @Test
    public void testDeAuthorizeUserLastUser() throws Exception
    {
        doThrow(new CannotDeAuthorizeLastUser()).when(raceSeriesAuthService).deAuthorizeUserForRaceSeries(TEST_RACE_SERIES_ID, TEST_ID);
        
        mockMvc.perform(
                delete("/raceseries/"+TEST_RACE_SERIES_ID+"/authorizeduser.json/"+TEST_ID)
                    .contentType(contentType))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status",is("FAIL")))
                    .andExpect(jsonPath("$.generalErrorText",is("Cannot de-authorize the last user")));
    }
    
    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private RaceSeriesAuthorizationService raceSeriesAuthService;
    
    @Autowired
    LoggedOnUserService loggedOnUserService;
}
