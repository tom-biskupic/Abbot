package com.runcible.abbot.service;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.runcible.abbot.model.Boat;
import com.runcible.abbot.model.RaceResult;
import com.runcible.abbot.repository.RaceResultRepository;
import com.runcible.abbot.service.exceptions.NoSuchBoat;
import com.runcible.abbot.service.exceptions.NoSuchRaceResult;
import com.runcible.abbot.service.exceptions.NoSuchUser;
import com.runcible.abbot.service.exceptions.UserNotPermitted;

@RunWith(MockitoJUnitRunner.class)
public class RaceResultServiceTest
{
    @Test
    public void testFindAll() throws NoSuchUser, UserNotPermitted
    {
        when(mockRaceResultRepo.findRacesResults(testRaceID, mockPageable))
            .thenReturn(mockRaceResultsPage);
        
        Page<RaceResult> result = fixture.findAll(testRaceID, mockPageable);
        assertEquals(mockRaceResultsPage,result);
        
        verify(mockRaceService).getRaceByID(testRaceID);
    }
    
    @Test
    public void testGetResultByID() throws NoSuchUser, UserNotPermitted, NoSuchRaceResult
    {
        when(mockRaceResultRepo.findOne(testRaceResultID)).thenReturn(mockRaceResult);
        when(mockRaceResult.getRaceId()).thenReturn(testRaceID);
        
        RaceResult result = fixture.getResultByID(testRaceResultID);
        assertEquals(mockRaceResult,result);
        verify(mockRaceService).getRaceByID(testRaceID);
    }

    @Test
    public void testAddResult() throws NoSuchUser, UserNotPermitted, NoSuchRaceResult, NoSuchBoat
    {
        when(mockRaceResult.getBoat()).thenReturn(mockBoat);
        when(mockBoat.getId()).thenReturn(testBoatID);
        when(mockBoatService.getBoatByID(testBoatID)).thenReturn(mockBoat);
        
        setupCalculationMocks();
        
        fixture.addResult(testRaceID, mockRaceResult);
        verify(mockRaceResult).setRaceId(testRaceID);
        verify(mockRaceResult).setBoat(mockBoat);
        verifyCalculations();
    }

    @Test
    public void testUpdateResult() throws NoSuchUser, UserNotPermitted, NoSuchRaceResult
    {
        when(mockRaceResult.getId()).thenReturn(testRaceResultID);
        when(mockRaceResult.getRaceId()).thenReturn(testRaceID);
        when(mockRaceResultRepo.findOne(testRaceResultID)).thenReturn(mockRaceResult);
        
        setupCalculationMocks();
        
        fixture.updateResult(mockRaceResult);
        verifyCalculations();
        verify(mockRaceResultRepo).save(mockRaceResult);
        verify(mockRaceService).getRaceByID(testRaceID);
    }
    
    private void verifyCalculations()
    {
        verify(mockRaceResult).setSailingTime(testSailingDuration);
        verify(mockRaceResult).setCorrectedTime(testSailingDuration-(60*testHandicap));
    }

    private void setupCalculationMocks()
    {
        when(mockRaceResult.getStartTime()).thenReturn(mockStartTime);
        when(mockRaceResult.getFinishTime()).thenReturn(mockFinishTime);
        when(mockRaceResult.getHandicap()).thenReturn(testHandicap);
        when(mockTimeService.subtractTime(mockStartTime, mockFinishTime)).thenReturn(testSailingDuration);
    }
    
    @Test(expected=NoSuchRaceResult.class)
    public void testGetResultByIDNoSuchRaceResult() throws NoSuchUser, UserNotPermitted, NoSuchRaceResult
    {
        when(mockRaceResultRepo.findOne(testRaceResultID)).thenReturn(null);
        fixture.getResultByID(testRaceResultID);
    }
    
    private static final Integer testRaceID = 1233;
    private static final Integer testRaceResultID = 4556;
    private static final Integer testBoatID = 111;
    private static final Integer testSailingDuration = 2345;
    private static final Integer testHandicap = 5;
    
    private @Mock TimeService           mockTimeService;
    private @Mock RaceService           mockRaceService;
    private @Mock BoatService           mockBoatService;
    private @Mock RaceResultRepository  mockRaceResultRepo;

    private @Mock Pageable              mockPageable;
    private @Mock Page<RaceResult>      mockRaceResultsPage;
    private @Mock RaceResult            mockRaceResult;
    private @Mock Boat                  mockBoat;
    private @Mock Date                  mockStartTime;
    private @Mock Date                  mockFinishTime;
    
    @InjectMocks
    private RaceResultService fixture = new RaceResultServiceImpl();
}
