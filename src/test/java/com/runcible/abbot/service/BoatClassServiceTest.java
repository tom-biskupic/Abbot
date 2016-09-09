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

import com.runcible.abbot.model.BoatClass;
import com.runcible.abbot.model.BoatDivision;
import com.runcible.abbot.model.RaceSeries;
import com.runcible.abbot.repository.BoatClassRepository;
import com.runcible.abbot.service.exceptions.DuplicateDivision;
import com.runcible.abbot.service.exceptions.NoSuchBoatClass;
import com.runcible.abbot.service.exceptions.NoSuchDivision;
import com.runcible.abbot.service.exceptions.NoSuchUser;
import com.runcible.abbot.service.exceptions.UserNotPermitted;

@RunWith(MockitoJUnitRunner.class)
public class BoatClassServiceTest
{
    @Test
    public void testGetAllBoatClassesForSeries() throws NoSuchUser, UserNotPermitted
    {
        setupCheckPermissionsMocks(true);
        when(boatClassRepoMock.findByRaceSeries(TEST_RACE_SERIES_ID, pageableMock)).thenReturn(boatClassPageMock);
        Page<BoatClass> returned = fixture.getAllBoatClassesForSeries(TEST_RACE_SERIES_ID, pageableMock);
        assertEquals(boatClassPageMock,returned);
    }
    
    @Test
    public void getAllBoatClassesForSeries2() throws NoSuchUser, UserNotPermitted
    {
        setupCheckPermissionsMocks(true);

        when(boatClassRepoMock.findByRaceSeries(TEST_RACE_SERIES_ID)).thenReturn(mockBoatList);
        List<BoatClass> returned = fixture.getAllBoatClassesForSeries(TEST_RACE_SERIES_ID);
        assertEquals(mockBoatList,returned);
    }

    @Test(expected=UserNotPermitted.class)
    public void getAllBoatClassesForSeries2UserNotPermitted() throws NoSuchUser, UserNotPermitted
    {
        setupCheckPermissionsMocks(false);

        fixture.getAllBoatClassesForSeries(TEST_ID);
    }

    @Test
    public void testAddBoatClass() throws NoSuchUser, UserNotPermitted
    {
        setupCheckPermissionsMocks(true);
        fixture.addBoatClass(TEST_RACE_SERIES_ID, boatClassMock);
        verify(boatClassRepoMock).save(boatClassMock);
    }

    @Test(expected=UserNotPermitted.class)
    public void testAddBoatClassUserNotPermitted() throws NoSuchUser, UserNotPermitted
    {
        when(raceSeriesServiceMock.findByID(TEST_ID)).thenThrow(new UserNotPermitted());
        fixture.addBoatClass(TEST_ID, boatClassMock);
    }

    @Test
    public void testUpdateBoatClass() throws NoSuchUser, UserNotPermitted
    {
        setupCheckPermissionsMocks(true);

        when(boatClassMock.getRaceSeriesId()).thenReturn(TEST_RACE_SERIES_ID);
        when(raceSeriesMock.getId()).thenReturn(TEST_ID);
        
        fixture.updateBoatClass(boatClassMock);
        verify(boatClassRepoMock).save(boatClassMock);
    }

    @Test(expected=UserNotPermitted.class)
    public void testUpdateBoatClassUserNotPermitted() throws NoSuchUser, UserNotPermitted
    {
        setupCheckPermissionsMocks(false);

        when(boatClassMock.getRaceSeriesId()).thenReturn(TEST_RACE_SERIES_ID);
        when(raceSeriesMock.getId()).thenReturn(TEST_ID);
        
        fixture.updateBoatClass(boatClassMock);
    }
    
    @Test
    public void testRemoveBoatClass() throws NoSuchUser, NoSuchBoatClass, UserNotPermitted
    {
        setupCheckPermissionsMocks(true);
        
        when(boatClassRepoMock.findOne(TEST_BOAT_CLASS_ID)).thenReturn(boatClassMock);

        when(boatClassMock.getRaceSeriesId()).thenReturn(TEST_RACE_SERIES_ID);
        when(raceSeriesMock.getId()).thenReturn(TEST_ID);
        
        fixture.removeBoatClass(TEST_BOAT_CLASS_ID);
        verify(boatClassRepoMock).delete(TEST_BOAT_CLASS_ID);
    }

