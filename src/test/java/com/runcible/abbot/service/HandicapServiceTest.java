package com.runcible.abbot.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
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
import com.runcible.abbot.service.exceptions.NoSuchFleet;
import com.runcible.abbot.service.exceptions.NoSuchUser;
import com.runcible.abbot.service.exceptions.UserNotPermitted;

@RunWith(MockitoJUnitRunner.class)
public class HandicapServiceTest
{
    @Before
    public void setUp()
    {
        testBoatList.add(mockBoat);
    }
    
    @Test
    public void testGetHandicapsForFleet() throws NoSuchUser, UserNotPermitted, NoSuchFleet
    {
        when(mockBoatService.getAllBoatsInFleetForSeries(testRaceSeriesID, testFleetID)).thenReturn(testBoatList);
        when(mockBoat.getId()).thenReturn(testBoatID);
        when(mockHandicapRepo.findByBoatID(testBoatID)).thenReturn(mockHandicap);
        
        List<Handicap> handicaps = fixture.getHandicapsForFleet(testRaceSeriesID, testFleetID);
        assertEquals(1,handicaps.size());
        assertEquals(mockHandicap,handicaps.get(0));
    }

    @Test
    public void testGetHandicapsForFleetNotFound() throws NoSuchUser, UserNotPermitted, NoSuchFleet
    {
        when(mockBoatService.getAllBoatsInFleetForSeries(testRaceSeriesID, testFleetID)).thenReturn(testBoatList);
        when(mockBoat.getId()).thenReturn(testBoatID);
        when(mockHandicapRepo.findByBoatID(testBoatID)).thenReturn(null);
        
        List<Handicap> handicaps = fixture.getHandicapsForFleet(testRaceSeriesID, testFleetID);
        assertEquals(1,handicaps.size());
        assertEquals(testBoatID,handicaps.get(0).getBoatID());
        assertEquals(new Float(0.0f),handicaps.get(0).getValue());
    }

    @Test 
    public void testUpdateHandicapsAllDNS() throws NoSuchUser, UserNotPermitted
    {
        RaceResult testResult = new RaceResult(testRaceID,mockBoat,0,null,null,ResultStatus.DNS);
        List<RaceResult> resultList = new ArrayList<RaceResult>();
        resultList.add(testResult);
        
        setupHandicapLimitMocks(20.0f);
        
        when(mockRaceResultService.findAll(testRaceID)).thenReturn(resultList);
        when(mockRaceService.getRaceByID(testRaceID)).thenReturn(mockRace);
        
        //
        //  Basically nothing should happen as the result is DNS
        //
        fixture.updateHandicapsFromResults(testRaceID);
        verifyNoMoreInteractions(mockHandicapRepo);
    }
    
    @Test
    public void testUpdateHandicapsSortingAndAdjustment() throws NoSuchUser, UserNotPermitted
    {
        RaceResult testResult1 = new RaceResult(
                testRaceID,mockBoat1,6,null,null,ResultStatus.FINISHED,0,100,0,0);
        RaceResult testResult2 = new RaceResult(
                testRaceID,mockBoat2,5,null,null,ResultStatus.FINISHED,0,90,0,0);
        RaceResult testResult3 = new RaceResult(
                testRaceID,mockBoat3,3,null,null,ResultStatus.FINISHED,0,95,0,0);

        when(mockBoat1.getId()).thenReturn(testBoatID1);
        when(mockBoat2.getId()).thenReturn(testBoatID2);
        when(mockBoat3.getId()).thenReturn(testBoatID3);
        
        List<RaceResult> resultList = new ArrayList<RaceResult>();
        resultList.add(testResult1);
        resultList.add(testResult2);
        resultList.add(testResult3);
        
        setupHandicapLimitMocks(20.0f);
        
        when(mockRaceResultService.findAll(testRaceID)).thenReturn(resultList);
        when(mockHandicapRepo.findByBoatID(testBoatID1)).thenReturn(mockHandicap1);
        when(mockHandicapRepo.findByBoatID(testBoatID2)).thenReturn(mockHandicap2);
        when(mockHandicapRepo.findByBoatID(testBoatID3)).thenReturn(mockHandicap3);
        when(mockRaceService.getRaceByID(testRaceID)).thenReturn(mockRace);
        
        fixture.updateHandicapsFromResults(testRaceID);
        
        //
        //  This guy is third so 6-1 = 5
        //
        verify(mockHandicap1).setValue(new Float(5.0));
        
        //
        //  This guy came first so 5 -3 = 2
        //
        verify(mockHandicap2).setValue(new Float(2.0));
        
        //
        //  Second place 3-2 = 1
        //
        verify(mockHandicap3).setValue(new Float(1.0));
    }

