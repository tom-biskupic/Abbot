package com.runcible.abbot.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.runcible.abbot.model.Fleet;
import com.runcible.abbot.model.RaceSeries;
import com.runcible.abbot.repository.FleetRespository;
import com.runcible.abbot.service.audit.AuditEventType;
import com.runcible.abbot.service.audit.AuditService;
import com.runcible.abbot.service.exceptions.NoSuchFleet;
import com.runcible.abbot.service.exceptions.NoSuchRaceSeries;
import com.runcible.abbot.service.exceptions.NoSuchUser;
import com.runcible.abbot.service.exceptions.UserNotPermitted;

@ExtendWith(MockitoExtension.class)
public class FleetServiceTest
{
    @Test
    public void testAddFleet() throws NoSuchRaceSeries, NoSuchUser, UserNotPermitted
    {
        setupCheckPermissionsMocks(true);

        when(fleetMock.getRaceSeriesId()).thenReturn(TEST_RACE_SERIES_ID);
        when(fleetMock.getFleetName()).thenReturn(TEST_NAME);
        
        fixture.addFleet(TEST_RACE_SERIES_ID, fleetMock);
        verify(fleetMock).setRaceSeriesId(TEST_RACE_SERIES_ID);
        verify(fleetRepoMock).save(fleetMock);
        
        verifiyAudit(AuditEventType.CREATED);
    }

    @Test
    public void testAddFleetNotPermitted() throws NoSuchRaceSeries, NoSuchUser, UserNotPermitted
    {
        setupCheckPermissionsMocks(false);

        Assertions.assertThrows(UserNotPermitted.class, () -> {
            fixture.addFleet(TEST_RACE_SERIES_ID, fleetMock);
        });
    }

    @Test
    public void testGetAllFleetsForSeries() throws NoSuchUser, UserNotPermitted
    {
        setupCheckPermissionsMocks(true);

        when(fleetRepoMock.findByRaceSeries(TEST_RACE_SERIES_ID, pageableMock)).thenReturn(fleetPageMock);
        Page<Fleet> result = fixture.getAllFleetsForSeries(TEST_RACE_SERIES_ID, pageableMock);
        assertEquals(fleetPageMock,result);
    }

    @Test
    public void testGetAllFleetsForSeriesNotPermitted() throws NoSuchUser, UserNotPermitted
    {
        setupCheckPermissionsMocks(false);

        Assertions.assertThrows(UserNotPermitted.class, () -> {
            fixture.getAllFleetsForSeries(TEST_RACE_SERIES_ID, pageableMock);
        });
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
        when(fleetMock.getFleetName()).thenReturn(TEST_NAME);
        fixture.updateFleet(fleetMock);
        verify(fleetRepoMock).save(fleetMock);
        
        verifiyAudit(AuditEventType.UPDATED);
    }
    
    @Test
    public void testGetFleetById() throws NoSuchFleet, NoSuchUser, UserNotPermitted
    {
        setupCheckPermissionsMocks(true);

        when(fleetRepoMock.findById(TEST_FLEET_ID)).thenReturn(Optional.of(fleetMock));
        when(fleetMock.getRaceSeriesId()).thenReturn(TEST_RACE_SERIES_ID);
        
        Fleet result = fixture.getFleetByID(TEST_FLEET_ID);
        assertEquals(fleetMock,result);
    }

    @Test
    public void testGetFleetByIdNoSuchFleet() throws NoSuchFleet, NoSuchUser, UserNotPermitted
    {
        when(fleetRepoMock.findById(TEST_FLEET_ID)).thenReturn(Optional.empty());
        Assertions.assertThrows(NoSuchFleet.class, () -> {
            fixture.getFleetByID(TEST_FLEET_ID);
        });
    }

    @Test
    public void testGetFleetByIdUserNotPermitted() throws NoSuchFleet, NoSuchUser, UserNotPermitted
    {
        setupCheckPermissionsMocks(false);

        when(fleetRepoMock.findById(TEST_FLEET_ID)).thenReturn(Optional.of(fleetMock));
        when(fleetMock.getRaceSeriesId()).thenReturn(TEST_RACE_SERIES_ID);
        Assertions.assertThrows(UserNotPermitted.class, () -> {
            fixture.getFleetByID(TEST_FLEET_ID);
        });
    }

    @Test
    public void testRemoveFleet() throws NoSuchFleet, NoSuchUser, UserNotPermitted
    {
        setupCheckPermissionsMocks(true);

        when(fleetRepoMock.findById(TEST_FLEET_ID)).thenReturn(Optional.of(fleetMock));
        when(fleetMock.getRaceSeriesId()).thenReturn(TEST_RACE_SERIES_ID);
        when(fleetMock.getFleetName()).thenReturn(TEST_NAME);
        
        fixture.removeFleet(TEST_FLEET_ID);
        verify(fleetRepoMock).deleteById(TEST_FLEET_ID);
        
        verifiyAudit(AuditEventType.DELETED);
    }

    @Test
    public void testRemoveFleetNoSuchFleet() throws NoSuchFleet, NoSuchUser, UserNotPermitted
    {
        when(fleetRepoMock.findById(TEST_FLEET_ID)).thenReturn(Optional.empty());
        Assertions.assertThrows(NoSuchFleet.class, () -> {
            fixture.removeFleet(TEST_FLEET_ID);
        });
    }

    @Test
    public void testRemoveFleetUserNotPermitted() throws NoSuchFleet, NoSuchUser, UserNotPermitted
    {
        setupCheckPermissionsMocks(false);

        when(fleetRepoMock.findById(TEST_FLEET_ID)).thenReturn(Optional.of(fleetMock));
        when(fleetMock.getRaceSeriesId()).thenReturn(TEST_RACE_SERIES_ID);
        Assertions.assertThrows(UserNotPermitted.class, () -> {
            fixture.removeFleet(TEST_FLEET_ID);
        });
    }
    
    private void setupCheckPermissionsMocks(boolean permitted) throws NoSuchUser
    {
        when(raceSeriesAuthorizationServiceMock.isLoggedOnUserPermitted(TEST_RACE_SERIES_ID)).thenReturn(permitted);
    }

    private void verifiyAudit( AuditEventType eventType ) throws NoSuchUser, UserNotPermitted
    {
        verify(auditMock).auditEvent(eventType, TEST_RACE_SERIES_ID, FLEET_OBJECT_NAME, TEST_NAME);
    }
    
    public static final Integer TEST_ID=1234;
    public static final Integer TEST_FLEET_ID=3456;
    public static final String  TEST_NAME="Radial";
    public static final Integer TEST_RACE_SERIES_ID=9999;
    public static final String  FLEET_OBJECT_NAME="Fleet";
    
    @Mock private FleetRespository      fleetRepoMock;
    @Mock private RaceSeriesAuthorizationService raceSeriesAuthorizationServiceMock;
    @Mock private Fleet                 fleetMock;
    @Mock private Page<Fleet>           fleetPageMock;
    @Mock private Pageable              pageableMock;
    @Mock private RaceSeriesService     raceSeriesServiceMock;
    @Mock private RaceSeries            raceSeriesMock;
    @Mock private List<Fleet>           fleetListMock;
    @Mock private AuditService          auditMock;
    
    @InjectMocks
    FleetServiceImpl fixture;
}