    @Test(expected=NoSuchBoatClass.class)
    public void testRemoveBoatClassInvalidBoatClass() throws NoSuchUser, NoSuchBoatClass, UserNotPermitted
    {
        when(boatClassRepoMock.findOne(TEST_BOAT_CLASS_ID)).thenReturn(null);

        fixture.removeBoatClass(TEST_BOAT_CLASS_ID);
    }

    @Test(expected=UserNotPermitted.class)
    public void testRemoveBoatClassUserNotPermitted() throws NoSuchUser, NoSuchBoatClass, UserNotPermitted
    {
        setupCheckPermissionsMocks(false);
        
        when(boatClassRepoMock.findOne(TEST_BOAT_CLASS_ID)).thenReturn(boatClassMock);

        when(boatClassMock.getRaceSeriesId()).thenReturn(TEST_RACE_SERIES_ID);
        when(raceSeriesMock.getId()).thenReturn(TEST_ID);
        
        fixture.removeBoatClass(TEST_BOAT_CLASS_ID);
    }

    @Test
    public void testgetBoatClassById() throws NoSuchUser, NoSuchBoatClass, UserNotPermitted
    {
        setupCheckPermissionsMocks(true);

        when(boatClassRepoMock.findOne(TEST_BOAT_CLASS_ID)).thenReturn(boatClassMock);

        when(boatClassMock.getRaceSeriesId()).thenReturn(TEST_RACE_SERIES_ID);
        when(raceSeriesMock.getId()).thenReturn(TEST_ID);
        
        BoatClass returned = fixture.getBoatClassByID(TEST_BOAT_CLASS_ID);
        assertEquals(boatClassMock,returned);
    }

    @Test(expected=NoSuchBoatClass.class)
    public void testgetBoatClassByIdNoSuchBoatClass() throws NoSuchUser, NoSuchBoatClass, UserNotPermitted
    {
        when(boatClassRepoMock.findOne(TEST_BOAT_CLASS_ID)).thenReturn(null);

        fixture.getBoatClassByID(TEST_BOAT_CLASS_ID);
    }

    @Test(expected=UserNotPermitted.class)
    public void testgetBoatClassByIdUserNotPermitted() throws NoSuchUser, NoSuchBoatClass, UserNotPermitted
    {
        setupCheckPermissionsMocks(false);

        when(boatClassRepoMock.findOne(TEST_BOAT_CLASS_ID)).thenReturn(boatClassMock);

        when(boatClassMock.getRaceSeriesId()).thenReturn(TEST_RACE_SERIES_ID);
        when(raceSeriesMock.getId()).thenReturn(TEST_ID);
        
        fixture.getBoatClassByID(TEST_BOAT_CLASS_ID);
    }

    @Test
    public void testAddDivision() throws DuplicateDivision, NoSuchUser, UserNotPermitted, NoSuchBoatClass
    {
        setupCheckPermissionsMocks(true);

        when(boatClassMock.getRaceSeriesId()).thenReturn(TEST_RACE_SERIES_ID);
        when(raceSeriesMock.getId()).thenReturn(TEST_ID);
        
        when(boatClassRepoMock.findOne(TEST_ID)).thenReturn(boatClassMock);
        when(divisionMock.getName()).thenReturn(TEST_NAME);
        when(boatClassMock.getDivision(TEST_NAME)).thenReturn(null);
        fixture.addDivision(TEST_ID, divisionMock);
        verify(boatClassRepoMock).save(boatClassMock);
        verify(boatClassMock).addDivision(divisionMock);
    }

    @Test(expected=DuplicateDivision.class)
    public void testAddDivisionDuplicate() throws DuplicateDivision, NoSuchUser, UserNotPermitted, NoSuchBoatClass
    {
        when(boatClassRepoMock.findOne(TEST_ID)).thenReturn(boatClassMock);
        when(divisionMock.getName()).thenReturn(TEST_NAME);
        when(boatClassMock.getDivision(TEST_NAME)).thenReturn(divisionMock2);
        
        fixture.addDivision(TEST_ID, divisionMock);
    }