    @Test
    public void testUpdateHandicapsAdjustmentFourthWin() throws NoSuchUser, UserNotPermitted
    {
        RaceResult testResult1 = new RaceResult(
                testRaceID,mockBoat1,6,null,null,ResultStatus.FINISHED,0,100,0,0);
        RaceResult testResult2 = new RaceResult(
                testRaceID,mockBoat2,5,null,null,ResultStatus.FINISHED,0,90,0,0);
        RaceResult testResult3 = new RaceResult(
                testRaceID,mockBoat3,3,null,null,ResultStatus.FINISHED,0,95,0,0);

        when(mockBoat1.getId()).thenReturn(testBoatID1);
        when(mockBoat2.getId()).thenReturn(testBoatID2);
        when(mockBoat3.getId()).thenReturn(testBoatID3);
        
        List<RaceResult> resultList = new ArrayList<RaceResult>();
        resultList.add(testResult1);
        resultList.add(testResult2);
        resultList.add(testResult3);
        
        setupHandicapLimitMocks(20.0f);
        
        when(mockRaceResultService.findAll(testRaceID)).thenReturn(resultList);
        when(mockHandicapRepo.findByBoatID(testBoatID1)).thenReturn(mockHandicap1);
        when(mockHandicapRepo.findByBoatID(testBoatID2)).thenReturn(mockHandicap2);
        when(mockHandicapRepo.findByBoatID(testBoatID3)).thenReturn(mockHandicap3);
        when(mockRaceService.getRaceByID(testRaceID)).thenReturn(mockRace);
        
        when(mockRace.getRaceDate()).thenReturn(testRaceDate);
        when(mockRaceResultService.getWinsForBoatBeforeDate(
                testRaceSeriesID, testFleetID, testBoatID1, testRaceDate)).thenReturn(1);
        when(mockRaceResultService.getWinsForBoatBeforeDate(
                testRaceSeriesID, testFleetID, testBoatID2, testRaceDate)).thenReturn(3);
        when(mockRaceResultService.getWinsForBoatBeforeDate(
                testRaceSeriesID, testFleetID, testBoatID3, testRaceDate)).thenReturn(2);

        fixture.updateHandicapsFromResults(testRaceID);
        
        //
        //  This guy is third so 6-1 = 5
        //
        verify(mockHandicap1).setValue(new Float(5.0));
        
        //
        //  This guy came first but he has had 3 wins so 5 - 4 = 1
        //
        verify(mockHandicap2).setValue(new Float(1.0));
        
        //
        //  Second place 3-2 = 1
        //
        verify(mockHandicap3).setValue(new Float(1.0));
    }

