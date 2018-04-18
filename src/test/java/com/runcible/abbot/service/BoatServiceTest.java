package com.runcible.abbot.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.runcible.abbot.model.Boat;
import com.runcible.abbot.model.BoatClass;
import com.runcible.abbot.model.BoatDivision;
import com.runcible.abbot.model.Fleet;
import com.runcible.abbot.model.FleetSelector;
import com.runcible.abbot.repository.BoatRepository;
import com.runcible.abbot.service.audit.AuditEventType;
import com.runcible.abbot.service.audit.AuditService;
import com.runcible.abbot.service.exceptions.NoSuchBoat;
import com.runcible.abbot.service.exceptions.NoSuchCompetition;
import com.runcible.abbot.service.exceptions.NoSuchFleet;
import com.runcible.abbot.service.exceptions.NoSuchRaceSeries;
import com.runcible.abbot.service.exceptions.NoSuchUser;
import com.runcible.abbot.service.exceptions.UserNotPermitted;

@RunWith(MockitoJUnitRunner.class)
public class BoatServiceTest 
{

	@Test
    public void testGetAllBoatsInFleet() throws NoSuchUser, UserNotPermitted, NoSuchFleet
    {
    	setupCheckPermissionsMocks(true);
    	
    	Set<FleetSelector> fleetSelectorList = new HashSet<FleetSelector>();
    	fleetSelectorList.add(fleetSelectorMock);
    	List<Boat> boatListMock = new ArrayList<Boat>();
    	boatListMock.add(boatMock);

    	when(fleetServiceMock.getFleetByID(TEST_FLEET_ID)).thenReturn(fleetMock);
    	when(fleetMock.getFleetClasses()).thenReturn(fleetSelectorList);
    	when(fleetSelectorMock.getBoatDivision()).thenReturn(boatDivisionMock);
    	when(boatDivisionMock.getId()).thenReturn(TEST_BOAT_DIVISION_ID);
    	when(fleetSelectorMock.getBoatClass()).thenReturn(boatClassMock);
    	when(boatClassMock.getId()).thenReturn(TEST_BOAT_CLASS_ID);
    	when(boatRepoMock.findBoatByClassAndDivision(TEST_RACE_SERIES_ID, TEST_BOAT_CLASS_ID, TEST_BOAT_DIVISION_ID)).thenReturn(boatListMock);
    	
    	List<Boat> returned = fixture.getAllBoatsInFleetForSeries(TEST_RACE_SERIES_ID, TEST_FLEET_ID);
    	assertEquals(boatListMock,returned);
    }
    
	@Test
    public void testGetAllBoatsInFleetWithMultipleFleetSelectors() throws NoSuchUser, UserNotPermitted, NoSuchFleet
    {
    	setupCheckPermissionsMocks(true);
    	
    	Set<FleetSelector> fleetSelectorList = new HashSet<FleetSelector>();
    	fleetSelectorList.add(fleetSelectorMock);
    	fleetSelectorList.add(fleetSelectorMock2);
    	List<Boat> boatListMock = new ArrayList<Boat>();
    	boatListMock.add(boatMock);
    	
    	when(fleetServiceMock.getFleetByID(TEST_FLEET_ID)).thenReturn(fleetMock);
    	when(fleetMock.getFleetClasses()).thenReturn(fleetSelectorList);
    	
    	when(fleetSelectorMock.getBoatDivision()).thenReturn(boatDivisionMock);
    	when(fleetSelectorMock2.getBoatDivision()).thenReturn(boatDivisionMock2);
    	
    	when(boatDivisionMock.getId()).thenReturn(TEST_BOAT_DIVISION_ID);
    	when(boatDivisionMock2.getId()).thenReturn(TEST_BOAT_DIVISION_ID2);
    	
    	when(fleetSelectorMock.getBoatClass()).thenReturn(boatClassMock);
    	when(fleetSelectorMock2.getBoatClass()).thenReturn(boatClassMock2);
    	
    	when(boatClassMock.getId()).thenReturn(TEST_BOAT_CLASS_ID);
    	when(boatClassMock2.getId()).thenReturn(TEST_BOAT_CLASS_ID2);
    	
    	when(boatRepoMock.findBoatByClassAndDivision(TEST_RACE_SERIES_ID, TEST_BOAT_CLASS_ID, TEST_BOAT_DIVISION_ID)).thenReturn(boatListMock);
    	when(boatRepoMock.findBoatByClassAndDivision(TEST_RACE_SERIES_ID, TEST_BOAT_CLASS_ID2, TEST_BOAT_DIVISION_ID2)).thenReturn(boatListMock);
    	
    	List<Boat> returned = fixture.getAllBoatsInFleetForSeries(TEST_RACE_SERIES_ID, TEST_FLEET_ID);
    	List<Boat> expected = new ArrayList<Boat>();
    	expected.addAll(boatListMock);
    	expected.addAll(boatListMock);
    	assertEquals(expected,returned);
    }
	
