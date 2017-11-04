package com.runcible.abbot.service;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.runcible.abbot.model.Boat;
import com.runcible.abbot.model.Handicap;
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

    private static final Integer testRaceSeriesID=1234;
    private static final Integer testFleetID=3456;
    private static final Integer testBoatID=333;
    
    private List<Boat> testBoatList = new ArrayList<Boat>();
    
    @Mock private Boat      mockBoat;
    @Mock private Handicap  mockHandicap;
    
    @Mock private BoatService           mockBoatService;
    @Mock private HandicapRepository    mockHandicapRepo;
    
    @InjectMocks
    private HandicapService fixture = new HandicapServiceImpl();
}
