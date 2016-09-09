package com.runcible.abbot.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.runcible.abbot.model.Fleet;
import com.runcible.abbot.model.RaceSeries;
import com.runcible.abbot.repository.FleetRespository;
import com.runcible.abbot.service.exceptions.NoSuchFleet;
import com.runcible.abbot.service.exceptions.NoSuchRaceSeries;
import com.runcible.abbot.service.exceptions.NoSuchUser;
import com.runcible.abbot.service.exceptions.UserNotPermitted;

@RunWith(MockitoJUnitRunner.class)
public class FleetServiceTest
{
    @Test
    public void testAddFleet() throws NoSuchRaceSeries, NoSuchUser, UserNotPermitted
    {
        setupCheckPermissionsMocks(true);

        when(fleetMock.getRaceSeriesId()).thenReturn(TEST_RACE_SERIES_ID);

        fixture.addFleet(TEST_RACE_SERIES_ID, fleetMock);
        verify(fleetMock).setRaceSeriesId(TEST_RACE_SERIES_ID);
        verify(fleetRepoMock).save(fleetMock);
    }

    @Test(expected=UserNotPermitted.class)
    public void testAddFleetNotPermitted() throws NoSuchRaceSeries, NoSuchUser, UserNotPermitted
    {
        setupCheckPermissionsMocks(false);

        when(fleetMock.getRaceSeriesId()).thenReturn(TEST_RACE_SERIES_ID);

        fixture.addFleet(TEST_RACE_SERIES_ID, fleetMock);
    }

    @Test
    public void testGetAllFleetsForSeries() throws NoSuchUser, UserNotPermitted
    {
        setupCheckPermissionsMocks(true);

        when(fleetRepoMock.findByRaceSeries(TEST_RACE_SERIES_ID, pageableMock)).thenReturn(fleetPageMock);
        Page<Fleet> result = fixture.getAllFleetsForSeries(TEST_RACE_SERIES_ID, pageableMock);
        assertEquals(fleetPageMock,result);
    }

    @Test(expected=UserNotPermitted.class)
    public void testGetAllFleetsForSeriesNotPermitted() throws NoSuchUser, UserNotPermitted
    {
        setupCheckPermissionsMocks(false);

        when(fleetRepoMock.findByRaceSeries(TEST_RACE_SERIES_ID, pageableMock)).thenReturn(fleetPageMock);
        fixture.getAllFleetsForSeries(TEST_RACE_SERIES_ID, pageableMock);
    }
    
    @Test
    public void testGetAllFleetsForSeries2() throws NoSuchUser, UserNotPermitted
    {
        setupCheckPermissionsMocks(true);
        when(fleetRepoMock.findByRaceSeries(TEST_RACE_SERIES_ID)).thenReturn(fleetListMock);
        List<Fleet> returned = fixture.getAllFleetsForSeries(TEST_RACE_SERIES_ID);
        assertEquals(fleetListMock,returned);
    }
    
    @Test
    public void testUpdateFleet() throws NoSuchUser, UserNotPermitted
    {
        setupCheckPermissionsMocks(true);

        when(fleetMock.getRaceSeriesId()).thenReturn(TEST_RACE_SERIES_ID);
        fixture.updateFleet(fleetMock);
        verify(fleetRepoMock).save(fleetMock);
    }
    
    @Test
    public void testGetFleetById() throws NoSuchFleet, NoSuchUser, UserNotPermitted
    {
        setupCheckPermissionsMocks(true);

        when(fleetRepoMock.findOne(TEST_FLEET_ID)).thenReturn(fleetMock);
        when(fleetMock.getRaceSeriesId()).thenReturn(TEST_RACE_SERIES_ID);
        
        Fleet result = fixture.getFleetByID(TEST_FLEET_ID);
        assertEquals(fleetMock,result);
    }

    @Test(expected=NoSuchFleet.class)
    public void testGetFleetByIdNoSuchFleet() throws NoSuchFleet, NoSuchUser, UserNotPermitted
    {
        setupCheckPermissionsMocks(true);

        when(fleetRepoMock.findOne(TEST_FLEET_ID)).thenReturn(null);
        fixture.getFleetByID(TEST_FLEET_ID);
    }

    @Test(expected=UserNotPermitted.class)
    public void testGetFleetByIdUserNotPermitted() throws NoSuchFleet, NoSuchUser, UserNotPermitted
    {
        setupCheckPermissionsMocks(false);

        when(fleetRepoMock.findOne(TEST_FLEET_ID)).thenReturn(fleetMock);
        when(fleetMock.getRaceSeriesId()).thenReturn(TEST_RACE_SERIES_ID);
        
        fixture.getFleetByID(TEST_FLEET_ID);
    }

    @Test
    public void testRemoveFleet() throws NoSuchFleet, NoSuchUser, UserNotPermitted
    {
        setupCheckPermissionsMocks(true);

        when(fleetRepoMock.findOne(TEST_FLEET_ID)).thenReturn(fleetMock);
        when(fleetMock.getRaceSeriesId()).thenReturn(TEST_RACE_SERIES_ID);
        
        fixture.removeFleet(TEST_FLEET_ID);
        verify(fleetRepoMock).delete(TEST_FLEET_ID);
    }

    @Test(expected=NoSuchFleet.class)
    public void testRemoveFleetNoSuchFleet() throws NoSuchFleet, NoSuchUser, UserNotPermitted
    {
        setupCheckPermissionsMocks(true);

        when(fleetRepoMock.findOne(TEST_FLEET_ID)).thenReturn(null);
        fixture.removeFleet(TEST_FLEET_ID);
    }

    @Test(expected=UserNotPermitted.class)
    public void testRemoveFleetUserNotPermitted() throws NoSuchFleet, NoSuchUser, UserNotPermitted
    {
        setupCheckPermissionsMocks(false);

        when(fleetRepoMock.findOne(TEST_FLEET_ID)).thenReturn(fleetMock);
        when(fleetMock.getRaceSeriesId()).thenReturn(TEST_RACE_SERIES_ID);
        
        fixture.removeFleet(TEST_FLEET_ID);
    }
    
    private void setupCheckPermissionsMocks(boolean permitted) throws NoSuchUser
    {
        when(raceSeriesAuthorizationServiceMock.isLoggedOnUserPermitted(TEST_RACE_SERIES_ID)).thenReturn(permitted);
    }

    public static final Integer TEST_ID=1234;
    public static final Integer TEST_FLEET_ID=3456;
    public static final String  TEST_NAME="Radial";
    public static final Integer TEST_RACE_SERIES_ID=9999;
    
    @Mock private FleetRespository      fleetRepoMock;
    @Mock private RaceSeriesAuthorizationService raceSeriesAuthorizationServiceMock;
    @Mock private Fleet                 fleetMock;
    @Mock private Page<Fleet>           fleetPageMock;
    @Mock private Pageable              pageableMock;
    @Mock private RaceSeriesService     raceSeriesServiceMock;
    @Mock private RaceSeries            raceSeriesMock;
    @Mock private List<Fleet>           fleetListMock;
    
    @InjectMocks
    FleetServiceImpl fixture;
}