	@Test
    public void testGetAllBoatsInFleetSelectorWithNoDivision() throws NoSuchUser, UserNotPermitted, NoSuchFleet
    {
    	setupCheckPermissionsMocks(true);
    	
    	Set<FleetSelector> fleetSelectorList = new HashSet<FleetSelector>();
    	fleetSelectorList.add(fleetSelectorMock);
    	List<Boat> boatListMock = new ArrayList<Boat>();
    	boatListMock.add(boatMock);

    	when(fleetServiceMock.getFleetByID(TEST_FLEET_ID)).thenReturn(fleetMock);
    	when(fleetMock.getFleetClasses()).thenReturn(fleetSelectorList);
    	when(fleetSelectorMock.getBoatDivision()).thenReturn(null);
    	when(fleetSelectorMock.getBoatClass()).thenReturn(boatClassMock);
    	when(boatClassMock.getId()).thenReturn(TEST_BOAT_CLASS_ID);
    	when(boatRepoMock.findBoatByClass(TEST_RACE_SERIES_ID, TEST_BOAT_CLASS_ID)).thenReturn(boatListMock);
    	
    	List<Boat> returned = fixture.getAllBoatsInFleetForSeries(TEST_RACE_SERIES_ID, TEST_FLEET_ID);
    	assertEquals(boatListMock,returned);
    }

    @Test(expected=UserNotPermitted.class)
    public void testGetAllBoatsInFleetUserNotPermitted() throws NoSuchUser, UserNotPermitted, NoSuchFleet
    {
        setupCheckPermissionsMocks(false);

        fixture.getAllBoatsInFleetForSeries(TEST_RACE_SERIES_ID, TEST_FLEET_ID);
    }

    @Test
    public void testGetAllBoatsInSeries() throws NoSuchUser, UserNotPermitted
    {
    	setupCheckPermissionsMocks(true);
    	when(boatRepoMock.findByRaceSeries(TEST_RACE_SERIES_ID, pageableMock)).thenReturn(boatPageMock);
    	Page<Boat> boats = fixture.getAllBoatsForSeries(TEST_RACE_SERIES_ID, pageableMock);
    	assertEquals(boatPageMock,boats);
    }

    @Test(expected=UserNotPermitted.class)
    public void testGetAllBoatsInSeriesUserNotPermitted() throws NoSuchUser, UserNotPermitted
    {
    	setupCheckPermissionsMocks(false);
    	fixture.getAllBoatsForSeries(TEST_RACE_SERIES_ID, pageableMock);
    }

    @Test
    public void testUpdateBoat() throws NoSuchUser, UserNotPermitted
    {
    	setupCheckPermissionsMocks(true);
    	when(boatMock.getRaceSeriesID()).thenReturn(TEST_RACE_SERIES_ID);
    	when(boatMock.getName()).thenReturn(TEST_BOAT_NAME);
    	
    	fixture.updateBoat(boatMock);
    	
    	verify(boatRepoMock).save(boatMock);
    	verifyAuditMock(AuditEventType.UPDATED);
    }

    @Test(expected=UserNotPermitted.class)
    public void testUpdateBoatUserNotPermitted() throws NoSuchUser, UserNotPermitted
    {
    	setupCheckPermissionsMocks(false);
    	fixture.updateBoat(boatMock);
    }

    @Test
    public void testAddBoat() throws NoSuchRaceSeries, NoSuchUser, UserNotPermitted
    {
    	setupCheckPermissionsMocks(true);
    	when(boatMock.getName()).thenReturn(TEST_BOAT_NAME);
    	when(boatMock.getRaceSeriesID()).thenReturn(TEST_RACE_SERIES_ID);
    	fixture.addBoat(TEST_RACE_SERIES_ID, boatMock);
    	verify(boatRepoMock).save(boatMock);
    	verify(boatMock).setRaceSeriesID(TEST_RACE_SERIES_ID);
    	verifyAuditMock(AuditEventType.CREATED);
    }

    @Test(expected=UserNotPermitted.class)
    public void testAddBoatUserNotPermitted() throws NoSuchRaceSeries, NoSuchUser, UserNotPermitted
    {
    	setupCheckPermissionsMocks(false);
    	fixture.addBoat(TEST_RACE_SERIES_ID, boatMock);
    }

    @Test
    public void testGetBoatById() throws NoSuchBoat, NoSuchUser, UserNotPermitted
    {
    	setupCheckPermissionsMocks(true);

    	when(boatRepoMock.findOne(TEST_BOAT_ID)).thenReturn(boatMock);
    	when(boatMock.getRaceSeriesID()).thenReturn(TEST_RACE_SERIES_ID);
    	Boat result = fixture.getBoatByID(TEST_BOAT_ID);
    	assertEquals(boatMock,result);
    }

