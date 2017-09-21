package com.runcible.abbot.service;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.runcible.abbot.model.Race;
import com.runcible.abbot.model.RaceDay;
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
    
    @Test
    public void testGetRaceDaysEmpty() throws NoSuchUser, UserNotPermitted
    {
        setupCheckPermissionsMocks(true);
        List<Race> testRaceList = new ArrayList<Race>();
        
        when(raceRepoMock.findRacesOrderByDate(TEST_RACE_SERIES_ID)).thenReturn(testRaceList);
        List<RaceDay> raceDays = fixture.getRaceDays(TEST_RACE_SERIES_ID);
        assertEquals(0,raceDays.size());
    }

    @Test
    public void testGetRaceDays() throws NoSuchUser, UserNotPermitted
    {
        setupCheckPermissionsMocks(true);
        //
        //	Create two races on today and one on tomorrow
        //
        Calendar cal = Calendar.getInstance();
        Date date1 = cal.getTime();
        cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH)+1);
        Date date2 = cal.getTime();
        
        List<Race> testRaceList = new ArrayList<Race>();
        testRaceList.add(new Race(TEST_RACE_SERIES_ID,date1,"Race1",null,null));
        testRaceList.add(new Race(TEST_RACE_SERIES_ID,date1,"Race2",null,null));
        testRaceList.add(new Race(TEST_RACE_SERIES_ID,date2,"Race3",null,null));
        
        when(raceRepoMock.findRacesOrderByDate(TEST_RACE_SERIES_ID)).thenReturn(testRaceList);
        List<RaceDay> raceDays = fixture.getRaceDays(TEST_RACE_SERIES_ID);
        assertEquals(2,raceDays.size());
        
        RaceDay day1 = raceDays.get(0);
        assertEquals(date1,day1.getDay());
        assertEquals(2,day1.getRaces().size());
        
        RaceDay day2 = raceDays.get(1);
        assertEquals(date2,day2.getDay());
        assertEquals(1,day2.getRaces().size());
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
    @Mock private List<Date> dateListMock;
    
    @InjectMocks private RaceServiceImpl fixture;
}
