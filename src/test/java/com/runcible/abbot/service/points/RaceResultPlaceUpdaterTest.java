package com.runcible.abbot.service.points;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.runcible.abbot.model.Boat;
import com.runcible.abbot.model.RaceResult;
import com.runcible.abbot.model.ResultStatus;
import com.runcible.abbot.model.ResultType;
import com.runcible.abbot.service.exceptions.DuplicateResult;

@RunWith(MockitoJUnitRunner.class)
public class RaceResultPlaceUpdaterTest
{

    @Before
    public void setUp()
    {
        testExistingResults = new ArrayList<RaceResult>();
        testExistingResults.add(mockExistingResult);
    }
    
    @Test
    public void testSorting() throws DuplicateResult
    {
        when(mockResultToUpdate.getBoat()).thenReturn(mockBoat1);
        when(mockResultToUpdate.getStatus()).thenReturn(ResultStatus.FINISHED);
        when(mockBoat1.getId()).thenReturn(123);
        when(mockExistingResult.getBoat()).thenReturn(mockBoat2);
        when(mockExistingResult.getStatus()).thenReturn(ResultStatus.FINISHED);
        when(mockBoat2.getId()).thenReturn(456);
        
        fixture.updateResultPlaces(mockResultToUpdate, testExistingResults);
        ArgumentCaptor<List> resultsCaptor1 = ArgumentCaptor.forClass(List.class);
        ArgumentCaptor<List> resultsCaptor2 = ArgumentCaptor.forClass(List.class);
        
        verify(mockRaceResultSorter).sortResults(resultsCaptor1.capture(), eq(ResultType.HANDICAP_RESULT));
        verify(mockRaceResultSorter).sortResults(resultsCaptor2.capture(), eq(ResultType.SCRATCH_RESULT));
        
        List<RaceResult> resultsList1 = resultsCaptor1.getValue();
        assertEquals(2,resultsList1.size());
        assertEquals(mockExistingResult,resultsList1.get(0));
        assertEquals(mockResultToUpdate,resultsList1.get(1));
        assertEquals(resultsList1,resultsCaptor2.getValue());
        
        verify(mockExistingResult).setHandicapPlace(1);
        verify(mockExistingResult).setScratchPlace(1);
        verify(mockResultToUpdate).setHandicapPlace(2);
        verify(mockResultToUpdate).setScratchPlace(2);

    }
    
    private static final Integer testRaceID = 11;
    
    private @Mock Boat          mockBoat1;
    private @Mock RaceResult    mockExistingResult;
    private @Mock Boat          mockBoat2;
    private @Mock RaceResult    mockResultToUpdate;
    
    private List<RaceResult> testExistingResults;
    
    private @Mock RaceResultSorter mockRaceResultSorter;
    
    @InjectMocks
    private RaceResultPlaceUpdater fixture = new RaceResultPlaceUpdaterImpl();
}
