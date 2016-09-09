package com.runcible.abbot.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.runcible.abbot.model.RaceSeries;
import com.runcible.abbot.model.RaceSeriesUser;
import com.runcible.abbot.model.User;
import com.runcible.abbot.model.UserSummary;
import com.runcible.abbot.repository.RaceSeriesUserRepository;
import com.runcible.abbot.service.exceptions.CannotDeAuthorizeLastUser;
import com.runcible.abbot.service.exceptions.NoSuchRaceSeries;
import com.runcible.abbot.service.exceptions.NoSuchUser;
import com.runcible.abbot.service.exceptions.UserNotPermitted;

@RunWith(MockitoJUnitRunner.class)
public class RaceAuthorizationServiceTest
{

    @Test
    public void testIsLoggedOnUserPermitted() throws NoSuchUser
    {
        setupIsUserPermttedMocks(true);
        assertEquals(true, fixture.isLoggedOnUserPermitted(TEST_RACE_SERIES_ID));
    }

    @Test
    public void testIsLoggedOnUserPermittedNotPermitted() throws NoSuchUser
    {
        setupIsUserPermttedMocks(false);
        assertEquals(false, fixture.isLoggedOnUserPermitted(TEST_RACE_SERIES_ID));
    }

    private void setupIsUserPermttedMocks(boolean permitted) throws NoSuchUser
    {
        when(loggedOnUserServiceMock.getLoggedOnUser()).thenReturn(userMock);
        when(userMock.getId()).thenReturn(TEST_USER_ID);
        when(raceSeriesUserRepoMock.countUserEntriesForRaceSeries(TEST_RACE_SERIES_ID, TEST_USER_ID))
                .thenReturn(permitted ? 1L : 0L);
    }

    @Test
    public void testAuthorizeUserForRaceSeries() throws UserNotPermitted, NoSuchUser
    {
        when(seriesMock.getId()).thenReturn(TEST_RACE_SERIES_ID);
        when(raceSeriesUserRepoMock.countUserEntriesForRaceSeries(TEST_RACE_SERIES_ID)).thenReturn(1L);

        setupIsUserPermttedMocks(true);

        fixture.authorizeUserForRaceSeries(seriesMock, userMock);
        verifyRaceSeriesUserAdded();
    }

    @Test
    public void testAuthorizeUserForRaceSeriesUsingRaceSeriesId() throws UserNotPermitted, NoSuchUser, NoSuchRaceSeries
    {
        when(raceSeriesServiceMock.findByID(TEST_RACE_SERIES_ID)).thenReturn(seriesMock);
        when(userServiceMock.findByEmail(TEST_EMAIL1)).thenReturn(userMock);
        when(seriesMock.getId()).thenReturn(TEST_RACE_SERIES_ID);
        when(raceSeriesUserRepoMock.countUserEntriesForRaceSeries(TEST_RACE_SERIES_ID)).thenReturn(1L);

        setupIsUserPermttedMocks(true);

        fixture.authorizeUserForRaceSeries(TEST_RACE_SERIES_ID, TEST_EMAIL1);
        verifyRaceSeriesUserAdded();
    }

    @Test
    public void testAuthorizeUserForRaceSeriesNewSeries() throws UserNotPermitted, NoSuchUser
    {
        when(seriesMock.getId()).thenReturn(TEST_RACE_SERIES_ID);
        when(raceSeriesUserRepoMock.countUserEntriesForRaceSeries(TEST_RACE_SERIES_ID)).thenReturn(0L);

        fixture.authorizeUserForRaceSeries(seriesMock, userMock);
        verifyRaceSeriesUserAdded();
    }

    @Test(expected = UserNotPermitted.class)
    public void testAuthorizeUserForRaceSeriesUserNotPermitted() throws UserNotPermitted, NoSuchUser
    {
        when(seriesMock.getId()).thenReturn(TEST_RACE_SERIES_ID);
        when(raceSeriesUserRepoMock.countUserEntriesForRaceSeries(TEST_RACE_SERIES_ID)).thenReturn(1L);

        setupIsUserPermttedMocks(false);

        fixture.authorizeUserForRaceSeries(seriesMock, userMock);
    }

