package com.runcible.abbot.service;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.runcible.abbot.model.Boat;
import com.runcible.abbot.model.BoatClass;
import com.runcible.abbot.model.Competition;
import com.runcible.abbot.model.Fleet;
import com.runcible.abbot.model.PointsForBoat;
import com.runcible.abbot.model.PointsSystem;
import com.runcible.abbot.model.PointsTable;
import com.runcible.abbot.model.Race;
import com.runcible.abbot.model.RaceResult;
import com.runcible.abbot.model.RaceStatus;
import com.runcible.abbot.model.ResultStatus;
import com.runcible.abbot.model.ResultType;
import com.runcible.abbot.service.audit.AuditEventType;
import com.runcible.abbot.service.audit.AuditService;
import com.runcible.abbot.service.exceptions.NoSuchCompetition;
import com.runcible.abbot.service.exceptions.NoSuchFleet;
import com.runcible.abbot.service.exceptions.NoSuchUser;
import com.runcible.abbot.service.exceptions.UserNotPermitted;
import com.runcible.abbot.service.points.PointsCalculator;
import com.runcible.abbot.service.points.PointsSorter;
import com.runcible.abbot.service.points.PointsTotalCalculator;
import com.runcible.abbot.service.points.RaceResultSorter;

@RunWith(MockitoJUnitRunner.class)
public class PointsServiceTest
{
	@Before
	public void setUp()
	{
		raceResults.add(resultBoat1);
		raceResults.add(resultBoat2);
		raceResults.add(resultBoat3);
		races.add(mockRace);

		racesWithIncompleteEntry.add(mockRace);
		racesWithIncompleteEntry.add(mockIncompleteRace);
		
		boats.add(testBoat1);
		boats.add(testBoat2);
		boats.add(testBoat3);
	}

	@Test
	public void testSimple() throws NoSuchCompetition, NoSuchUser, UserNotPermitted, NoSuchFleet
	{
		setupCompetition();

		setupDataFetchingMocks(raceResults);

		when(mockRaceResultSorter.sortResults(raceResults,ResultType.SCRATCH_RESULT)).thenReturn(raceResults);
		
		when(mockPointsCalculator.calculatePoints(mockCompetition, 3, 1, ResultStatus.FINISHED)).thenReturn(1.0f);
		when(mockPointsCalculator.calculatePoints(mockCompetition, 3, 2, ResultStatus.FINISHED)).thenReturn(2.0f);
		when(mockPointsCalculator.calculatePoints(mockCompetition, 3, 3, ResultStatus.FINISHED)).thenReturn(3.0f);
		
		PointsTable pointsTable = fixture.generatePointsTable(testRaceSeriesID, testCompetitionID);
		assertEquals(mockCompetition,pointsTable.getCompetition());
		assertEquals(races,pointsTable.getRaces());
		verify(mockPointsSorter).sortPoints(pointsTable);
		
		checkPoints(pointsTable, testBoat1, 1.0f);
		checkPoints(pointsTable, testBoat2, 2.0f);
		checkPoints(pointsTable, testBoat3, 3.0f);
		
        verify(mockAudit).auditEvent(
                AuditEventType.CREATED, 
                testRaceSeriesID, 
                POINTS_TABLE_NAME, 
                TEST_COMPETITION_NAME);
	}

	@Test
	public void testSkippingIncompleteRaces() throws NoSuchCompetition, NoSuchUser, UserNotPermitted, NoSuchFleet
	{
	    setupCompetition();

        when(mockFleet.getId()).thenReturn(testFleetID);
        when(mockBoatService.getAllBoatsInFleetForSeries(testRaceSeriesID, testFleetID)).thenReturn(boats);
        when(mockRaceService.getRacesInCompetition(mockCompetition)).thenReturn(racesWithIncompleteEntry);
        when(mockRace.getId()).thenReturn(testRaceID);
        when(mockRace.getRaceStatus()).thenReturn(RaceStatus.COMPLETED);
        when(mockResultService.findAll(testRaceID)).thenReturn(raceResults);

        when(mockIncompleteRace.getRaceStatus()).thenReturn(RaceStatus.NOT_RUN);

        when(mockRaceResultSorter.sortResults(raceResults,ResultType.SCRATCH_RESULT)).thenReturn(raceResults);
        
        when(mockPointsCalculator.calculatePoints(mockCompetition, 3, 1, ResultStatus.FINISHED)).thenReturn(1.0f);
        when(mockPointsCalculator.calculatePoints(mockCompetition, 3, 2, ResultStatus.FINISHED)).thenReturn(2.0f);
        when(mockPointsCalculator.calculatePoints(mockCompetition, 3, 3, ResultStatus.FINISHED)).thenReturn(3.0f);
        
        PointsTable pointsTable = fixture.generatePointsTable(testRaceSeriesID, testCompetitionID);
        
        //
        //  Now verify that the race list only includes the complete race
        //
        assertEquals(races,pointsTable.getRaces());
        
        verify(mockAudit).auditEvent(
                AuditEventType.CREATED, 
                testRaceSeriesID, 
                POINTS_TABLE_NAME, 
                TEST_COMPETITION_NAME);
	}
	