    @Test(expected=UserNotPermitted.class)
    public void testAddDivisionNotPermitted() throws DuplicateDivision, NoSuchUser, UserNotPermitted, NoSuchBoatClass
    {
        setupCheckPermissionsMocks(false);

        when(boatClassMock.getRaceSeriesId()).thenReturn(TEST_RACE_SERIES_ID);
        when(raceSeriesMock.getId()).thenReturn(TEST_ID);
        
        when(boatClassRepoMock.findOne(TEST_ID)).thenReturn(boatClassMock);
        fixture.addDivision(TEST_ID, divisionMock);
    }

    @Test(expected=NoSuchBoatClass.class)
    public void testAddDivisionNoSuchBoatClass() throws DuplicateDivision, NoSuchUser, UserNotPermitted, NoSuchBoatClass
    {
        when(boatClassRepoMock.findOne(TEST_ID)).thenReturn(null);
        fixture.addDivision(TEST_ID, divisionMock);
    }
    
    @Test
    public void testUpdateDivision() throws DuplicateDivision, NoSuchDivision, NoSuchBoatClass, NoSuchUser, UserNotPermitted
    {
        setupCheckPermissionsMocks(true);

        when(boatClassMock.getRaceSeriesId()).thenReturn(TEST_RACE_SERIES_ID);
        when(raceSeriesMock.getId()).thenReturn(TEST_ID);

        when(boatClassRepoMock.findOne(TEST_ID)).thenReturn(boatClassMock);
        when(divisionMock.getName()).thenReturn(TEST_NAME);
        when(boatClassMock.getDivision(TEST_NAME)).thenReturn(divisionMock2);
        when(divisionMock.getId()).thenReturn(TEST_ID);
        when(divisionMock2.getId()).thenReturn(TEST_ID);
        when(boatClassMock.getDivision(TEST_ID)).thenReturn(divisionMock2);
        
        fixture.updateDivision(TEST_ID, divisionMock);
        verify(boatClassMock).removeDivision(divisionMock2);
        verify(boatClassMock).addDivision(divisionMock);
        verify(boatClassRepoMock).save(boatClassMock);
    }

    @Test(expected=DuplicateDivision.class)
    public void testUpdateDivisionDuplicate() throws DuplicateDivision, NoSuchDivision, NoSuchBoatClass, NoSuchUser, UserNotPermitted
    {
        setupCheckPermissionsMocks(true);

        when(boatClassMock.getRaceSeriesId()).thenReturn(TEST_RACE_SERIES_ID);
        when(raceSeriesMock.getId()).thenReturn(TEST_ID);

        when(boatClassRepoMock.findOne(TEST_ID)).thenReturn(boatClassMock);
        when(divisionMock.getName()).thenReturn(TEST_NAME);
        when(boatClassMock.getDivision(TEST_NAME)).thenReturn(divisionMock2);
        when(divisionMock.getId()).thenReturn(TEST_ID);
        when(divisionMock2.getId()).thenReturn(99);
        
        fixture.updateDivision(TEST_ID, divisionMock);
    }

    @Test(expected=NoSuchDivision.class)
    public void testUpdateDivisionNoDivision() throws DuplicateDivision, NoSuchDivision, NoSuchBoatClass, NoSuchUser, UserNotPermitted
    {
        setupCheckPermissionsMocks(true);

        when(boatClassMock.getRaceSeriesId()).thenReturn(TEST_RACE_SERIES_ID);
        when(raceSeriesMock.getId()).thenReturn(TEST_ID);
        
        when(boatClassRepoMock.findOne(TEST_ID)).thenReturn(boatClassMock);
        when(divisionMock.getName()).thenReturn(TEST_NAME);
        when(boatClassMock.getDivision(TEST_NAME)).thenReturn(null);
        when(boatClassMock.getDivision(TEST_ID)).thenReturn(null);
        
        fixture.updateDivision(TEST_ID, divisionMock);
    }

    @Test(expected=UserNotPermitted.class)
    public void testUpdateDivisionNotPermitted() throws DuplicateDivision, NoSuchDivision, NoSuchBoatClass, NoSuchUser, UserNotPermitted
    {
        setupCheckPermissionsMocks(false);

        when(boatClassMock.getRaceSeriesId()).thenReturn(TEST_RACE_SERIES_ID);
        when(raceSeriesMock.getId()).thenReturn(TEST_ID);
        when(boatClassRepoMock.findOne(TEST_ID)).thenReturn(boatClassMock);
        
        fixture.updateDivision(TEST_ID, divisionMock);
    }

