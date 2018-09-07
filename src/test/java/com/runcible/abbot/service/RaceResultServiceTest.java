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
import org.springframework.boot.test.mock.mockito.MockReset;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.runcible.abbot.model.Boat;
import com.runcible.abbot.model.BoatClass;
import com.runcible.abbot.model.BoatDivision;
import com.runcible.abbot.model.Fleet;
import com.runcible.abbot.model.Race;
import com.runcible.abbot.model.RaceResult;
import com.runcible.abbot.model.ResultStatus;
import com.runcible.abbot.repository.RaceResultRepository;
import com.runcible.abbot.service.audit.AuditEventType;
import com.runcible.abbot.service.audit.AuditService;
import com.runcible.abbot.service.exceptions.DuplicateResult;
import com.runcible.abbot.service.exceptions.NoSuchBoat;
import com.runcible.abbot.service.exceptions.NoSuchRaceResult;
import com.runcible.abbot.service.exceptions.NoSuchUser;
import com.runcible.abbot.service.exceptions.UserNotPermitted;
import com.runcible.abbot.service.points.RaceResultPlaceUpdater;

@RunWith(MockitoJUnitRunner.class)
public class RaceResultServiceTest
{
    private static final float testYardstick = 132.0f;
    private static final float targetYardstick = 110.0f;

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
        setupGeneralRaceResultMocks();
        setupRaceMock(false);
        
        when(mockBoat.getId()).thenReturn(testBoatID);
        when(mockBoatService.getBoatByID(testBoatID)).thenReturn(mockBoat);
        
        setupCalculationMocks(ResultStatus.FINISHED,true, true);
        
        List<RaceResult> resultList = new ArrayList<RaceResult>();
        resultList.add(mockExistingRaceResult);
        setupUpdateRacePlacesMocks(resultList);
        
        fixture.addResult(testRaceID, mockRaceResult);
        verify(mockRaceResult).setRaceId(testRaceID);
        verify(mockRaceResult).setBoat(mockBoat);

        verifyUpdateRacePlaces(resultList, false);
        verify(mockRaceResultRepo).save(mockExistingRaceResult);
        
        verifyCalculations(true, false);
        
