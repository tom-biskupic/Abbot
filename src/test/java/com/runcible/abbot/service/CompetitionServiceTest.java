package com.runcible.abbot.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.runcible.abbot.model.Competition;
import com.runcible.abbot.model.RaceSeries;
import com.runcible.abbot.repository.CompetitionRepository;
import com.runcible.abbot.service.audit.AuditEventType;
import com.runcible.abbot.service.audit.AuditService;
import com.runcible.abbot.service.exceptions.NoSuchCompetition;
import com.runcible.abbot.service.exceptions.NoSuchRaceSeries;
import com.runcible.abbot.service.exceptions.NoSuchUser;
import com.runcible.abbot.service.exceptions.UserNotPermitted;

@RunWith(MockitoJUnitRunner.class)
public class CompetitionServiceTest
{
    @Test
    public void testGetAllCompetitionsForSeries() throws NoSuchUser, UserNotPermitted
    {
        setupCheckPermissionsMocks(true);
        
        when(competitionRepoMock.findByRaceSeries(TEST_RACE_SERIES_ID, pageableMock)).thenReturn(competitionPageMock);
        Page<Competition> returned = fixture.getAllCompetitionsForSeries(TEST_RACE_SERIES_ID, pageableMock);
        assertEquals(competitionPageMock,returned);
    }

    @Test(expected=UserNotPermitted.class)
    public void testGetAllCompetitionsForSeriesUserNotPermitted() throws NoSuchUser, UserNotPermitted
    {
        setupCheckPermissionsMocks(false);
        
        fixture.getAllCompetitionsForSeries(TEST_ID, pageableMock);
    }

    @Test
    public void testUpdateCompetition() throws NoSuchUser, UserNotPermitted
    {
        setupCheckPermissionsMocks(true);
        when(competitionMock.getRaceSeriesId()).thenReturn(TEST_RACE_SERIES_ID);
        when(competitionMock.getName()).thenReturn(TEST_COMPETITION_NAME);
        
        fixture.updateCompetition(competitionMock);
        verify(competitionRepoMock).save(competitionMock);
        
        verifyAuditEvent(AuditEventType.UPDATED);
    }

    @Test(expected=UserNotPermitted.class)
    public void testUpdateCompetitionUserNotPermitted() throws NoSuchUser, UserNotPermitted
    {
        setupCheckPermissionsMocks(false);
        when(competitionMock.getRaceSeriesId()).thenReturn(TEST_RACE_SERIES_ID);
        
        fixture.updateCompetition(competitionMock);
    }

    @Test
    public void testAddCompetition() throws NoSuchRaceSeries, NoSuchUser, UserNotPermitted
    {
        setupCheckPermissionsMocks(true);
        
        when(competitionMock.getName()).thenReturn(TEST_COMPETITION_NAME);
        when(competitionMock.getRaceSeriesId()).thenReturn(TEST_RACE_SERIES_ID);
        
        fixture.addCompetition(TEST_RACE_SERIES_ID, competitionMock);
        verify(competitionRepoMock).save(competitionMock);
        
        verifyAuditEvent(AuditEventType.CREATED);
    }

    @Test(expected=UserNotPermitted.class)
    public void testAddCompetitionUserNotPermitted() throws NoSuchRaceSeries, NoSuchUser, UserNotPermitted
    {
        setupCheckPermissionsMocks(false);
        
        fixture.addCompetition(TEST_RACE_SERIES_ID, competitionMock);
    }
    
    @Test
    public void testGetCompetitionById() throws NoSuchCompetition, NoSuchUser, UserNotPermitted
    {
        setupCheckPermissionsMocks(true);

        when(competitionRepoMock.findOne(TEST_COMPETITION_ID)).thenReturn(competitionMock);
        when(competitionMock.getRaceSeriesId()).thenReturn(TEST_RACE_SERIES_ID);
        
        Competition returned = fixture.getCompetitionByID(TEST_COMPETITION_ID);
        assertEquals(competitionMock,returned);
    }

    @Test(expected=NoSuchCompetition.class)
    public void testGetCompetitionByIdNoSuchCompetition() throws NoSuchCompetition, NoSuchUser, UserNotPermitted
    {
        when(competitionRepoMock.findOne(TEST_COMPETITION_ID)).thenReturn(null);
        
        fixture.getCompetitionByID(TEST_COMPETITION_ID);
    }

    @Test
    public void testRemoveCompetition() throws NoSuchCompetition, NoSuchUser, UserNotPermitted
    {
        setupCheckPermissionsMocks(true);

        when(competitionRepoMock.findOne(TEST_COMPETITION_ID)).thenReturn(competitionMock);
        when(competitionMock.getRaceSeriesId()).thenReturn(TEST_RACE_SERIES_ID);
        when(competitionMock.getName()).thenReturn(TEST_COMPETITION_NAME);
        
        fixture.removeCompetition(TEST_COMPETITION_ID);
        verify(competitionRepoMock).delete(TEST_COMPETITION_ID);
        
        verifyAuditEvent(AuditEventType.DELETED);
    }

    @Test(expected=NoSuchCompetition.class)
    public void testRemoveCompetitionNoSuchCompetition() throws NoSuchCompetition, NoSuchUser, UserNotPermitted
    {
        setupCheckPermissionsMocks(true);
        when(competitionRepoMock.findOne(TEST_COMPETITION_ID)).thenReturn(null);
        fixture.removeCompetition(TEST_COMPETITION_ID);
    }

    @Test(expected=UserNotPermitted.class)
    public void testRemoveCompetitionUserNotPermitted() throws NoSuchCompetition, NoSuchUser, UserNotPermitted
    {
        setupCheckPermissionsMocks(false);

        when(competitionRepoMock.findOne(TEST_COMPETITION_ID)).thenReturn(competitionMock);
        when(competitionMock.getRaceSeriesId()).thenReturn(TEST_RACE_SERIES_ID);
        
        fixture.removeCompetition(TEST_COMPETITION_ID);
    }
    
    private void setupCheckPermissionsMocks(boolean permitted) throws NoSuchUser
    {
        when(raceSeriesAuthorizationServiceMock.isLoggedOnUserPermitted(TEST_RACE_SERIES_ID)).thenReturn(permitted);
    }

    private void verifyAuditEvent(AuditEventType eventType)
            throws NoSuchUser, UserNotPermitted
    {
        verify(auditMock).auditEvent(
                eventType, 
                TEST_RACE_SERIES_ID, 
                COMPETITION_OBJECT_NAME, 
                TEST_COMPETITION_NAME);
    }


    public static final Integer TEST_ID=1234;
    public static final Integer TEST_COMPETITION_ID=1111;
    public static final Integer TEST_RACE_SERIES_ID=9999;
    public static final String  COMPETITION_OBJECT_NAME = "Competition"; 
    public static final String  TEST_COMPETITION_NAME = "Laser Series Pointscore";
    
    @Mock private RaceSeriesService                 raceSeriesServiceMock;
    @Mock private CompetitionRepository             competitionRepoMock;
    @Mock private RaceSeriesAuthorizationService    raceSeriesAuthorizationServiceMock;
    @Mock private Pageable                          pageableMock;
    @Mock private Page<Competition>                 competitionPageMock;
    @Mock private Competition                       competitionMock;
    @Mock private RaceSeries                        raceSeriesMock;
    @Mock private AuditService                      auditMock;
    
    @InjectMocks
    private CompetitionServiceImpl fixture;
}
