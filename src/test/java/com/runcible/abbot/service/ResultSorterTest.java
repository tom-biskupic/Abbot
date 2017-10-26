package com.runcible.abbot.service;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.runcible.abbot.model.Boat;
import com.runcible.abbot.model.BoatClass;
import com.runcible.abbot.model.Competition;
import com.runcible.abbot.model.RaceResult;
import com.runcible.abbot.model.ResultStatus;
import com.runcible.abbot.model.ResultType;

@RunWith(MockitoJUnitRunner.class)
public class ResultSorterTest
{
    @Before
    public void setUp()
    {
        raceResults.add(resultBoat1);
        raceResults.add(resultBoat2);
        raceResults.add(resultBoat3);
        raceResults.add(resultNonStarter1);
        raceResults.add(resultNonStarter2);
        raceResults.add(resultNonFinisher1);
        raceResults.add(resultNonFinisher2);
    }

    @Test
    public void testHandicapSort()
    {
        when(mockCompetition.getResultType()).thenReturn(ResultType.HANDICAP_RESULT);
        List<RaceResult> sortedResults = fixture.sortResults(raceResults, mockCompetition);
        assertEquals(testBoat2,sortedResults.get(0).getBoat());
        assertEquals(testBoat1,sortedResults.get(1).getBoat());
        assertEquals(testBoat3,sortedResults.get(2).getBoat());
        
        checkTail(sortedResults);
    }

    @Test
    public void testScratchSort()
    {
        when(mockCompetition.getResultType()).thenReturn(ResultType.SCRATCH_RESULT);
        List<RaceResult> sortedResults = fixture.sortResults(raceResults, mockCompetition);
        assertEquals(testBoat1,sortedResults.get(0).getBoat());
        assertEquals(testBoat3,sortedResults.get(1).getBoat());
        assertEquals(testBoat2,sortedResults.get(2).getBoat());
        
        checkTail(sortedResults);
    }

    private void checkTail(List<RaceResult> sortedResults)
    {
        assertTrue(startedButNotFinished(sortedResults.get(3)));
        assertTrue(startedButNotFinished(sortedResults.get(4)));
        assertTrue(!sortedResults.get(5).isStarted());
        assertTrue(!sortedResults.get(6).isStarted());
    }

    private boolean startedButNotFinished(RaceResult raceResult)
    {
        return !raceResult.isFinished() && raceResult.isStarted();
    }
    
    private List<RaceResult> raceResults = new ArrayList<RaceResult>();

    private BoatClass testClass = new BoatClass(null, 123, "Laser", 123.0f);

    private Boat testBoat1 = new Boat(null, "FredTheWinner", "1234", testClass, null, false, "", "");
    private Boat testBoat2 = new Boat(null, "CloseBehind", "1234", testClass, null, false, "", "");
    private Boat testBoat3 = new Boat(null, "IkeepTheTail", "1234", testClass, null, false, "", "");
    private Boat testNonStarter1 = new Boat(null, "NotHere", "1234", testClass, null, false, "", "");
    private Boat testNonStarter2 = new Boat(null, "Couldn't Be Bothered", "1234", testClass, null, false, "", "");
    private Boat testNonFinisher1 = new Boat(null, "NotFinished", "1234", testClass, null, false, "", "");
    private Boat testNonFinisher2 = new Boat(null, "NotFinishedEither", "1234", testClass, null, false, "", "");

    private RaceResult resultBoat1 = new RaceResult(null, testBoat1, 1, new Date(), new Date(), ResultStatus.FINISHED,90,90);
    private RaceResult resultBoat2 = new RaceResult(null, testBoat2, 1, new Date(), new Date(), ResultStatus.FINISHED,100,85);
    private RaceResult resultBoat3 = new RaceResult(null, testBoat3, 1, new Date(), new Date(), ResultStatus.FINISHED,95,94);
    private RaceResult resultNonStarter1 = new RaceResult(null, testNonStarter1, 1, new Date(), new Date(), ResultStatus.DNS);
    private RaceResult resultNonStarter2 = new RaceResult(null, testNonStarter2, 1, new Date(), new Date(), ResultStatus.DNC);
    private RaceResult resultNonFinisher1 = new RaceResult(
                null, testNonFinisher1, 1, new Date(), new Date(),ResultStatus.DNF);
    private RaceResult resultNonFinisher2 = new RaceResult(
            null, testNonFinisher2, 1, new Date(), new Date(),ResultStatus.OCS);

    @Mock Competition mockCompetition;
    
    private RaceResultSorter fixture = new RaceResultSorterImpl();
}