    @Test
    public void testUpdateHandicapsPushOutFourthWin() throws NoSuchUser, UserNotPermitted
    {
        RaceResult testResult1 = new RaceResult(
                testRaceID,mockBoat1,0,null,null,ResultStatus.FINISHED,0,100,0,0);
        RaceResult testResult2 = new RaceResult(
                testRaceID,mockBoat2,5,null,null,ResultStatus.DNF,0,0,0,0);
        RaceResult testResult3 = new RaceResult(
                testRaceID,mockBoat3,3,null,null,ResultStatus.DNS,0,0,0,0);

        when(mockBoat1.getId()).thenReturn(testBoatID1);
        when(mockBoat2.getId()).thenReturn(testBoatID2);
        when(mockBoat3.getId()).thenReturn(testBoatID3);
        
        setupHandicapLimitMocks(20.0f);
        
        List<RaceResult> resultList = new ArrayList<RaceResult>();
        resultList.add(testResult1);
        resultList.add(testResult2);
        resultList.add(testResult3);
        
        when(mockRaceResultService.findAll(testRaceID)).thenReturn(resultList);
        when(mockHandicapRepo.findByBoatID(testBoatID1)).thenReturn(mockHandicap1);
        when(mockHandicapRepo.findByBoatID(testBoatID2)).thenReturn(mockHandicap2);
        when(mockRaceService.getRaceByID(testRaceID)).thenReturn(mockRace);
        
        when(mockRace.getRaceDate()).thenReturn(testRaceDate);
        when(mockRaceResultService.getWinsForBoatBeforeDate(
                testRaceSeriesID, testFleetID, testBoatID1, testRaceDate)).thenReturn(3);
        when(mockRaceResultService.getWinsForBoatBeforeDate(
                testRaceSeriesID, testFleetID, testBoatID2, testRaceDate)).thenReturn(1);
        when(mockRaceResultService.getWinsForBoatBeforeDate(
                testRaceSeriesID, testFleetID, testBoatID3, testRaceDate)).thenReturn(1);

        fixture.updateHandicapsFromResults(testRaceID);
        
        //
        //  This guy is first but he was already on zero so still zero
        //
        verify(mockHandicap1).setValue(new Float(0.0));
        
        //
        //  This guy started but did not finish. The winner is on their third
        //  win so this guy gets a 4 minute pushout
        //
        verify(mockHandicap2).setValue(new Float(9.0));

        //
        //  There should be no more updates as the third guy is DNS
        //
        verifyNoMoreInteractions(mockHandicap3);
    }

    @Test
    public void testUpdateHandicapsPushOut() throws NoSuchUser, UserNotPermitted
    {
        RaceResult testResult1 = new RaceResult(
                testRaceID,mockBoat1,0,null,null,ResultStatus.FINISHED,0,100,0,0);
        RaceResult testResult2 = new RaceResult(
                testRaceID,mockBoat2,5,null,null,ResultStatus.DNF,0,0,0,0);
        RaceResult testResult3 = new RaceResult(
                testRaceID,mockBoat3,3,null,null,ResultStatus.DNS,0,0,0,0);

        when(mockBoat1.getId()).thenReturn(testBoatID1);
        when(mockBoat2.getId()).thenReturn(testBoatID2);
        when(mockBoat3.getId()).thenReturn(testBoatID3);
        
        setupHandicapLimitMocks(20.0f);
        
        List<RaceResult> resultList = new ArrayList<RaceResult>();
        resultList.add(testResult1);
        resultList.add(testResult2);
        resultList.add(testResult3);
        
        when(mockRaceResultService.findAll(testRaceID)).thenReturn(resultList);
        when(mockHandicapRepo.findByBoatID(testBoatID1)).thenReturn(mockHandicap1);
        when(mockHandicapRepo.findByBoatID(testBoatID2)).thenReturn(mockHandicap2);
        when(mockRaceService.getRaceByID(testRaceID)).thenReturn(mockRace);
        
        fixture.updateHandicapsFromResults(testRaceID);
        
        //
        //  This guy is first but he was already on zero so still zero
        //
        verify(mockHandicap1).setValue(new Float(0.0));
        
        //
        //  This guy started but did not finish so he gets a pushout of 3
        //
        verify(mockHandicap2).setValue(new Float(8.0));

        //
        //  There should be no more updates as the third guy is DNS
        //
        verifyNoMoreInteractions(mockHandicap3);
    }

