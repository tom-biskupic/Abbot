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

import com.runcible.abbot.model.RaceSeries;
import com.runcible.abbot.model.User;
import com.runcible.abbot.repository.RaceSeriesRepository;
import com.runcible.abbot.service.audit.AuditEventType;
import com.runcible.abbot.service.audit.AuditService;
import com.runcible.abbot.service.exceptions.NoSuchUser;
import com.runcible.abbot.service.exceptions.UserNotPermitted;

@RunWith(MockitoJUnitRunner.class)
public class RaceSeriesServiceTest 
{
    @Test
    public void testAddSeries() throws NoSuchUser, UserNotPermitted
    {
        when(loggedOnUserServiceMock.getLoggedOnUser()).thenReturn(userMock);
        when(raceSeriesRepoMock.save(raceSeriesMock)).thenReturn(raceSeriesMock);
        when(raceSeriesMock.getName()).thenReturn(TEST_SERIES_NAME);
        fixture.add(raceSeriesMock);
        verify(raceSeriesAuthorizationServiceMock).authorizeUserForRaceSeries(raceSeriesMock, userMock);
        auditEvent(AuditEventType.CREATED);
    }

    @Test
	public void testFindAll()
	{
		when(raceSeriesRepoMock.findByPermittedUsers(TEST_USER_ID, pageableMock)).thenReturn(raceSeriesPageMock);
		when(userMock.getId()).thenReturn(TEST_USER_ID);
		assertEquals(raceSeriesPageMock,fixture.findAll(pageableMock, userMock));
	}

	@Test 
	public void testFindById() throws NoSuchUser, UserNotPermitted
	{
	    setupCheckPermissionsMocks(true);
	    
		when(raceSeriesRepoMock.findOne(TEST_ID)).thenReturn(raceSeriesMock);
		assertEquals(raceSeriesMock,fixture.findByID(TEST_ID));
	}

	@Test
	public void testUpdateRaceSeries() throws NoSuchUser, UserNotPermitted
	{
        setupCheckPermissionsMocks(true);
        when(raceSeriesMock.getId()).thenReturn(TEST_ID);
        when(raceSeriesMock.getName()).thenReturn(TEST_SERIES_NAME);
	    fixture.update(raceSeriesMock);
	    verify(raceSeriesRepoMock).save(raceSeriesMock);
	    auditEvent(AuditEventType.UPDATED);
	}

    @Test(expected=UserNotPermitted.class)
    public void testUpdateRaceSeriesNotPermitted() throws NoSuchUser, UserNotPermitted
    {
        setupCheckPermissionsMocks(false);
        when(raceSeriesMock.getId()).thenReturn(TEST_ID);
        fixture.update(raceSeriesMock);
    }
	
 
	@Test(expected=UserNotPermitted.class) 
    public void testFindByIdNotPermitted() throws NoSuchUser, UserNotPermitted
    {
        setupCheckPermissionsMocks(false);
        
        when(raceSeriesRepoMock.findOne(TEST_ID)).thenReturn(raceSeriesMock);
        assertEquals(raceSeriesMock,fixture.findByID(TEST_ID));
    }

	
    private void setupCheckPermissionsMocks(boolean permitted) throws NoSuchUser
    {
        when(raceSeriesAuthorizationServiceMock.isLoggedOnUserPermitted(TEST_ID)).thenReturn(permitted);
    }

    private void auditEvent(AuditEventType evenType)
            throws NoSuchUser, UserNotPermitted
    {
        verify(auditMock).auditEvent(evenType, RACE_SERIES_OBJECT_NAME, TEST_SERIES_NAME);
    }

    private static final Integer   TEST_ID=1234;
	private static final Integer   TEST_USER_ID=3456;
	private static final String	   TEST_NAME="Radial";
	private static final String    RACE_SERIES_OBJECT_NAME = "Race series";
	private static final String    TEST_SERIES_NAME = "2017/2017 Season";
	
	@Mock private RaceSeriesRepository 	           raceSeriesRepoMock;
    @Mock private LoggedOnUserService 	           loggedOnUserServiceMock;
    @Mock private Pageable                         pageableMock;
    @Mock private User 					           userMock;
    @Mock private Page<RaceSeries> 		           raceSeriesPageMock;
	@Mock private RaceSeries 			           raceSeriesMock;
	@Mock private RaceSeriesAuthorizationService   raceSeriesAuthorizationServiceMock;
	@Mock private AuditService                     auditMock;
    
	@InjectMocks
    RaceSeriesServiceImpl fixture;
}
