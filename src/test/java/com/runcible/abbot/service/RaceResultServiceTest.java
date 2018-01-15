package com.runcible.abbot.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import com.runcible.abbot.service.exceptions.DuplicateResult;
import com.runcible.abbot.service.exceptions.NoSuchBoat;
import com.runcible.abbot.service.exceptions.NoSuchRaceResult;
import com.runcible.abbot.service.exceptions.NoSuchUser;
import com.runcible.abbot.service.exceptions.UserNotPermitted;
import com.runcible.abbot.service.points.RaceResultPlaceUpdater;

@RunWith(MockitoJUnitRunner.class)
public class RaceResultServiceTest
{
    enum TimeValues { NO_START_TIME, NO_FINISH_TIME, NO_EITHER_TIME, BOTH_TIMES };
    
    @Test
    public void testFindAll() throws NoSuchUser, UserNotPermitted
    {
        when(mockRaceResultRepo.findRaceResults(testRaceID, mockPageable))
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
    public void testAddResult() throws NoSuchUser, UserNotPermitted, NoSuchRaceResult, NoSuchBoat, DuplicateResult
    {
        when(mockRaceResult.getBoat()).thenReturn(mockBoat);
        when(mockBoat.getId()).thenReturn(testBoatID);
        when(mockBoatService.getBoatByID(testBoatID)).thenReturn(mockBoat);
        
        setupCalculationMocks(true, true);
        
        List<RaceResult> resultList = new ArrayList<RaceResult>();
        resultList.add(mockExistingRaceResult);
        setupUpdateRacePlacesMocks(resultList);
        
        fixture.addResult(testRaceID, mockRaceResult);
        verify(mockRaceResult).setRaceId(testRaceID);
        verify(mockRaceResult).setBoat(mockBoat);

        verifyUpdateRacePlaces(resultList, false);
        verify(mockRaceResultRepo).save(mockExistingRaceResult);
        
        verifyCalculations(true);
    }

    @Test
    public void testUpdateResult() throws NoSuchUser, UserNotPermitted, NoSuchRaceResult, DuplicateResult
    {
        when(mockRaceResult.getId()).thenReturn(testRaceResultID);
        when(mockRaceResult.getRaceId()).thenReturn(testRaceID);
        when(mockRaceResultRepo.findOne(testRaceResultID)).thenReturn(mockRaceResult);
        
        setupCalculationMocks(true, true);
        
        List<RaceResult> resultList = new ArrayList<RaceResult>();
        resultList.add(mockExistingRaceResult);
        setupUpdateRacePlacesMocks(resultList);

        fixture.updateResult(mockRaceResult);
        verifyCalculations(true);
        verify(mockRaceResultRepo).save(mockRaceResult);
        verify(mockRaceService).getRaceByID(testRaceID);
        
        verifyUpdateRacePlaces(resultList, false);
        verify(mockRaceResultRepo).save(mockExistingRaceResult);
    }

    @Test
    public void testUpdateResultDNS() throws NoSuchUser, UserNotPermitted, NoSuchRaceResult, DuplicateResult
    {
        when(mockRaceResult.getId()).thenReturn(testRaceResultID);
        when(mockRaceResult.getRaceId()).thenReturn(testRaceID);
        when(mockRaceResultRepo.findOne(testRaceResultID)).thenReturn(mockRaceResult);
        
        setupCalculationMocks(false, false);

        List<RaceResult> resultList = new ArrayList<RaceResult>();
        setupUpdateRacePlacesMocks(resultList);

        fixture.updateResult(mockRaceResult);
        verifyCalculations(false);
        verify(mockRaceResultRepo).save(mockRaceResult);
        verify(mockRaceService).getRaceByID(testRaceID);
        
        verifyUpdateRacePlaces(resultList, false);
    }

    @Test
    public void testUpdateResultDNF() throws NoSuchUser, UserNotPermitted, NoSuchRaceResult, DuplicateResult
    {
        when(mockRaceResult.getId()).thenReturn(testRaceResultID);
        when(mockRaceResult.getRaceId()).thenReturn(testRaceID);
        when(mockRaceResultRepo.findOne(testRaceResultID)).thenReturn(mockRaceResult);
        
        setupCalculationMocks(true, false);
        
        List<RaceResult> resultList = new ArrayList<RaceResult>();
        setupUpdateRacePlacesMocks(resultList);

        fixture.updateResult(mockRaceResult);
        verifyCalculations(false);
        verify(mockRaceResultRepo).save(mockRaceResult);
        verify(mockRaceService).getRaceByID(testRaceID);
        
        verifyUpdateRacePlaces(resultList, false);
    }

    @Test
    public void testRemoveResult() throws NoSuchRaceResult, NoSuchUser, UserNotPermitted, DuplicateResult
    {
        when(mockRaceResultRepo.findOne(testRaceResultID)).thenReturn(mockRaceResult);
        when(mockRaceResult.getRaceId()).thenReturn(testRaceID);
        
        List<RaceResult> resultList = new ArrayList<RaceResult>();
        resultList.add(mockExistingRaceResult);
        setupUpdateRacePlacesMocks(resultList);

        fixture.removeResult(testRaceResultID);
        verify(mockRaceResultRepo).delete(mockRaceResult);
        verify(mockRaceService).getRaceByID(testRaceID);
        verifyUpdateRacePlaces(resultList, true);
    }
    
    private void verifyCalculations(boolean calculationsExpected)
    {
        verify(mockRaceResult).setSailingTime(calculationsExpected ? testSailingDuration : null);
        verify(mockRaceResult).setCorrectedTime(
                calculationsExpected ? (testSailingDuration-(60*testHandicap)) : null);
    }

    private void setupCalculationMocks(boolean hasStartTime, boolean hasFinishTime)
    {
        when(mockRaceResult.getStartTime()).thenReturn(hasStartTime ? mockStartTime : null);
        when(mockRaceResult.getFinishTime()).thenReturn(hasFinishTime ? mockFinishTime : null);
        
        if ( hasStartTime && hasFinishTime )
        {
            when(mockRaceResult.getHandicap()).thenReturn(testHandicap);
            when(mockTimeService.subtractTime(mockStartTime, mockFinishTime)).thenReturn(testSailingDuration);
        }
    }
    
    @Test(expected=NoSuchRaceResult.class)
    public void testGetResultByIDNoSuchRaceResult() throws NoSuchUser, UserNotPermitted, NoSuchRaceResult
    {
        when(mockRaceResultRepo.findOne(testRaceResultID)).thenReturn(null);
        fixture.getResultByID(testRaceResultID);
    }

    private void setupUpdateRacePlacesMocks(List<RaceResult> testResultList)
    {
        when(mockRaceResultRepo.findRaceResults(testRaceID)).thenReturn(testResultList);
    }

    private void verifyUpdateRacePlaces(List<RaceResult> testResultList, boolean deletedResult) throws DuplicateResult
    {
       verify(mockRaceResultPlaceUpdater).updateResultPlaces(
               deletedResult ? null : mockRaceResult, testResultList);
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

    private @Mock Pageable                  mockPageable;
    private @Mock Page<RaceResult>          mockRaceResultsPage;
    private @Mock RaceResult                mockRaceResult;
    private @Mock RaceResult                mockExistingRaceResult;
    private @Mock Boat                      mockBoat;
    private @Mock Date                      mockStartTime;
    private @Mock Date                      mockFinishTime;
    private @Mock RaceResultPlaceUpdater    mockRaceResultPlaceUpdater;
    
    @InjectMocks
    private RaceResultService fixture = new RaceResultServiceImpl();
}