	@Test
	public void testResultMissing() throws NoSuchCompetition, NoSuchUser, UserNotPermitted, NoSuchFleet
	{
	    setupCompetition();

	    List<RaceResult> resultsWithMissingEntry = new ArrayList<RaceResult>();
	    resultsWithMissingEntry.add(resultBoat1);
	    resultsWithMissingEntry.add(resultBoat2);
	    
	    setupDataFetchingMocks(resultsWithMissingEntry);

	    when(mockRaceResultSorter.sortResults(
	            resultsWithMissingEntry, ResultType.SCRATCH_RESULT)).thenReturn(resultsWithMissingEntry);
	        
	    when(mockPointsCalculator.calculatePoints(mockCompetition, 2, 1, ResultStatus.FINISHED)).thenReturn(1.0f);
	    when(mockPointsCalculator.calculatePoints(mockCompetition, 2, 2, ResultStatus.FINISHED)).thenReturn(2.0f);
	    when(mockPointsCalculator.calculatePoints(mockCompetition, 2, 0, ResultStatus.DNS)).thenReturn(3.0f);
	        
	    PointsTable pointsTable = fixture.generatePointsTable(testRaceSeriesID, testCompetitionID);
	    assertEquals(mockCompetition,pointsTable.getCompetition());
	    assertEquals(races,pointsTable.getRaces());
	    verify(mockPointsSorter).sortPoints(pointsTable);
	    
	    checkPoints(pointsTable, testBoat1, 1.0f);
	    checkPoints(pointsTable, testBoat2, 2.0f);
	    checkPoints(pointsTable, testBoat3, 3.0f);
	    
        verify(mockAudit).auditEvent(
                AuditEventType.CREATED, 
                testRaceSeriesID, 
                POINTS_TABLE_NAME, 
                TEST_COMPETITION_NAME);
	}

    private void setupDataFetchingMocks(List<RaceResult> results)
            throws NoSuchUser, UserNotPermitted, NoSuchFleet
    {
        when(mockFleet.getId()).thenReturn(testFleetID);
		when(mockBoatService.getAllBoatsInFleetForSeries(testRaceSeriesID, testFleetID)).thenReturn(boats);
		when(mockRaceService.getRacesInCompetition(mockCompetition)).thenReturn(races);
		when(mockRace.getId()).thenReturn(testRaceID);
		when(mockRace.getRaceStatus()).thenReturn(RaceStatus.COMPLETED);
		when(mockResultService.findAll(testRaceID)).thenReturn(results);
    }

    private void setupCompetition()
            throws NoSuchCompetition, NoSuchUser, UserNotPermitted
    {
        when(mockCompetitionService.getCompetitionByID(testCompetitionID)).thenReturn(mockCompetition);
		when(mockCompetition.getFleet()).thenReturn(mockFleet);
		when(mockCompetition.getResultType()).thenReturn(ResultType.SCRATCH_RESULT);
		when(mockCompetition.getPointsSystem()).thenReturn(PointsSystem.LOW_POINTS);
		when(mockCompetition.getName()).thenReturn(TEST_COMPETITION_NAME);
    }

    private void checkPoints(PointsTable pointsTable, Boat boat, float points)
    {
        PointsForBoat boatPoints = findBoatPoints(boat,pointsTable);
		assertEquals(1,boatPoints.getPoints().size());
		assertEquals(new Float(points),boatPoints.getPoints().get(0));
    }

	private PointsForBoat findBoatPoints(Boat boat, PointsTable pointsTable)
    {
	    for(PointsForBoat points : pointsTable.getPointsForBoat())
	    {
	        if ( points.getBoat() == boat )
	        {
	            return points;
	        }
	    }
	    
        return null;
    }

    private List<RaceResult> raceResults = new ArrayList<RaceResult>();
	private List<Race> races = new ArrayList<Race>();
	private List<Race> racesWithIncompleteEntry = new ArrayList<Race>();
	private List<Boat> boats = new ArrayList<Boat>();

	private static final Integer testRaceSeriesID = 123;
	private static final Integer testCompetitionID = 345;
	private static final Integer testFleetID = 69;
	private static final Integer testRaceID = 96;

	@Mock private Race mockRace;
	@Mock private Race mockIncompleteRace;
	@Mock private Competition mockCompetition;
	@Mock private Fleet mockFleet;

	private BoatClass testClass = new BoatClass(null, 123, "Laser", 123.0f);

	private Boat testBoat1 = new Boat(null, "FredTheWinner", "1234", testClass, null, false, "", "");
	private Boat testBoat2 = new Boat(null, "CloseBehind", "1234", testClass, null, false, "", "");
	private Boat testBoat3 = new Boat(null, "IkeepTheTail", "1234", testClass, null, false, "", "");

	private RaceResult resultBoat1 = new RaceResult(
	        null, testBoat1, 1.0f, false, new Date(), new Date(), ResultStatus.FINISHED, 0, 0, 1, 1);
	private RaceResult resultBoat2 = new RaceResult(
	        null, testBoat2, 1.0f, false, new Date(), new Date(), ResultStatus.FINISHED, 0, 0, 2, 2);
	private RaceResult resultBoat3 = new RaceResult(
	        null, testBoat3, 1.0f, false, new Date(), new Date(), ResultStatus.FINISHED, 0, 0, 3, 3);

	private static final String POINTS_TABLE_NAME = "Points table"; 
	private static final String TEST_COMPETITION_NAME = "Laser season point score";
	
	@Mock private RaceService mockRaceService;
	@Mock private CompetitionService mockCompetitionService;
	@Mock private BoatService mockBoatService;
	@Mock private RaceResultService mockResultService;
	@Mock private PointsCalculator mockPointsCalculator;
	@Mock private RaceResultSorter mockRaceResultSorter;
	@Mock private PointsTotalCalculator mockPointsTotalCalculator;
	@Mock private PointsSorter mockPointsSorter;
	@Mock private AuditService mockAudit;
	
	@InjectMocks
	private PointsService fixture = new PointsServiceImpl();
}
