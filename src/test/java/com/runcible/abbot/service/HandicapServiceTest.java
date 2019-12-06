package com.runcible.abbot.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.runcible.abbot.model.Boat;
import com.runcible.abbot.model.Fleet;
import com.runcible.abbot.model.Handicap;
import com.runcible.abbot.model.HandicapLimit;
import com.runcible.abbot.model.Race;
import com.runcible.abbot.model.RaceResult;
import com.runcible.abbot.model.ResultStatus;
import com.runcible.abbot.repository.HandicapLimitsRepository;
import com.runcible.abbot.repository.HandicapRepository;
import com.runcible.abbot.service.audit.AuditEventType;
import com.runcible.abbot.service.audit.AuditService;
import com.runcible.abbot.service.exceptions.DuplicateResult;
import com.runcible.abbot.service.exceptions.NoSuchFleet;
import com.runcible.abbot.service.exceptions.NoSuchRaceResult;
import com.runcible.abbot.service.exceptions.NoSuchUser;
import com.runcible.abbot.service.exceptions.UserNotPermitted;

@RunWith(MockitoJUnitRunner.class)
public class HandicapServiceTest
{
    private class RaceResultTest
    {
        public RaceResultTest(
                float           initialHandicap,
                ResultStatus    resultStatus,
                Integer         sailingTime,
                float           expectedHandicapUpdate,
                Integer         winsCount)
        {
            this.initialHandicap = initialHandicap;
            this.resultStatus = resultStatus; 
            this.sailingTime = sailingTime;
            this.expectedHandicapUpdate = expectedHandicapUpdate;
            this.winsCount = winsCount;
        }
        
        public float        initialHandicap;
        public ResultStatus resultStatus;
        public Integer      sailingTime;
        public float        expectedHandicapUpdate;
        public Integer      winsCount;
    }
    
    @Before
    public void setUp()
    {
        testBoatList.add(mockBoat);
    }
    
    @Test
    public void testGetHandicapsForFleet() throws NoSuchUser, UserNotPermitted, NoSuchFleet
    {
        setupGetHandicapsForFleetMock();
        
        List<Handicap> handicaps = fixture.getHandicapsForFleet(testRaceSeriesID, testFleetID, testRaceID);
        assertEquals(1,handicaps.size());
        assertEquals(mockHandicap,handicaps.get(0));
    }

    private void setupGetHandicapsForFleetMock()
            throws NoSuchUser, UserNotPermitted, NoSuchFleet
    {
        when(mockBoatService.getAllBoatsInFleetForSeries(testRaceSeriesID, testFleetID)).thenReturn(testBoatList);
        when(mockBoat.getId()).thenReturn(testBoatID);
        when(mockRaceService.findPreviousFinishedRaceId(testRaceID)).thenReturn(testPreviousRaceId);
        when(mockHandicapRepo.findByBoatAndRace(testBoatID,testPreviousRaceId)).thenReturn(mockHandicap);
    }

    @Test
    public void testGetHandicapsForFleetNotFound() throws NoSuchUser, UserNotPermitted, NoSuchFleet
    {
        when(mockBoatService.getAllBoatsInFleetForSeries(testRaceSeriesID, testFleetID)).thenReturn(testBoatList);
        when(mockBoat.getId()).thenReturn(testBoatID);
        when(mockHandicapRepo.findByBoatAndRace(testBoatID,testPreviousRaceId)).thenReturn(mockHandicap);
        when(mockHandicapRepo.findByBoatAndRace(testBoatID,testPreviousRaceId)).thenReturn(null);
        
        List<Handicap> handicaps = fixture.getHandicapsForFleet(testRaceSeriesID, testFleetID,testRaceID);
        assertEquals(1,handicaps.size());
        assertEquals(testBoatID,handicaps.get(0).getBoatID());
        assertEquals(new Float(0.0f),handicaps.get(0).getValue());
    }

