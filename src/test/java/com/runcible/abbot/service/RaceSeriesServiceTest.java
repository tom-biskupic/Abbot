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
	    
		when(raceSeriesRepoMock.findOne(TEST_ID)).thenReturn(raceSeriesMock);
		assertEquals(raceSeriesMock,fixture.findByID(TEST_ID));
	}

	@Test
	public void testUpdateRaceSeries() throws NoSuchUser, UserNotPermitted
	{
        setupCheckPermissionsMocks(true);
        when(raceSeriesMock.getId()).thenReturn(TEST_ID);
	    fixture.update(raceSeriesMock);
	    verify(raceSeriesRepoMock).save(raceSeriesMock);
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
    
	public static final Integer TEST_ID=1234;
	public static final Integer TEST_USER_ID=3456;
	public static final String	TEST_NAME="Radial";
	
	@Mock private RaceSeriesRepository 	raceSeriesRepoMock;
    @Mock private LoggedOnUserService 	loggedOnUserServiceMock;
    @Mock private Pageable 				pageableMock;
    @Mock private User 					userMock;
    @Mock private Page<RaceSeries> 		raceSeriesPageMock;
	@Mock private RaceSeries 			raceSeriesMock;
	@Mock private RaceSeriesAuthorizationService raceSeriesAuthorizationServiceMock;
	
    @InjectMocks
    RaceSeriesServiceImpl fixture;
}
