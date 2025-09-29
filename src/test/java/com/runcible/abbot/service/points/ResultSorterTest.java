package com.runcible.abbot.service.points;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.runcible.abbot.model.Boat;
import com.runcible.abbot.model.BoatClass;
import com.runcible.abbot.model.RaceResult;
import com.runcible.abbot.model.ResultStatus;
import com.runcible.abbot.model.ResultType;

@ExtendWith(MockitoExtension.class)
public class ResultSorterTest
{
    @BeforeEach
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
        List<RaceResult> sortedResults = fixture.sortResults(raceResults, ResultType.HANDICAP_RESULT);
        assertEquals(testBoat2,sortedResults.get(0).getBoat());
        assertEquals(testBoat1,sortedResults.get(1).getBoat());
        assertEquals(testBoat3,sortedResults.get(2).getBoat());
        
        checkTail(sortedResults);
    }

    @Test
    public void testScratchSort()
    {
        List<RaceResult> sortedResults = fixture.sortResults(raceResults, ResultType.SCRATCH_RESULT);
        assertEquals(testBoat1,sortedResults.get(0).getBoat());
        assertEquals(testBoat3,sortedResults.get(1).getBoat());
        assertEquals(testBoat2,sortedResults.get(2).getBoat());
        
        checkTail(sortedResults);
    }

    private void checkTail(List<RaceResult> sortedResults)
    {
        assertTrue(startedButNotFinished(sortedResults.get(3)));
        assertTrue(startedButNotFinished(sortedResults.get(4)));
        assertTrue(!sortedResults.get(5).getStatus().isStarted());
        assertTrue(!sortedResults.get(6).getStatus().isStarted());
    }

    private boolean startedButNotFinished(RaceResult raceResult)
    {
        return !raceResult.getStatus().isFinished() && raceResult.getStatus().isStarted();
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

    private RaceResult resultBoat1 = new RaceResult(
            null, testBoat1, 1.0f, false, new Date(), new Date(), ResultStatus.FINISHED,90,90,0,0);
    private RaceResult resultBoat2 = new RaceResult(
            null, testBoat2, 1.0f, false, new Date(), new Date(), ResultStatus.FINISHED,100,85,0,0);
    private RaceResult resultBoat3 = new RaceResult(
            null, testBoat3, 1.0f, false, new Date(), new Date(), ResultStatus.FINISHED,95,94,0,0);
    private RaceResult resultNonStarter1 = new RaceResult(
            null, testNonStarter1, 1.0f, false, new Date(), new Date(), ResultStatus.DNS);
    private RaceResult resultNonStarter2 = new RaceResult(
            null, testNonStarter2, 1.0f, false, new Date(), new Date(), ResultStatus.DNC);
    private RaceResult resultNonFinisher1 = new RaceResult(
            null, testNonFinisher1, 1.0f, false, new Date(), new Date(),ResultStatus.DNF);
    private RaceResult resultNonFinisher2 = new RaceResult(
            null, testNonFinisher2, 1.0f, false, new Date(), new Date(),ResultStatus.OCS);

    private RaceResultSorter fixture = new RaceResultSorterImpl();
}