    @Test
    public void testUpdateHandicapsFromPreviousRace() throws NoSuchUser, UserNotPermitted, NoSuchRaceResult, DuplicateResult, NoSuchFleet
    {
        setupRaceMocks(false);
        setupGetHandicapsForFleetMock();
        
        List<RaceResult> testResultList = new ArrayList<RaceResult>();
        testResultList.add(mockRaceResult);
        setupRaceResultMocks(testResultList, false);
        setupHandicapMocks();
        
        fixture.updateHandicapsFromPreviousRace(testRaceID);
        verify(mockRaceResult).setHandicap(testHandicapValue);
        verify(mockRaceResultService).updateResult(mockRaceResult);
    }

    @Test
    public void testUpdateHandicapsFromPreviousRaceHandicapOverride() throws NoSuchUser, UserNotPermitted, NoSuchRaceResult, DuplicateResult, NoSuchFleet
    {
        setupRaceMocks(false);
        setupGetHandicapsForFleetMock();
        
        List<RaceResult> testResultList = new ArrayList<RaceResult>();
        testResultList.add(mockRaceResult);
        setupRaceResultMocks(testResultList, true);
        setupHandicapMocks();
        
        fixture.updateHandicapsFromPreviousRace(testRaceID);
        verify(mockRaceResult,never()).setHandicap(testHandicapValue);
        verify(mockRaceResultService,never()).updateResult(mockRaceResult);

    }

    @Test 
    public void testUpdateHandicapsAllDNS() throws NoSuchUser, UserNotPermitted
    {
        setupRaceMocks(false);
        setupHandicapLimitMocks(20.0f);

        List<RaceResultTest> testList = new ArrayList<RaceResultTest>();
        testList.add(new RaceResultTest(1.0f,ResultStatus.DNS,0,1.0f,0));
        testList.add(new RaceResultTest(2.0f,ResultStatus.DNS,0,2.0f,0));
        testUpdateHandicaps(testList, false);
    }
    
    @Test
    public void testUpdateHandicapsSortingAndAdjustment() throws NoSuchUser, UserNotPermitted
    {
        setupRaceMocks(false);
        setupHandicapLimitMocks(20.0f);

        List<RaceResultTest> testList = new ArrayList<RaceResultTest>();

        //
        //  This guy is third so 6-1 = 5
        //
        testList.add(new RaceResultTest(6.0f,ResultStatus.FINISHED,100,5.0f,0));
        
        //
        //  This guy came first so 5 -3 = 2
        //
        testList.add(new RaceResultTest(5.0f,ResultStatus.FINISHED,90,2.0f,0));

        //
        //  Second place 3-2 = 1
        //
        testList.add(new RaceResultTest(3.0f,ResultStatus.FINISHED,95,1.0f,0));
        testUpdateHandicaps(testList, false);
    }

    @Test
    public void testUpdateHandicapsSortingAndAdjustmentShortCourse() throws NoSuchUser, UserNotPermitted
    {
        setupRaceMocks(true);
        setupHandicapLimitMocks(20.0f);

        List<RaceResultTest> testList = new ArrayList<RaceResultTest>();

        //
        //  This guy is third so 6-0.5 = 5.5
        //
        testList.add(new RaceResultTest(6.0f,ResultStatus.FINISHED,100,5.5f,0));
        
        //
        //  This guy came first so 5 - 1.5 = 3.5
        //
        testList.add(new RaceResultTest(5.0f,ResultStatus.FINISHED,90,3.5f,0));

        //
        //  Second place 3-1 = 1
        //
        testList.add(new RaceResultTest(3.0f,ResultStatus.FINISHED,95,2.0f,0));
        testUpdateHandicaps(testList, true);
    }

    @Test
    public void testUpdateHandicapsAdjustmentFourthWin() throws NoSuchUser, UserNotPermitted
    {
        setupRaceMocks(false);
        setupHandicapLimitMocks(20.0f);

        List<RaceResultTest> testList = new ArrayList<RaceResultTest>();

        //
        //  This guy is third so 6-1 = 5
        //
        testList.add(new RaceResultTest(6.0f,ResultStatus.FINISHED,100,5.0f,1));
        
        //
        //  This guy came first but he has had 3 wins so 5 - 4 = 1
        //
        testList.add(new RaceResultTest(5.0f,ResultStatus.FINISHED,90,1.0f,3));
        
        //
        //  Second place 3-2 = 1
        //
        testList.add(new RaceResultTest(3.0f,ResultStatus.FINISHED,95,1.0f,2));
        
        testUpdateHandicaps(testList, false);
    }