    @Test(expected=NoSuchBoatClass.class)
    public void testUpdateDivisionNoSuchBoatClass() throws DuplicateDivision, NoSuchDivision, NoSuchBoatClass, NoSuchUser, UserNotPermitted
    {
        when(boatClassRepoMock.findOne(TEST_ID)).thenReturn(null);
        fixture.updateDivision(TEST_ID, divisionMock);
    }

    @Test
    public void testRemoveDivision() throws NoSuchDivision, NoSuchBoatClass, NoSuchUser, UserNotPermitted
    {
        setupCheckPermissionsMocks(true);
        
        when(boatClassRepoMock.findOne(TEST_BOAT_CLASS_ID)).thenReturn(boatClassMock);
        
        when(boatClassMock.getDivision(TEST_ID)).thenReturn(divisionMock);
        when(boatClassMock.getRaceSeriesId()).thenReturn(TEST_RACE_SERIES_ID);
        when(raceSeriesMock.getId()).thenReturn(TEST_ID);
        
        fixture.removeDivision(TEST_BOAT_CLASS_ID, TEST_ID);
        verify(boatClassMock).removeDivision(divisionMock);
        verify(boatClassRepoMock).save(boatClassMock);
    }

    @Test(expected=NoSuchBoatClass.class)
    public void testRemoveDivisionNoSuchBoatClass() throws NoSuchDivision, NoSuchBoatClass, NoSuchUser, UserNotPermitted
    {
        when(boatClassRepoMock.findOne(TEST_BOAT_CLASS_ID)).thenReturn(null);
        
        fixture.removeDivision(TEST_BOAT_CLASS_ID, TEST_ID);
    }

    @Test(expected=NoSuchDivision.class)
    public void testRemoveDivisionNoSuchDivision() throws NoSuchDivision, NoSuchBoatClass, NoSuchUser, UserNotPermitted
    {
        setupCheckPermissionsMocks(true);
        
        when(boatClassRepoMock.findOne(TEST_BOAT_CLASS_ID)).thenReturn(boatClassMock);
        
        when(boatClassMock.getRaceSeriesId()).thenReturn(TEST_RACE_SERIES_ID);
        when(raceSeriesMock.getId()).thenReturn(TEST_ID);

        when(boatClassMock.getDivision(TEST_ID)).thenReturn(null);

        fixture.removeDivision(TEST_BOAT_CLASS_ID, TEST_ID);
    }

    @Test(expected=UserNotPermitted.class)
    public void testRemoveDivisionUserNotPermitted() throws NoSuchDivision, NoSuchBoatClass, NoSuchUser, UserNotPermitted
    {
        setupCheckPermissionsMocks(false);
        
        when(boatClassRepoMock.findOne(TEST_BOAT_CLASS_ID)).thenReturn(boatClassMock);
        
        when(boatClassMock.getRaceSeriesId()).thenReturn(TEST_RACE_SERIES_ID);
        when(raceSeriesMock.getId()).thenReturn(TEST_ID);

        fixture.removeDivision(TEST_BOAT_CLASS_ID, TEST_ID);
    }
    
    private void setupCheckPermissionsMocks(boolean permitted) throws NoSuchUser
    {
        when(raceSeriesAuthorizationServiceMock.isLoggedOnUserPermitted(TEST_RACE_SERIES_ID)).thenReturn(permitted);
    }

    public static final Integer TEST_ID=1234;
    public static final String  TEST_NAME="Radial";
    public static final Integer TEST_BOAT_CLASS_ID=6969;
    public static final Integer TEST_RACE_SERIES_ID=999;
    
    @Mock private Page<BoatClass>                   boatClassPageMock;
    @Mock private List<BoatClass>                   mockBoatList;
    @Mock private BoatClass                         boatClassMock;
    @Mock private BoatClassRepository               boatClassRepoMock;
    @Mock private RaceSeries                        raceSeriesMock;
    @Mock private BoatDivision                      divisionMock;
    @Mock private BoatDivision                      divisionMock2;
    @Mock private RaceSeriesAuthorizationService    raceSeriesAuthorizationServiceMock;
    @Mock private Pageable                          pageableMock;
    @Mock private RaceSeriesService                 raceSeriesServiceMock;
    
    @InjectMocks
    BoatClassServiceImpl fixture;
}
