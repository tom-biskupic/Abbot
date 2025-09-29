package com.runcible.abbot.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.runcible.abbot.model.Race;
import com.runcible.abbot.model.RaceDay;
import com.runcible.abbot.repository.RaceRespository;
import com.runcible.abbot.service.audit.AuditEventType;
import com.runcible.abbot.service.audit.AuditService;
import com.runcible.abbot.service.exceptions.NoSuchUser;
import com.runcible.abbot.service.exceptions.UserNotPermitted;

@ExtendWith(MockitoExtension.class)
public class RaceServiceTest
{
    @Test
    public void testAddRace() throws NoSuchUser, UserNotPermitted
    {
        setupCheckPermissionsMocks(true);
        setupRaceMock();
        fixture.addRace(TEST_RACE_SERIES_ID, raceMock);
        verify(raceRepoMock).save(raceMock);
        verifyAuditEvent(AuditEventType.CREATED);
    }

    @Test
    public void testAddRaceNotPermitted() throws NoSuchUser, UserNotPermitted
    {
        setupCheckPermissionsMocks(false);
        Assertions.assertThrows(UserNotPermitted.class, () -> {
            fixture.addRace(TEST_RACE_SERIES_ID, raceMock);
        });
    } 

    @Test
    public void testUpdateRace() throws NoSuchUser, UserNotPermitted
    {
        setupCheckPermissionsMocks(true);
        setupRaceMock();
        fixture.updateRace(raceMock);
        verify(raceRepoMock).save(raceMock);
        verifyAuditEvent(AuditEventType.UPDATED);
    }

    @Test
    public void testUpdateRaceNotPermitted() throws NoSuchUser, UserNotPermitted
    {
        setupCheckPermissionsMocks(false);
        when(raceMock.getRaceSeriesId()).thenReturn(TEST_RACE_SERIES_ID);
        Assertions.assertThrows(UserNotPermitted.class, () -> {
            fixture.updateRace(raceMock);
        });
    }

    @Test
    public void testGetRaceByID() throws NoSuchUser, UserNotPermitted
    {
        setupCheckPermissionsMocks(true);
        when(raceRepoMock.findById(TEST_ID)).thenReturn(Optional.of(raceMock));
        when(raceMock.getRaceSeriesId()).thenReturn(TEST_RACE_SERIES_ID);
        assertEquals(fixture.getRaceByID(TEST_ID),raceMock);
    }

    @Test
    public void testGetRaceByIDNotPermitted() throws NoSuchUser, UserNotPermitted
    {
        setupCheckPermissionsMocks(false);
        when(raceRepoMock.findById(TEST_ID)).thenReturn(Optional.of(raceMock));
        when(raceMock.getRaceSeriesId()).thenReturn(TEST_RACE_SERIES_ID);
        Assertions.assertThrows(UserNotPermitted.class, () -> {
            fixture.getRaceByID(TEST_ID);
        });
    }
    
    @Test
    public void testFindAll() throws NoSuchUser, UserNotPermitted
    {
        setupCheckPermissionsMocks(true);
        when(raceRepoMock.findByRaceSeries(TEST_RACE_SERIES_ID, pageableMock)).thenReturn(pageMock);
        assertEquals(pageMock,fixture.getAllRacesForSeries(TEST_RACE_SERIES_ID, pageableMock));
    }

    @Test
    public void testFindAllNotPermitted() throws NoSuchUser, UserNotPermitted
    {
        setupCheckPermissionsMocks(false);
        Assertions.assertThrows(UserNotPermitted.class, () -> {
            fixture.getAllRacesForSeries(TEST_RACE_SERIES_ID, pageableMock);
        });
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
        testRaceList.add(new Race(TEST_RACE_SERIES_ID,date1,"Race1",null,false,null));
        testRaceList.add(new Race(TEST_RACE_SERIES_ID,date1,"Race2",null,false,null));
        testRaceList.add(new Race(TEST_RACE_SERIES_ID,date2,"Race3",null,false,null));
        
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

    private void setupRaceMock()
    {
        when(raceMock.getRaceSeriesId()).thenReturn(TEST_RACE_SERIES_ID);
        when(raceMock.getName()).thenReturn(TEST_RACE_NAME);
    } 

    private void verifyAuditEvent(AuditEventType eventType)
            throws NoSuchUser, UserNotPermitted
    {
        verify(auditMock).auditEvent(
                eventType, 
                TEST_RACE_SERIES_ID, 
                RACE_OBJECT_NAME, 
                TEST_RACE_NAME);
    }


    public static final Integer TEST_ID=999;
    public static final Integer TEST_RACE_SERIES_ID=999;

    private static final String RACE_OBJECT_NAME = "Race"; 
    private static final String TEST_RACE_NAME = "The Muppet's Trophy";
    
    @Mock private RaceSeriesAuthorizationService    raceSeriesAuthorizationServiceMock;
    @Mock private Race              raceMock;
    @Mock private RaceRespository   raceRepoMock;
    @Mock private Pageable          pageableMock;
    @Mock private Page<Race>        pageMock;
    @Mock private List<Date>        dateListMock;
    @Mock private AuditService      auditMock;
    
    @InjectMocks private RaceServiceImpl fixture;
}