    @Test
    public void testUpdateHandicapsAdjustmentFourthWinShortCourse() throws NoSuchUser, UserNotPermitted
    {
        setupRaceMocks(true);
        setupHandicapLimitMocks(20.0f);

        List<RaceResultTest> testList = new ArrayList<RaceResultTest>();

        //
        //  This guy is third so 6-0.5 = 5.5
        //
        testList.add(new RaceResultTest(6.0f,ResultStatus.FINISHED,100,5.5f,1));
        
        //
        //  This guy came first but he has had 3 wins so 5 - 2 = 3
        //
        testList.add(new RaceResultTest(5.0f,ResultStatus.FINISHED,90,3.0f,3));
        
        //
        //  Second place 3-1 = 2
        //
        testList.add(new RaceResultTest(3.0f,ResultStatus.FINISHED,95,2.0f,2));
        
        testUpdateHandicaps(testList, true);
    }

    @Test
    public void testUpdateHandicapsPushOutFourthWin() throws NoSuchUser, UserNotPermitted
    {
        setupRaceMocks(false);
        setupHandicapLimitMocks(20.0f);

        List<RaceResultTest> testList = new ArrayList<RaceResultTest>();

        //
        //  This guy is first but he was already on zero so still zero
        //
        testList.add(new RaceResultTest(0.0f,ResultStatus.FINISHED,100,0.0f,3));
        
        //
        //  This guy started but did not finish. The winner is on their third
        //  win so this guy gets a 4 minute pushout
        //
        testList.add(new RaceResultTest(5.0f,ResultStatus.DNF,0,9.0f,1));
        
        //
        //  This guy is DNS so no update
        //
        testList.add(new RaceResultTest(3.0f,ResultStatus.DNS,0,3.0f,1));
        
        testUpdateHandicaps(testList, false);
    }

    @Test
    public void testUpdateHandicapsPushOutFourthWinShortCourse() throws NoSuchUser, UserNotPermitted
    {
        setupRaceMocks(true);
        setupHandicapLimitMocks(20.0f);

        List<RaceResultTest> testList = new ArrayList<RaceResultTest>();

        //
        //  This guy is first but he was already on zero so still zero
        //
        testList.add(new RaceResultTest(0.0f,ResultStatus.FINISHED,100,0.0f,3));
        
        //
        //  This guy started but did not finish. The winner is on their third
        //  win so this guy gets a 2 minute pushout
        //
        testList.add(new RaceResultTest(5.0f,ResultStatus.DNF,0,7.0f,1));
        
        //
        //  This guy is DNS so no update
        //
        testList.add(new RaceResultTest(3.0f,ResultStatus.DNS,0,3.0f,1));
        
        testUpdateHandicaps(testList, true);
    }

    @Test
    public void testUpdateHandicapsPushOut() throws NoSuchUser, UserNotPermitted
    {
        setupRaceMocks(false);
        setupHandicapLimitMocks(20.0f);

        List<RaceResultTest> testList = new ArrayList<RaceResultTest>();

        //
        //  This guy is first but he was already on zero so still zero
        //
        testList.add(new RaceResultTest(0.0f,ResultStatus.FINISHED,100,0.0f,1));
        
        //
        //  This guy started but did not finish so he gets a pushout of 3
        //
        testList.add(new RaceResultTest(5.0f,ResultStatus.DNF,0,8.0f,1));
        
        //
        //  This guy is DNS so no update
        //
        testList.add(new RaceResultTest(3.0f,ResultStatus.DNS,0,3.0f,1));
        
        testUpdateHandicaps(testList, false);
    }

