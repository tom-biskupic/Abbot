package com.runcible.abbot.service;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.runcible.abbot.model.Competition;
import com.runcible.abbot.model.CompetitionExportConfigItem;
import com.runcible.abbot.model.ExportConfig;
import com.runcible.abbot.model.ExportConfigItem;
import com.runcible.abbot.model.FleetExportConfigItem;
import com.runcible.abbot.model.Fleet;
import com.runcible.abbot.model.PointsTable;
import com.runcible.abbot.model.Race;
import com.runcible.abbot.model.RaceStatus;
import com.runcible.abbot.service.audit.AuditEventType;
import com.runcible.abbot.service.audit.AuditService;
import com.runcible.abbot.service.exceptions.NoSuchCompetition;
import com.runcible.abbot.service.exceptions.NoSuchFleet;
import com.runcible.abbot.service.exceptions.NoSuchUser;
import com.runcible.abbot.service.exceptions.UserNotPermitted;

@ExtendWith(MockitoExtension.class)
public class ExportServiceTest
{
    @Test
    public void testExportAll_competitionItem_includesCompetitionHTML()
            throws NoSuchCompetition, NoSuchUser, UserNotPermitted, NoSuchFleet
    {
        setupCompetitionMocks();

        String result = fixture.exportAll(
                RACE_SERIES_ID,
                configWith(new CompetitionExportConfigItem(COMPETITION_ID)));

        assertTrue(result.contains("<h1>" + COMPETITION_NAME + "</h1>"));
        verify(mockAudit).auditEvent(AuditEventType.EXPORTED, RACE_SERIES_ID, "Competition", COMPETITION_NAME);
    }

    @Test
    public void testExportAll_fleetItem_includesFleetHeadingAndRaceHTML()
            throws NoSuchCompetition, NoSuchUser, UserNotPermitted, NoSuchFleet
    {
        setupFleetMocks();

        String result = fixture.exportAll(
                RACE_SERIES_ID,
                configWith(new FleetExportConfigItem(FLEET_ID)));

        assertTrue(result.contains("<h2>" + FLEET_NAME + "</h2>"));
        assertTrue(result.contains("<h3>" + RACE_NAME));
        verify(mockAudit).auditEvent(AuditEventType.EXPORTED, RACE_SERIES_ID, "Races", FLEET_NAME);
    }

    @Test
    public void testExportAll_mixedItems_combinesOutputInOrder()
            throws NoSuchCompetition, NoSuchUser, UserNotPermitted, NoSuchFleet
    {
        setupCompetitionMocks();
        setupFleetMocks();

        String result = fixture.exportAll(
                RACE_SERIES_ID,
                configWith(
                        new CompetitionExportConfigItem(COMPETITION_ID),
                        new FleetExportConfigItem(FLEET_ID)));

        assertTrue(result.contains("<h1>" + COMPETITION_NAME + "</h1>"));
        assertTrue(result.contains("<h2>" + FLEET_NAME + "</h2>"));
        assertTrue(result.indexOf("<h1>") < result.indexOf("<h2>"),
                "Competition section should precede fleet section");
    }

    @Test
    public void testExportAll_emptyList_returnsEmptyString()
            throws NoSuchCompetition, NoSuchUser, UserNotPermitted, NoSuchFleet
    {
        String result = fixture.exportAll(RACE_SERIES_ID, configWith());
        assertTrue(result.isEmpty());
    }

    private ExportConfig configWith(ExportConfigItem... items)
    {
        return new ExportConfig("test", RACE_SERIES_ID, "results-table", List.of(items));
    }

    private void setupCompetitionMocks()
            throws NoSuchCompetition, NoSuchUser, UserNotPermitted, NoSuchFleet
    {
        PointsTable pointsTable = new PointsTable(mockCompetition);
        when(mockPointsService.generatePointsTable(RACE_SERIES_ID, COMPETITION_ID)).thenReturn(pointsTable);
        when(mockCompetition.getName()).thenReturn(COMPETITION_NAME);
    }

    private void setupFleetMocks() throws NoSuchUser, UserNotPermitted, NoSuchFleet
    {
        when(mockFleetService.getFleetByID(FLEET_ID)).thenReturn(mockFleet);
        when(mockFleet.getFleetName()).thenReturn(FLEET_NAME);
        when(mockFleet.getId()).thenReturn(FLEET_ID);
        when(mockRaceService.getRacesForFleet(RACE_SERIES_ID, FLEET_ID)).thenReturn(List.of(mockRace));
        when(mockRace.getId()).thenReturn(RACE_ID);
        when(mockRaceResultService.findAll(RACE_ID)).thenReturn(List.of());
        when(mockRace.getRaceStatus()).thenReturn(RaceStatus.COMPLETED);
        when(mockRace.getName()).thenReturn(RACE_NAME);
        when(mockRace.getRaceDate()).thenReturn(new Date());
    }

    private static final Integer RACE_SERIES_ID   = 1;
    private static final Integer COMPETITION_ID   = 2;
    private static final Integer FLEET_ID         = 3;
    private static final Integer RACE_ID          = 4;
    private static final String  COMPETITION_NAME = "Laser Championship";
    private static final String  FLEET_NAME       = "Laser Fleet";
    private static final String  RACE_NAME        = "Race 1";

    @Mock private Competition          mockCompetition;
    @Mock private Fleet                mockFleet;
    @Mock private Race                 mockRace;
    @Mock private PointsService        mockPointsService;
    @Mock private RaceService          mockRaceService;
    @Mock private RaceResultService    mockRaceResultService;
    @Mock private FleetService         mockFleetService;
    @Mock private AuditService         mockAudit;
    @Mock private HandicapTableService mockHandicapTableService;

    @InjectMocks
    private ExportServiceImpl fixture;
}
