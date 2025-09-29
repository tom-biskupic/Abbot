package com.runcible.abbot.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.runcible.abbot.model.User;
import com.runcible.abbot.repository.UserRepository;
import com.runcible.abbot.service.exceptions.DuplicateUserException;
import com.runcible.abbot.service.exceptions.NoSuchUser;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest
{
    @Test
    public void testFindAll()
    {
        when(mockUserRepo.findAll(mockPage)).thenReturn(mockUsers);
        
        Page<User> users = fixture.findAll(mockPage);        
        assertEquals(users,mockUsers);
    }
    
    @Test
    public void testValidateLogon()
    {
        when(mockUserRepo.findByEmailAndPassword(testEmail, testPassword)).thenReturn(mockUser);
        
        User user = fixture.validateLogon(testEmail,testPassword);
        assertEquals(user,mockUser);
    }
    
    @Test
    public void testAddUser() throws DuplicateUserException
    {
        when(mockUser.getEmail()).thenReturn(testEmail);
        when(mockUserRepo.findByEmail(testEmail)).thenReturn(null);
        when(mockUserRepo.count()).thenReturn(1L);
        
        fixture.addUser(mockUser);
        
        verify(mockUserRepo).save(mockUser);
    }

    @Test
    public void testAddFirstUser() throws DuplicateUserException
    {
        when(mockUser.getEmail()).thenReturn(testEmail);
        when(mockUserRepo.findByEmail(testEmail)).thenReturn(null);
        when(mockUserRepo.count()).thenReturn(0L);
        
        fixture.addUser(mockUser);
        
        verify(mockUserRepo).save(mockUser);
        verify(mockUser).setAdministrator(true);
    }

    @Test
    public void testAddUserDuplicate() throws DuplicateUserException
    {
        when(mockUser.getEmail()).thenReturn(testEmail);
        when(mockUserRepo.findByEmail(testEmail)).thenReturn(mockUser);
        Assertions.assertThrows(DuplicateUserException.class, () -> {
            fixture.addUser(mockUser);
        });
    }
    
    @Test
    public void testUpdateUser() throws DuplicateUserException
    {
        when(mockUser.getEmail()).thenReturn(testEmail);
        when(mockUserRepo.findByEmail(testEmail)).thenReturn(mockUser);
        when(mockUser.getId()).thenReturn(testID);
        fixture.updateUser(mockUser);
    }

    @Test
    public void testUpdateUserEmailChanged() throws DuplicateUserException
    {
        when(mockUser.getEmail()).thenReturn(testEmail);
        when(mockUserRepo.findByEmail(testEmail)).thenReturn(null);
        fixture.updateUser(mockUser);
    }

    @Test
    public void testUpdateUserEmailDuplicate() throws DuplicateUserException
    {
        when(mockUser.getEmail()).thenReturn(testEmail);
        when(mockUserRepo.findByEmail(testEmail)).thenReturn(mockExistingUser);
        when(mockExistingUser.getId()).thenReturn(testID2);
        when(mockUser.getId()).thenReturn(testID);
        Assertions.assertThrows(DuplicateUserException.class, () -> {
            fixture.updateUser(mockUser);
        });
    }

    @Test
    public void testFindByID() throws NoSuchUser
    {
        when(mockUserRepo.findById(testID)).thenReturn(Optional.of(mockUser));
        User user = fixture.findByID(testID);
        assertEquals(user,mockUser);
    }

    @Test
    public void testFindByIDNoUser() throws NoSuchUser
    {
        when(mockUserRepo.findById(testID)).thenReturn(Optional.empty());
        Assertions.assertThrows(NoSuchUser.class, () -> {
            fixture.findByID(testID);
        });
    }

    @Test
    public void testFindByEmail() throws NoSuchUser
    {
        when(mockUserRepo.findByEmail(testEmail)).thenReturn(mockUser);
        User result = fixture.findByEmail(testEmail);
        assertEquals(mockUser,result);
    }

    @Test
    public void testFindByEmailNoSuchUser() throws NoSuchUser
    {
        when(mockUserRepo.findByEmail(testEmail)).thenReturn(null);
        Assertions.assertThrows(NoSuchUser.class, () -> {
            fixture.findByEmail(testEmail);
        });
    }

    @InjectMocks
    UserServiceImpl fixture;
    
    @Mock
    UserRepository mockUserRepo;
    
    @Mock
    Pageable mockPage;
    
    @Mock
    Page<User>  mockUsers;
    
    @Mock
    User    mockUser;
    
    @Mock
    User    mockExistingUser;
    
    private static final String testEmail = "user@nowhere.com";
    private static final String testPassword = "password";
    private static final Integer testID = 12345;
    private static final Integer testID2 = 3456;
}