    @Test
    public void testUpdateHandicapsPushOutShortCourse() throws NoSuchUser, UserNotPermitted
    {
        setupRaceMocks(true);
        setupHandicapLimitMocks(20.0f);

        List<RaceResultTest> testList = new ArrayList<RaceResultTest>();

        //
        //  This guy is first but he was already on zero so still zero
        //
        testList.add(new RaceResultTest(0.0f,ResultStatus.FINISHED,100,0.0f,1));
        
        //
        //  This guy started but did not finish so he gets a pushout of 1.5
        //
        testList.add(new RaceResultTest(5.0f,ResultStatus.DNF,0,6.5f,1));
        
        //
        //  This guy is DNS so no update
        //
        testList.add(new RaceResultTest(3.0f,ResultStatus.DNS,0,3.0f,1));
        
        testUpdateHandicaps(testList, true);
    }

    @Test
    public void testUpdateHandicapsPushOutLimit() throws NoSuchUser, UserNotPermitted
    {
        setupRaceMocks(false);
        setupHandicapLimitMocks(20.0f);

        List<RaceResultTest> testList = new ArrayList<RaceResultTest>();

        //
        //  This guy is first but he was already on zero so still zero
        //
        testList.add(new RaceResultTest(0.0f,ResultStatus.FINISHED,100,0.0f,1));
        
        //
        //  This guy hit the handicap limit so he gets the limit
        //
        testList.add(new RaceResultTest(19.0f,ResultStatus.DNF,0,20.0f,1));
        
        testUpdateHandicaps(testList, false);
    }

    @Test
    public void testUpdateHandicapsPushOutLimitShortCourse() throws NoSuchUser, UserNotPermitted
    {
        setupRaceMocks(true);
        setupHandicapLimitMocks(20.0f);

        List<RaceResultTest> testList = new ArrayList<RaceResultTest>();

        //
        //  This guy is first but he was already on zero so still zero
        //
        testList.add(new RaceResultTest(0.0f,ResultStatus.FINISHED,100,0.0f,1));
        
        //
        //  This guy hit the handicap limit so he gets the limit. Limit for short
        //  course is half of long course limit
        //
        testList.add(new RaceResultTest(9.0f,ResultStatus.DNF,0,10.0f,1));
        
        testUpdateHandicaps(testList, true);
    }

    @Test
    public void testUpdateHandicapsNoLimitSet() throws NoSuchUser, UserNotPermitted
    {
        setupRaceMocks(false);
        setupHandicapLimitMocks(null);

        List<RaceResultTest> testList = new ArrayList<RaceResultTest>();

        //
        //  This guy is first but he was already on zero so still zero
        //
        testList.add(new RaceResultTest(0.0f,ResultStatus.FINISHED,100,0.0f,1));
        
        //
        //  19 but got a 3 pushout so = 22
        //
        testList.add(new RaceResultTest(19.0f,ResultStatus.DNF,0,22.0f,1));
        
        testUpdateHandicaps(testList, false);

    }
    
    public void testUpdateHandicaps(List<RaceResultTest> testResults, boolean shortCourse) throws NoSuchUser, UserNotPermitted
    {
        List<Boat> boatMocks = new ArrayList<Boat>();
        List<Handicap> mockHandicaps = new ArrayList<Handicap>();
        int i=0;

        for(i=0;i<testResults.size();i++)
        {
            Boat boatMock = Mockito.mock(Boat.class);
            when(boatMock.getId()).thenReturn(testBoatID1+i);
            boatMocks.add(boatMock);
            Handicap mockHandicap = Mockito.mock(Handicap.class);
            when(mockHandicapRepo.findByBoatAndRace(testBoatID1+i,testRaceID)).thenReturn(mockHandicap);
            mockHandicaps.add(mockHandicap);
        }
        
        List<RaceResult> resultList = new ArrayList<RaceResult>();
        i=0;
        for( RaceResultTest testResult : testResults )
        {
            resultList.add(new RaceResult(
                    testRaceID,
                    boatMocks.get(i),
                    testResult.initialHandicap,
                    false,
                    null,
                    null,
                    testResult.resultStatus,
                    0,
                    testResult.sailingTime,
                    0,
                    0));
            
            when(mockRaceResultService.getWinsForBoatBeforeDate(
                    testRaceSeriesID, testFleetID, testBoatID1+i, testRaceDate, shortCourse))
                        .thenReturn(testResult.winsCount);
            i++;
        }

        when(mockRace.getRaceDate()).thenReturn(testRaceDate);        
        when(mockRaceResultService.findAll(testRaceID)).thenReturn(resultList);
        when(mockRaceService.getRaceByID(testRaceID)).thenReturn(mockRace);
        
        fixture.updateHandicapsFromResults(testRaceID);

        for(i=0;i<testResults.size();i++)
        {
            verify(mockHandicaps.get(i)).setValue(new Float(testResults.get(i).expectedHandicapUpdate));
        }
    }