    @Test(expected=NoSuchBoat.class)
    public void testGetBoatByIdNotFound() throws NoSuchBoat, NoSuchUser, UserNotPermitted
    {
    	setupCheckPermissionsMocks(true);

    	when(boatRepoMock.findOne(TEST_BOAT_ID)).thenReturn(null);
    	fixture.getBoatByID(TEST_BOAT_ID);
    }

    @Test(expected=UserNotPermitted.class)
    public void testGetBoatByIdUserNotPermitted() throws NoSuchBoat, NoSuchUser, UserNotPermitted
    {
    	setupCheckPermissionsMocks(false);

    	when(boatRepoMock.findOne(TEST_BOAT_ID)).thenReturn(boatMock);
    	when(boatMock.getRaceSeriesID()).thenReturn(TEST_RACE_SERIES_ID);
    	fixture.getBoatByID(TEST_BOAT_ID);
    }

    @Test
    public void testRemoveBoat() throws NoSuchUser, NoSuchCompetition, UserNotPermitted, NoSuchBoat
    {
    	setupCheckPermissionsMocks(true);
    	when(boatRepoMock.findOne(TEST_BOAT_ID)).thenReturn(boatMock);
    	when(boatMock.getRaceSeriesID()).thenReturn(TEST_RACE_SERIES_ID);
    	when(boatMock.getName()).thenReturn(TEST_BOAT_NAME);
    	
    	fixture.removeBoat(TEST_BOAT_ID);
    	verify(boatRepoMock).delete(boatMock);
    	verifyAuditMock(AuditEventType.DELETED);
    }

    @Test(expected=UserNotPermitted.class)
    public void testRemoveBoatUserNotPermitted() throws NoSuchUser, NoSuchCompetition, UserNotPermitted, NoSuchBoat
    {
    	setupCheckPermissionsMocks(false);
    	when(boatRepoMock.findOne(TEST_BOAT_ID)).thenReturn(boatMock);
    	when(boatMock.getRaceSeriesID()).thenReturn(TEST_RACE_SERIES_ID);
    	
    	fixture.removeBoat(TEST_BOAT_ID);
    }

    @Test(expected=NoSuchBoat.class)
    public void testRemoveBoatNoBoat() throws NoSuchUser, NoSuchCompetition, UserNotPermitted, NoSuchBoat
    {
    	setupCheckPermissionsMocks(true);
    	when(boatRepoMock.findOne(TEST_BOAT_ID)).thenReturn(null);
    	
    	fixture.removeBoat(TEST_BOAT_ID);
    }

    private void setupCheckPermissionsMocks(boolean permitted) throws NoSuchUser
    {
        when(raceSeriesAuthorizationServiceMock.isLoggedOnUserPermitted(TEST_RACE_SERIES_ID)).thenReturn(permitted);
    }

    private void verifyAuditMock(AuditEventType eventType)
            throws NoSuchUser, UserNotPermitted
    {
        verify(auditServiceMock).auditEvent(
                eventType, 
                TEST_RACE_SERIES_ID, 
                BOAT_OBJECT_NAME, 
                TEST_BOAT_NAME);
    }

    private static Integer TEST_RACE_SERIES_ID=1;
    private static Integer TEST_FLEET_ID=2;
    private static Integer TEST_BOAT_DIVISION_ID=3;
    private static Integer TEST_BOAT_CLASS_ID=4;
    private static Integer TEST_BOAT_DIVISION_ID2=5;
    private static Integer TEST_BOAT_CLASS_ID2=6;
    private static Integer TEST_BOAT_ID=7;

    private static final String BOAT_OBJECT_NAME = "Boat"; 
    private static final String TEST_BOAT_NAME = "Goats afloat";
    
    @Mock private Fleet 			fleetMock;
    @Mock private BoatDivision 		boatDivisionMock;
    @Mock private BoatDivision 		boatDivisionMock2;
    @Mock private FleetSelector		fleetSelectorMock;
    @Mock private FleetSelector		fleetSelectorMock2;
    @Mock private BoatClass			boatClassMock;
    @Mock private BoatClass			boatClassMock2;
    @Mock private Boat				boatMock;
    @Mock private Pageable			pageableMock;
    @Mock private Page<Boat>		boatPageMock;
    
    @Mock private BoatRepository 	boatRepoMock;
    @Mock private FleetService		fleetServiceMock;
    @Mock private RaceSeriesAuthorizationService    raceSeriesAuthorizationServiceMock;
    @Mock private AuditService      auditServiceMock;
    
    @InjectMocks
    BoatServiceImpl fixture;
}