    @Test
    public void testGetAuthorizedUsers() throws NoSuchUser, UserNotPermitted
    {
        setupIsUserPermttedMocks(true);

        List<RaceSeriesUser> userList = Arrays.asList(new RaceSeriesUser(seriesMock, testUser1),
                new RaceSeriesUser(seriesMock, testUser2));

        when(raceSeriesUserRepoMock.getByRaceSeriesId(TEST_RACE_SERIES_ID, pageableMock)).thenReturn(userPageMock);
        when(userPageMock.getContent()).thenReturn(userList);
        when(userPageMock.getTotalPages()).thenReturn(1);
        when(userPageMock.getNumber()).thenReturn(0);
        when(userPageMock.getSize()).thenReturn(5);
        
        Page<UserSummary> returned = fixture.getAuthorizedUsers(TEST_RACE_SERIES_ID, pageableMock);
        assertEquals(2, returned.getNumberOfElements());
        assertEquals(1,returned.getTotalPages());
        assertEquals(5,returned.getSize());
        assertEquals(0,returned.getNumber());
        
        assertEquals(TEST_EMAIL1, returned.getContent().get(0).getEmailAddress());
        assertEquals(TEST_FIRST_NAME1 + " "+TEST_LAST_NAME1,returned.getContent().get(0).getName());
        assertEquals(true,returned.getContent().get(0).isCurrentUser());
        
        assertEquals(TEST_EMAIL2,returned.getContent().get(1).getEmailAddress());
        assertEquals(TEST_FIRST_NAME2 + " "+TEST_LAST_NAME2,returned.getContent().get(1).getName());
        assertEquals(false,returned.getContent().get(1).isCurrentUser());
    }

     @Test(expected=UserNotPermitted.class)
     public void testGetAuthorizedUsersUserNotPermitted() throws NoSuchUser, UserNotPermitted
     {
         setupIsUserPermttedMocks(false);
    
         fixture.getAuthorizedUsers(TEST_RACE_SERIES_ID,pageableMock);
     }

    @Test
    public void testDeAuthorizeUserForRaceSeries() throws NoSuchUser, UserNotPermitted, CannotDeAuthorizeLastUser
    {
        setupIsUserPermttedMocks(true);

        when(raceSeriesUserRepoMock.countUserEntriesForRaceSeries(TEST_RACE_SERIES_ID)).thenReturn(2L);
        fixture.deAuthorizeUserForRaceSeries(TEST_RACE_SERIES_ID, TEST_USER_ID);
        verify(raceSeriesUserRepoMock).removeUser(TEST_RACE_SERIES_ID, TEST_USER_ID);
    }

    @Test(expected = CannotDeAuthorizeLastUser.class)
    public void testDeAuthorizeUserForRaceSeriesCannotDeAuthorizeLastUser()
            throws NoSuchUser, UserNotPermitted, CannotDeAuthorizeLastUser
    {
        setupIsUserPermttedMocks(true);

        when(raceSeriesUserRepoMock.countUserEntriesForRaceSeries(TEST_RACE_SERIES_ID)).thenReturn(1L);
        fixture.deAuthorizeUserForRaceSeries(TEST_RACE_SERIES_ID, TEST_USER_ID);
    }

    @Test(expected = UserNotPermitted.class)
    public void testDeAuthorizeUserForRaceSeriesUserNotPermitted()
            throws NoSuchUser, UserNotPermitted, CannotDeAuthorizeLastUser
    {
        setupIsUserPermttedMocks(false);

        when(raceSeriesUserRepoMock.countUserEntriesForRaceSeries(TEST_RACE_SERIES_ID)).thenReturn(2L);
        fixture.deAuthorizeUserForRaceSeries(TEST_RACE_SERIES_ID, TEST_USER_ID);
    }

    private void verifyRaceSeriesUserAdded()
    {
        ArgumentCaptor<RaceSeriesUser> captor = ArgumentCaptor.forClass(RaceSeriesUser.class);
        verify(raceSeriesUserRepoMock).save(captor.capture());
        RaceSeriesUser raceSeriesUser = captor.getValue();
        assertEquals(seriesMock, raceSeriesUser.getRaceSeries());
        assertEquals(userMock, raceSeriesUser.getUser());
    }

    private static final Integer TEST_RACE_SERIES_ID = 1234;
    private static final Integer TEST_USER_ID = 333;
    private static final String TEST_EMAIL1 = "email1@nowhere.com";
    private static final String TEST_EMAIL2 = "email2@nowhere.com";
    private static final String TEST_FIRST_NAME1 = "Fred";
    private static final String TEST_FIRST_NAME2 = "Wilma";
    private static final String TEST_LAST_NAME1 = "Flintstone";
    private static final String TEST_LAST_NAME2 = "Flintstone";

    private static final User testUser1 = new User(TEST_USER_ID,TEST_EMAIL1, "", TEST_FIRST_NAME1, TEST_LAST_NAME1, "", true);
    private static final User testUser2 = new User(6969,TEST_EMAIL2, "", TEST_FIRST_NAME2, TEST_LAST_NAME2, "", true);

    @Mock private RaceSeriesUserRepository raceSeriesUserRepoMock;
    
    @Mock private LoggedOnUserService   loggedOnUserServiceMock;
    @Mock private User                  userMock;
    @Mock private User                  userMock2;
    @Mock private RaceSeries            seriesMock;
    @Mock private Pageable              pageableMock;
    @Mock private Page<RaceSeriesUser>  userPageMock;
    @Mock private RaceSeriesService     raceSeriesServiceMock;
    @Mock private UserService           userServiceMock;
    
    @InjectMocks
    private RaceSeriesAuthorizationServiceImpl fixture;
}