    @Test 
    public void testUpdateHandicapsAddHandicap() throws NoSuchUser, UserNotPermitted
    {
        RaceResult testResult = new RaceResult(testRaceID,mockBoat,0.0f,false,null,null,ResultStatus.FINISHED);
        List<RaceResult> resultList = new ArrayList<RaceResult>();
        resultList.add(testResult);
        
        when(mockBoat.getId()).thenReturn(testBoatID);
        when(mockRaceResultService.findAll(testRaceID)).thenReturn(resultList);
        when(mockHandicapRepo.findByBoatAndRace(testBoatID,testRaceID)).thenReturn(null);
        when(mockRaceService.getRaceByID(testRaceID)).thenReturn(mockRace);
        when(mockRace.getName()).thenReturn(TEST_RACE_NAME);
        
        setupRaceMocks(false);
        setupHandicapLimitMocks(20.0f);
        
        fixture.updateHandicapsFromResults(testRaceID);
        ArgumentCaptor<Handicap> handicapCaptor = ArgumentCaptor.forClass(Handicap.class);
        verify(mockHandicapRepo).save(handicapCaptor.capture());
        
        assertEquals(new Float(0.0),handicapCaptor.getValue().getValue());
        
        verify(mockAudit).auditEventFreeForm(
                AuditEventType.UPDATED, 
                testRaceSeriesID, 
                "Handicaps as a result of the race "+TEST_RACE_NAME);
    }

    @Test
    public void testGetHandicapLimitForFleet()
    {
        when(mockHandicapLimitRepo.findByFleetID(testRaceSeriesID, testFleetID)).thenReturn(mockHandicapLimit);
        assertEquals(mockHandicapLimit,fixture.getHandicapLimitForFleet(testRaceSeriesID, testFleetID));
    }

    @Test
    public void testGetHandicapLimit() throws NoSuchUser, UserNotPermitted
    {
        when(mockHandicapLimitRepo.findOne(testHandicapLimitID)).thenReturn(mockHandicapLimit);
        setupCheckPermissionsMocks(true);
        when(mockHandicapLimit.getRaceSeriesID()).thenReturn(testRaceSeriesID);
        assertEquals(mockHandicapLimit,fixture.getHandicapLimit(testRaceSeriesID,testHandicapLimitID));
    }

    @Test(expected=UserNotPermitted.class)
    public void testGetHandicapLimitNotPermitted() throws NoSuchUser, UserNotPermitted
    {
        when(mockHandicapLimitRepo.findOne(testHandicapLimitID)).thenReturn(mockHandicapLimit);
        setupCheckPermissionsMocks(false);
        fixture.getHandicapLimit(testRaceSeriesID,testHandicapLimitID);
    }

    @Test(expected=UserNotPermitted.class)
    public void testGetHandicapLimitWrongSeries() throws NoSuchUser, UserNotPermitted
    {
        when(mockHandicapLimitRepo.findOne(testHandicapLimitID)).thenReturn(mockHandicapLimit);
        setupCheckPermissionsMocks(true);
        when(mockHandicapLimit.getRaceSeriesID()).thenReturn(111);
        fixture.getHandicapLimit(testRaceSeriesID,testHandicapLimitID);
    }

