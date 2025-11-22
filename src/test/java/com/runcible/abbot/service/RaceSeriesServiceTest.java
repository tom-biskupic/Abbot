package com.runcible.abbot.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.runcible.abbot.model.RaceSeries;
import com.runcible.abbot.model.User;
import com.runcible.abbot.repository.RaceSeriesRepository;
import com.runcible.abbot.service.audit.AuditService;
import com.runcible.abbot.service.exceptions.NoSuchUser;
import com.runcible.abbot.service.exceptions.UserNotPermitted;

@ExtendWith(MockitoExtension.class)
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
	    
		when(raceSeriesRepoMock.findById(TEST_ID)).thenReturn(Optional.of(raceSeriesMock));
		assertEquals(raceSeriesMock,fixture.findByID(TEST_ID));
	}

	@Test
	public void testUpdateRaceSeries() throws NoSuchUser, UserNotPermitted
	{
        setupCheckPermissionsMocks(true);
        when(loggedOnUserServiceMock.getLoggedOnUser()).thenReturn(userMock);
        when(raceSeriesMock.getId()).thenReturn(TEST_ID);
        when(raceSeriesMock.getName()).thenReturn(TEST_SERIES_NAME);
	    fixture.update(raceSeriesMock);
	    verify(raceSeriesRepoMock).save(raceSeriesMock);
	}

    @Test
    public void testUpdateRaceSeriesNotPermitted() throws NoSuchUser, UserNotPermitted
    {
        setupCheckPermissionsMocks(false);
        when(raceSeriesMock.getId()).thenReturn(TEST_ID);
        Assertions.assertThrows(UserNotPermitted.class, () -> {
            fixture.update(raceSeriesMock);
        });
    }
	
 
	@Test
    public void testFindByIdNotPermitted() throws NoSuchUser, UserNotPermitted
    {
        setupCheckPermissionsMocks(false);
        
        when(raceSeriesRepoMock.findById(TEST_ID)).thenReturn(Optional.of(raceSeriesMock));
        Assertions.assertThrows(UserNotPermitted.class, () -> {
            fixture.findByID(TEST_ID);
        });
    }

	
    private void setupCheckPermissionsMocks(boolean permitted) throws NoSuchUser
    {
        when(raceSeriesAuthorizationServiceMock.isLoggedOnUserPermitted(TEST_ID)).thenReturn(permitted);
    }

    private static final Integer   TEST_ID=1234;
	private static final Integer   TEST_USER_ID=3456;
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
