package com.runcible.abbot.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Collection;
import java.util.Iterator;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.runcible.abbot.model.User;

@RunWith(MockitoJUnitRunner.class)
public class AuthenticationServiceTest
{

    @Test
    public void testAuthenticate()
    {
        when(authenticationMock.getName()).thenReturn(TEST_EMAIL);
        when(authenticationMock.getCredentials()).thenReturn(TEST_CREDENTIALS);
        when(userServiceMock.validateLogon(TEST_EMAIL, TEST_CREDENTIALS)).thenReturn(userMock);
        when(userMock.isAdministrator()).thenReturn(false);
        
        Authentication returnValue = fixture.authenticate(authenticationMock);
        
        checkAuthenticationToken(returnValue, false);
    }

    @Test(expected=BadCredentialsException.class)
    public void testAuthenticateInvalidUser()
    {
        when(authenticationMock.getName()).thenReturn(TEST_EMAIL);
        when(authenticationMock.getCredentials()).thenReturn(TEST_CREDENTIALS);
        when(userServiceMock.validateLogon(TEST_EMAIL, TEST_CREDENTIALS)).thenReturn(null);
        fixture.authenticate(authenticationMock);
    }

    @Test
    public void testAuthenticateAdmin()
    {
        when(authenticationMock.getName()).thenReturn(TEST_EMAIL);
        when(authenticationMock.getCredentials()).thenReturn(TEST_CREDENTIALS);
        when(userServiceMock.validateLogon(TEST_EMAIL, TEST_CREDENTIALS)).thenReturn(userMock);
        when(userMock.isAdministrator()).thenReturn(true);
        
        Authentication returnValue = fixture.authenticate(authenticationMock);
        
        checkAuthenticationToken(returnValue, true);
    }

    private void checkAuthenticationToken(Authentication returnValue, boolean isAdministrator)
    {
        UsernamePasswordAuthenticationToken asUserNamePasswordToken = (UsernamePasswordAuthenticationToken)returnValue;
        Collection<GrantedAuthority> authorities = asUserNamePasswordToken.getAuthorities();
        assertEquals(isAdministrator?2:1,authorities.size());
        
        Iterator<GrantedAuthority> authoritiesIter = authorities.iterator();
        
        if ( isAdministrator )
        {
            SimpleGrantedAuthority adminAuthority = (SimpleGrantedAuthority)authoritiesIter.next();
            assertEquals(ADMIN_ROLE,adminAuthority.getAuthority());
        }

        SimpleGrantedAuthority userAuthority = (SimpleGrantedAuthority)authoritiesIter.next();
        assertEquals(USER_ROLE,userAuthority.getAuthority());
        
    }

    private static final String TEST_CREDENTIALS = "Password01!";
    private static final String TEST_EMAIL = "fred@nowhere.com";
    private static final String USER_ROLE = "ROLE_USER";
    private static final String ADMIN_ROLE = "ROLE_ADMIN";
    
    @Mock private UserService       userServiceMock;
    @Mock private Authentication    authenticationMock;
    @Mock private User              userMock;
    
    @InjectMocks
    private AuthenticationService fixture = new AuthenticationService();
}
