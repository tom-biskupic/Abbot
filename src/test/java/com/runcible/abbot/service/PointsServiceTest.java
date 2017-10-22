package com.runcible.abbot.service;

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
import com.runcible.abbot.model.PointsSystem;
import com.runcible.abbot.model.Race;
import com.runcible.abbot.model.RaceResult;
import com.runcible.abbot.model.ResultStatus;
import com.runcible.abbot.model.ResultType;
import com.runcible.abbot.service.exceptions.NoSuchCompetition;
import com.runcible.abbot.service.exceptions.NoSuchFleet;
import com.runcible.abbot.service.exceptions.NoSuchUser;
import com.runcible.abbot.service.exceptions.UserNotPermitted;

@RunWith(MockitoJUnitRunner.class)
public class PointsServiceTest
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
		races.add(mockRace);
		
		boats.add(testBoat1);
		boats.add(testBoat2);
		boats.add(testBoat3);
		boats.add(testNonFinisher1);
		boats.add(testNonFinisher2);
		boats.add(testNonStarter);
	}

	@Test
	public void testHandicapLowPoints() throws NoSuchCompetition, NoSuchUser, UserNotPermitted, NoSuchFleet
	{
		when(mockCompetitionService.getCompetitionByID(testCompetitionID)).thenReturn(mockCompetition);
		when(mockCompetition.getFleet()).thenReturn(mockFleet);
		when(mockCompetition.getResultType()).thenReturn(ResultType.SCRATCH_RESULT);
		when(mockCompetition.getPointsSystem()).thenReturn(PointsSystem.LOW_POINTS);
		when(mockFleet.getId()).thenReturn(testFleetID);
		
		when(mockBoatService.getAllBoatsInFleetForSeries(testRaceSeriesID, testFleetID)).thenReturn(boats);
		when(mockRaceService.getRacesInCompetition(mockCompetition)).thenReturn(races);
		when(mockRace.getId()).thenReturn(testRaceID);
		when(mockResultService.findAll(testRaceID)).thenReturn(raceResults);
		
		fixture.generatePointsTable(testRaceSeriesID, testCompetitionID);
	}

	private List<RaceResult> raceResults = new ArrayList<RaceResult>();
	private List<Race> races = new ArrayList<Race>();
	private List<Boat> boats = new ArrayList<Boat>();

	private static final Integer testRaceSeriesID = 123;
	private static final Integer testCompetitionID = 345;
	private static final Integer testFleetID = 69;
	private static final Integer testRaceID = 96;

	@Mock private Race mockRace;
	@Mock private Competition mockCompetition;
	@Mock private Fleet mockFleet;

	private BoatClass testClass = new BoatClass(null, 123, "Laser", 123.0f);

	private Boat testBoat1 = new Boat(null, "FredTheWinner", "1234", testClass, null, false, "", "");
	private Boat testBoat2 = new Boat(null, "CloseBehind", "1234", testClass, null, false, "", "");
	private Boat testBoat3 = new Boat(null, "IkeepTheTail", "1234", testClass, null, false, "", "");
	private Boat testNonStarter = new Boat(null, "NotHere", "1234", testClass, null, false, "", "");
	private Boat testNonFinisher1 = new Boat(null, "NotFinished", "1234", testClass, null, false, "", "");
	private Boat testNonFinisher2 = new Boat(null, "NotFinishedEither", "1234", testClass, null, false, "", "");

	private RaceResult resultBoat1 = new RaceResult(null, testBoat1, 1, new Date(), new Date(), ResultStatus.FINISHED,90,90);
	private RaceResult resultBoat2 = new RaceResult(null, testBoat1, 1, new Date(), new Date(), ResultStatus.FINISHED,100,85);
	private RaceResult resultBoat3 = new RaceResult(null, testBoat1, 1, new Date(), new Date(), ResultStatus.FINISHED,95,94);
	private RaceResult resultNonStarter1 = new RaceResult(null, testBoat1, 1, new Date(), new Date(), ResultStatus.DNS);
	private RaceResult resultNonStarter2 = new RaceResult(null, testBoat1, 1, new Date(), new Date(), ResultStatus.DNC);
	private RaceResult resultNonFinisher1 = new RaceResult(
				null, testBoat1, 1, new Date(), new Date(),ResultStatus.DNF);
	private RaceResult resultNonFinisher2 = new RaceResult(
			null, testBoat1, 1, new Date(), new Date(),ResultStatus.OCS);

	@Mock private RaceService mockRaceService;
	@Mock private CompetitionService mockCompetitionService;
	@Mock private BoatService mockBoatService;
	@Mock private RaceResultService mockResultService;

	@InjectMocks
	private PointsService fixture = new PointsServiceImpl();
}