    private void setupRaceMocks(boolean shortCourse) throws NoSuchUser, UserNotPermitted
    {
        when(mockRaceService.getRaceByID(testRaceID)).thenReturn(mockRace);
        when(mockRace.getRaceSeriesId()).thenReturn(testRaceSeriesID);
        when(mockRace.isShortCourseRace()).thenReturn(shortCourse);
        when(mockRace.getFleet()).thenReturn(mockFleet);
        when(mockFleet.getId()).thenReturn(testFleetID);
    }
    
    private void setupHandicapLimitMocks(Float handicapLimitValue) throws NoSuchUser, UserNotPermitted
    {
        if ( handicapLimitValue == null )
        {
            when(mockHandicapLimitRepo.findByFleetID(testRaceSeriesID, testFleetID)).thenReturn(null);
        }
        else
        {
            when(mockHandicapLimitRepo.findByFleetID(testRaceSeriesID, testFleetID)).thenReturn(mockHandicapLimit);
            when(mockHandicapLimit.getLimit()).thenReturn(handicapLimitValue);
            when(mockHandicapLimit.getRaceSeriesID()).thenReturn(testRaceSeriesID);
        }
    }

    private void setupCheckPermissionsMocks(boolean permitted) throws NoSuchUser
    {
        when(mockRaceSeriesAuthorizationService.isLoggedOnUserPermitted(testRaceSeriesID)).thenReturn(permitted);
    }

    private void setupHandicapMocks()
    {
        when(mockBoat.getId()).thenReturn(testBoatID);
        when(mockHandicap.getBoatID()).thenReturn(testBoatID);
        when(mockHandicap.getValue()).thenReturn(testHandicapValue);
    }

    private void setupRaceResultMocks(List<RaceResult> testResultList, boolean handicapOverride)
            throws NoSuchUser, UserNotPermitted
    {
        when(mockRaceResult.getBoat()).thenReturn(mockBoat);
        when(mockRaceResult.getOverrideHandicap()).thenReturn(handicapOverride);
        if ( ! handicapOverride )
        {
            when(mockRaceResult.getHandicap()).thenReturn(0.0f);
        }
        when(mockRaceResultService.findAll(testRaceID)).thenReturn(testResultList);
    }

    private static final Integer testRaceSeriesID=1234;
    private static final Integer testFleetID=3456;
    private static final Integer testBoatID=333;
    private static final Integer testRaceID=999;
    private static final Integer testPreviousRaceId=998;
    private static final Integer testBoatID1=111;
    private static final Integer testBoatID2=222;
    private static final Integer testBoatID3=333;
    
    private static final Integer testHandicapLimitID = 911;
    
    private List<Boat> testBoatList = new ArrayList<Boat>();
    
    private Date testRaceDate = Calendar.getInstance().getTime();
    private static final Float      testHandicapValue = 3.5f;

    private static final String     HANDICAP_OBJECT_NAME = "Handicap"; 
    private static final String     TEST_RACE_NAME = "The Muppet's trophy";
    
    @Mock private Boat              mockBoat;
    @Mock private Boat              mockBoat1;
    @Mock private Boat              mockBoat2;
    @Mock private Boat              mockBoat3;
    @Mock private Handicap          mockHandicap;
    @Mock private Handicap          mockHandicap1;
    @Mock private Handicap          mockHandicap2;
    @Mock private Handicap          mockHandicap3;
    @Mock private Race              mockRace;
    @Mock private Fleet             mockFleet;
    @Mock private HandicapLimit     mockHandicapLimit;
    
    @Mock private BoatService           mockBoatService;
    @Mock private HandicapRepository    mockHandicapRepo;
    @Mock private RaceResultService     mockRaceResultService;
    @Mock private RaceService           mockRaceService;
    @Mock private HandicapLimitsRepository  mockHandicapLimitRepo;
    @Mock private RaceSeriesAuthorizationService    mockRaceSeriesAuthorizationService;
    @Mock private AuditService          mockAudit;
    @Mock private RaceResult            mockRaceResult;
    
    @InjectMocks
    private HandicapService fixture = new HandicapServiceImpl();
}