    @Test
    public void testUpdateHandicapsPushOutLimit() throws NoSuchUser, UserNotPermitted
    {
        RaceResult testResult1 = new RaceResult(
                testRaceID,
                mockBoat1,
                0,
                null,
                null,
                ResultStatus.FINISHED,
                0,
                100,
                0,
                0);
        
        RaceResult testResult2 = new RaceResult(
                testRaceID,
                mockBoat2,
                19,
                null,
                null,
                ResultStatus.DNF,
                0,
                0,
                0,
                0);

        when(mockBoat1.getId()).thenReturn(testBoatID1);
        when(mockBoat2.getId()).thenReturn(testBoatID2);
        
        setupHandicapLimitMocks(20.0f);
        
        List<RaceResult> resultList = new ArrayList<RaceResult>();
        resultList.add(testResult1);
        resultList.add(testResult2);
        
        when(mockRaceResultService.findAll(testRaceID)).thenReturn(resultList);
        when(mockHandicapRepo.findByBoatID(testBoatID1)).thenReturn(mockHandicap1);
        when(mockHandicapRepo.findByBoatID(testBoatID2)).thenReturn(mockHandicap2);
        when(mockRaceService.getRaceByID(testRaceID)).thenReturn(mockRace);
        
        fixture.updateHandicapsFromResults(testRaceID);
        
        //
        //  This guy is first but he was already on zero so still zero
        //
        verify(mockHandicap1).setValue(new Float(0.0));
        
        //
        //  This guy hit the handicap limit so he gets the limit
        //
        verify(mockHandicap2).setValue(new Float(20.0));
    }

    @Test 
    public void testUpdateHandicapsAddHandicap() throws NoSuchUser, UserNotPermitted
    {
        RaceResult testResult = new RaceResult(testRaceID,mockBoat,0,null,null,ResultStatus.FINISHED);
        List<RaceResult> resultList = new ArrayList<RaceResult>();
        resultList.add(testResult);
        
        when(mockBoat.getId()).thenReturn(testBoatID);
        when(mockRaceResultService.findAll(testRaceID)).thenReturn(resultList);
        when(mockHandicapRepo.findByBoatID(testBoatID)).thenReturn(null);
        when(mockRaceService.getRaceByID(testRaceID)).thenReturn(mockRace);
        setupHandicapLimitMocks(20.0f);
        
        fixture.updateHandicapsFromResults(testRaceID);
        ArgumentCaptor<Handicap> handicapCaptor = ArgumentCaptor.forClass(Handicap.class);
        verify(mockHandicapRepo).save(handicapCaptor.capture());
        
        assertEquals(new Float(0.0),handicapCaptor.getValue().getValue());
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

    private void setupHandicapLimitMocks(Float handicapLimitValue) throws NoSuchUser, UserNotPermitted
    {
        when(mockRaceService.getRaceByID(testRaceID)).thenReturn(mockRace);
        when(mockRace.getRaceSeriesId()).thenReturn(testRaceSeriesID);
        when(mockRace.getFleet()).thenReturn(mockFleet);
        when(mockFleet.getId()).thenReturn(testFleetID);
        when(mockHandicapLimitRepo.findByFleetID(testRaceSeriesID, testFleetID)).thenReturn(mockHandicapLimit);
        when(mockHandicapLimit.getLimit()).thenReturn(handicapLimitValue);
        when(mockHandicapLimit.getRaceSeriesID()).thenReturn(testRaceSeriesID);
    }

    private void setupCheckPermissionsMocks(boolean permitted) throws NoSuchUser
    {
        when(mockRaceSeriesAuthorizationService.isLoggedOnUserPermitted(testRaceSeriesID)).thenReturn(permitted);
    }

    private static final Integer testRaceSeriesID=1234;
    private static final Integer testFleetID=3456;
    private static final Integer testBoatID=333;
    private static final Integer testRaceID=999;
    private static final Integer testBoatID1=111;
    private static final Integer testBoatID2=222;
    private static final Integer testBoatID3=333;
    
    private static final Integer testHandicapLimitID = 911;
    
    private List<Boat> testBoatList = new ArrayList<Boat>();
    
    private Date testRaceDate = Calendar.getInstance().getTime();
    
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
    
    @InjectMocks
    private HandicapService fixture = new HandicapServiceImpl();
}
