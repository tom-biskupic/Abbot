package com.runcible.abbot.service;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.runcible.abbot.model.Race;
import com.runcible.abbot.repository.RaceRespository;
import com.runcible.abbot.service.exceptions.NoSuchUser;
import com.runcible.abbot.service.exceptions.UserNotPermitted;

@RunWith(MockitoJUnitRunner.class)
public class RaceServiceTest
{
    @Test
    public void testAddRace() throws NoSuchUser, UserNotPermitted
    {
        setupCheckPermissionsMocks(true);
        fixture.addRace(TEST_RACE_SERIES_ID, raceMock);
        verify(raceRepoMock).save(raceMock);
    } 

    @Test(expected=UserNotPermitted.class)
    public void testAddRaceNotPermitted() throws NoSuchUser, UserNotPermitted
    {
        setupCheckPermissionsMocks(false);
        fixture.addRace(TEST_RACE_SERIES_ID, raceMock);
    } 

    @Test
    public void testUpdateRace() throws NoSuchUser, UserNotPermitted
    {
        setupCheckPermissionsMocks(true);
        when(raceMock.getRaceSeriesId()).thenReturn(TEST_RACE_SERIES_ID);
        fixture.updateRace(raceMock);
        verify(raceRepoMock).save(raceMock);
    }

    @Test(expected=UserNotPermitted.class)
    public void testUpdateRaceNotPermitted() throws NoSuchUser, UserNotPermitted
    {
        setupCheckPermissionsMocks(false);
        when(raceMock.getRaceSeriesId()).thenReturn(TEST_RACE_SERIES_ID);
        fixture.updateRace(raceMock);
    }

    @Test
    public void testGetRaceByID() throws NoSuchUser, UserNotPermitted
    {
        setupCheckPermissionsMocks(true);
        when(raceRepoMock.findOne(TEST_ID)).thenReturn(raceMock);
        when(raceMock.getRaceSeriesId()).thenReturn(TEST_RACE_SERIES_ID);
        assertEquals(fixture.getRaceByID(TEST_ID),raceMock);
    }

    @Test(expected=UserNotPermitted.class)
    public void testGetRaceByIDNotPermitted() throws NoSuchUser, UserNotPermitted
    {
        setupCheckPermissionsMocks(false);
        when(raceRepoMock.findOne(TEST_ID)).thenReturn(raceMock);
        when(raceMock.getRaceSeriesId()).thenReturn(TEST_RACE_SERIES_ID);
        fixture.getRaceByID(TEST_ID);
    }
    
    @Test
    public void testFindAll() throws NoSuchUser, UserNotPermitted
    {
        setupCheckPermissionsMocks(true);
        when(raceRepoMock.findByRaceSeries(TEST_RACE_SERIES_ID, pageableMock)).thenReturn(pageMock);
        assertEquals(pageMock,fixture.getAllRacesForSeries(TEST_RACE_SERIES_ID, pageableMock));
    }

    @Test(expected=UserNotPermitted.class)
    public void testFindAllNotPermitted() throws NoSuchUser, UserNotPermitted
    {
        setupCheckPermissionsMocks(false);
        when(raceRepoMock.findByRaceSeries(TEST_RACE_SERIES_ID, pageableMock)).thenReturn(pageMock);
        fixture.getAllRacesForSeries(TEST_RACE_SERIES_ID, pageableMock);
    }
    
    private void setupCheckPermissionsMocks(boolean permitted) throws NoSuchUser
    {
        when(raceSeriesAuthorizationServiceMock.isLoggedOnUserPermitted(TEST_RACE_SERIES_ID)).thenReturn(permitted);
    }

    public static final Integer TEST_ID=999;
    public static final Integer TEST_RACE_SERIES_ID=999;

    @Mock private RaceSeriesAuthorizationService    raceSeriesAuthorizationServiceMock;
    @Mock private Race raceMock;
    @Mock private RaceRespository raceRepoMock;
    @Mock private Pageable pageableMock;
    @Mock private Page<Race> pageMock;
    @InjectMocks private RaceServiceImpl fixture;
}
