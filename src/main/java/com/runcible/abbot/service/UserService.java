package com.runcible.abbot.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.runcible.abbot.model.User;
import com.runcible.abbot.service.exceptions.DuplicateUserException;
import com.runcible.abbot.service.exceptions.NoSuchUser;

public interface UserService
{
    /**
     * If there exists a user with email address and password specified, this function
     * returns the user object. Otherwise the function returns false.
     * @param email The email address of the user 
     * @param password The users password
     * @return The corresponding user object or null if the email or password are incorrect
     */
    public User validateLogon( String email, String password);
    
    /**
     * Adds the specified user to the system. If a user already exist with the same
     * email address the function throws
     * @param user The user to be added.
     * @throws DuplicateUserException If a user with this email address is already configured.
     */
    public void addUser(User user) throws DuplicateUserException;

    /**
     * Updates the specified user in the database.
     * @param user The new user details.
     * @throws DuplicateUserException if the email provided is already in use
     */
    public void updateUser(User user) throws DuplicateUserException;
        
    /**
     * Returns a page of users using the pageable info provided
     * @param page Where to start from
     * @return The page of users
     */
    public Page<User> findAll(Pageable page);

    /**
     * Returns all of the users in the system
     * @return The list of users
     */
    public List<User> findAll();

    /**
     * Returns the user identified by the ID provided. Throws if no matching user
     * @param id The ID of the user
     * @return The user
     */
    public User findByID(Integer id) throws NoSuchUser;

    /**
     * Returns the details of the user with the email address specified
     * @param email the email address of the user
     * @return The user
     * @throws NoSuchUser if no user with that email addres exists
     */
    public User findByEmail(String name) throws NoSuchUser;

}