        verifyAudit(AuditEventType.CREATED);
    }

    @Test
    public void testAddResultYardstickFleet() throws NoSuchUser, UserNotPermitted, NoSuchRaceResult, NoSuchBoat, DuplicateResult
    {
        setupGeneralRaceResultMocks();
        setupYardstickMocks(false,testYardstick);
        setupRaceMock(true);
        
        when(mockBoat.getId()).thenReturn(testBoatID);
        when(mockBoatService.getBoatByID(testBoatID)).thenReturn(mockBoat);
        
        setupCalculationMocks(ResultStatus.FINISHED,true, true);
        
        List<RaceResult> resultList = new ArrayList<RaceResult>();
        resultList.add(mockExistingRaceResult);
        setupUpdateRacePlacesMocks(resultList);
        
        fixture.addResult(testRaceID, mockRaceResult);
        verify(mockRaceResult).setRaceId(testRaceID);
        verify(mockRaceResult).setBoat(mockBoat);

        verifyUpdateRacePlaces(resultList, false);
        verify(mockRaceResultRepo).save(mockExistingRaceResult);
        
        verifyCalculations(true, true);
        
        verifyAudit(AuditEventType.CREATED);
    }

    @Test
    public void testUpdateResult() throws NoSuchUser, UserNotPermitted, NoSuchRaceResult, DuplicateResult
    {
        setupGeneralRaceResultMocks();

        when(mockRaceResultRepo.findOne(testRaceResultID)).thenReturn(mockRaceResult);
        
        setupCalculationMocks(ResultStatus.FINISHED,true, true);
        setupRaceMock(false);
        
        List<RaceResult> resultList = new ArrayList<RaceResult>();
        resultList.add(mockExistingRaceResult);
        setupUpdateRacePlacesMocks(resultList);

        fixture.updateResult(mockRaceResult);
        verifyCalculations(true, false);
        verify(mockRaceResultRepo).save(mockRaceResult);
        
        verifyUpdateRacePlaces(resultList, false);
        verify(mockRaceResultRepo).save(mockExistingRaceResult);
        
        verifyAudit(AuditEventType.UPDATED);
    }

    @Test
    public void testUpdateResultDNS() throws NoSuchUser, UserNotPermitted, NoSuchRaceResult, DuplicateResult
    {
        setupGeneralRaceResultMocks();

        when(mockRaceResultRepo.findOne(testRaceResultID)).thenReturn(mockRaceResult);
        setupRaceMock(false);
        
        setupCalculationMocks(ResultStatus.DNS,false, false);

        List<RaceResult> resultList = new ArrayList<RaceResult>();
        setupUpdateRacePlacesMocks(resultList);

        fixture.updateResult(mockRaceResult);
        verifyCalculations(false, false);
        verify(mockRaceResultRepo).save(mockRaceResult);
        
        verifyUpdateRacePlaces(resultList, false);
        
        verifyAudit(AuditEventType.UPDATED);
    }

    @Test
    public void testUpdateResultDNF() throws NoSuchUser, UserNotPermitted, NoSuchRaceResult, DuplicateResult
    {
        setupGeneralRaceResultMocks();

        when(mockRaceResultRepo.findOne(testRaceResultID)).thenReturn(mockRaceResult);
        setupRaceMock(false);
        
        setupCalculationMocks(ResultStatus.DNF,true, false);
        
        List<RaceResult> resultList = new ArrayList<RaceResult>();
        setupUpdateRacePlacesMocks(resultList);

        fixture.updateResult(mockRaceResult);
        verifyCalculations(false, false);
        verify(mockRaceResultRepo).save(mockRaceResult);
        
        verifyUpdateRacePlaces(resultList, false);
        
        verifyAudit(AuditEventType.UPDATED);
    }

    @Test
    public void testRemoveResult() throws NoSuchRaceResult, NoSuchUser, UserNotPermitted, DuplicateResult
    {
        when(mockRaceResultRepo.findOne(testRaceResultID)).thenReturn(mockRaceResult);
        setupGeneralRaceResultMocks();
        setupRaceMock(false);
        
        List<RaceResult> resultList = new ArrayList<RaceResult>();
        resultList.add(mockExistingRaceResult);
        setupUpdateRacePlacesMocks(resultList);

        fixture.removeResult(testRaceResultID);
        verify(mockRaceResultRepo).delete(mockRaceResult);
        verifyUpdateRacePlaces(resultList, true);
        
        verifyAudit(AuditEventType.DELETED);
    }
    
    private void verifyCalculations(boolean calculationsExpected, boolean adjustedForYardstick)
    {
        Integer correctedTime = null;
        Integer sailingDuration = null;
        
        if ( calculationsExpected )
        {
            if ( adjustedForYardstick )
            {
                sailingDuration = (int)((float)testSailingDuration * testYardstick/targetYardstick);
            }
            else
            {
                sailingDuration = testSailingDuration;
            }
            correctedTime = sailingDuration - (int)(60.0f*testHandicap);
        }

        verify(mockRaceResult).setSailingTime(sailingDuration);
        verify(mockRaceResult).setCorrectedTime(correctedTime);
    }

    private void setupCalculationMocks(ResultStatus resultStatus, boolean hasStartTime, boolean hasFinishTime)
    {
        when(mockRaceResult.getStatus()).thenReturn(resultStatus);
        
        when(mockRaceResult.getStartTime()).thenReturn(hasStartTime ? mockStartTime : null);
        when(mockRaceResult.getFinishTime()).thenReturn(hasFinishTime ? mockFinishTime : null);
        
        if ( resultStatus.isFinished() )
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

    private void verifyAudit(AuditEventType eventType)
            throws NoSuchUser, UserNotPermitted
    {
        String text = String.format(
                "Race Result for boat %s in race name %s, fleet %s",
                testBoatName,
                testRaceName,
                testFleetName);

        verify(mockAudit).auditEventFreeForm(
                eventType, testRaceSeriesId, text);
    }

    private void setupGeneralRaceResultMocks()
    {
        when(mockRaceResult.getId()).thenReturn(testRaceResultID);
        when(mockRaceResult.getRaceId()).thenReturn(testRaceID);
        when(mockRaceResult.getBoat()).thenReturn(mockBoat);
        when(mockBoat.getName()).thenReturn(testBoatName);
    }

    private void setupYardstickMocks(boolean hasDivision,Float yardstick)
    {
        when(mockBoat.getDivision()).thenReturn(hasDivision ? mockDivision : null);
        if ( hasDivision )
        {
            when(mockDivision.getYardStick()).thenReturn(yardstick);
        }
        else
        {
            when(mockBoat.getBoatClass()).thenReturn(mockBoatClass);
            when(mockBoatClass.getYardStick()).thenReturn(yardstick);
        }
    }

    private void setupRaceMock(boolean yardstickFleet) throws NoSuchUser, UserNotPermitted
    {
        when(mockRaceService.getRaceByID(testRaceID)).thenReturn(mockRace);
        when(mockRace.getFleet()).thenReturn(mockFleet);
        when(mockFleet.getFleetName()).thenReturn(testFleetName);
        when(mockFleet.getCompeteOnYardstick()).thenReturn(yardstickFleet);
        when(mockRace.getName()).thenReturn(testRaceName);
        when(mockRace.getRaceSeriesId()).thenReturn(testRaceSeriesId);
    }

 
    private static final Integer    testRaceID = 1233;
    private static final Integer    testRaceResultID = 4556;
    private static final Integer    testBoatID = 111;
    private static final Integer    testSailingDuration = 2345;
    private static final Float      testHandicap = 5.0f;
    private static final String     testFleetName = "Lasers";
    private static final String     testRaceName = "The muppet's trophy";
    private static final String     testBoatName = "Boaty McBoatface";
    private static final Integer    testRaceSeriesId = 111;
    
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
    private @Mock Race                      mockRace;
    private @Mock AuditService              mockAudit;
    private @Mock Fleet                     mockFleet;
    private @Mock BoatClass                 mockBoatClass;
    private @Mock BoatDivision              mockDivision;
    
    @InjectMocks
    private RaceResultService fixture = new RaceResultServiceImpl();
}
