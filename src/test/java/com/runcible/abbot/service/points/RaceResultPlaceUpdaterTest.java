package com.runcible.abbot.service.points;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.runcible.abbot.model.Boat;
import com.runcible.abbot.model.RaceResult;
import com.runcible.abbot.model.ResultStatus;
import com.runcible.abbot.model.ResultType;
import com.runcible.abbot.service.exceptions.DuplicateResult;

@ExtendWith(MockitoExtension.class)
public class RaceResultPlaceUpdaterTest
{

    @BeforeEach
    public void setUp()
    {
        testExistingResults = new ArrayList<RaceResult>();
        testExistingResults.add(mockExistingResult);
        
        testExistingResultsUpdateCase = new ArrayList<RaceResult>();
        testExistingResultsUpdateCase.add(mockExistingResult);
        testExistingResultsUpdateCase.add(mockResultToUpdate);
    }
    
    @Test
    public void testSortingAfterAdd() throws DuplicateResult
    {
        setupMockBoats(false);
        
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

    @Test
    public void testSortingAfterUpdate() throws DuplicateResult
    {
        setupMockBoats(false);
        
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

    @Test 
    public void testTie() throws DuplicateResult
    {
        setupMockBoats(true);
        
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
        verify(mockResultToUpdate).setHandicapPlace(1);
        verify(mockResultToUpdate).setScratchPlace(1);
    }
    
    private void setupMockBoats(boolean makeTie)
    {
        when(mockResultToUpdate.getId()).thenReturn(testResultID1);
        when(mockResultToUpdate.getBoat()).thenReturn(mockBoat1);
        when(mockResultToUpdate.getStatus()).thenReturn(ResultStatus.FINISHED);
        when(mockResultToUpdate.getSailingTime()).thenReturn(100);
        when(mockResultToUpdate.getCorrectedTime()).thenReturn(100);
        when(mockBoat1.getId()).thenReturn(123);
        when(mockExistingResult.getId()).thenReturn(testResultID2);
        when(mockExistingResult.getBoat()).thenReturn(mockBoat2);
        when(mockExistingResult.getStatus()).thenReturn(ResultStatus.FINISHED);
        when(mockExistingResult.getSailingTime()).thenReturn(makeTie? 100 : 90);
        when(mockExistingResult.getCorrectedTime()).thenReturn(makeTie? 100 : 90);

        if ( makeTie )
        {
            when(mockExistingResult.getHandicapPlace()).thenReturn(1);
            when(mockExistingResult.getScratchPlace()).thenReturn(1);
        }
        
        when(mockBoat2.getId()).thenReturn(456);
    }
    
    private static final Integer testRaceID = 11;
    private static final Integer testResultID1 = 919;
    private static final Integer testResultID2 = 929;
    
    private @Mock Boat          mockBoat1;
    private @Mock RaceResult    mockExistingResult;
    private @Mock Boat          mockBoat2;
    private @Mock RaceResult    mockResultToUpdate;
    
    private List<RaceResult> testExistingResults;
    private List<RaceResult> testExistingResultsUpdateCase;
    
    private @Mock RaceResultSorter mockRaceResultSorter;
    
    @InjectMocks
    private RaceResultPlaceUpdater fixture = new RaceResultPlaceUpdaterImpl();
}
