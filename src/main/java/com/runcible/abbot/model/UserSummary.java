package com.runcible.abbot.model;

import com.runcible.abbot.model.User;

/**
 * The UserSummary class is a DTO that allows us to display the list of users authorized for a
 * particular race series without exposing the full User model as this would be a security risk.
 * 
 * @author tom
 *
 */
public class UserSummary
{
    /**
     * Default constructor. Initialises with empty values.
     */
    public UserSummary()
    {
    }

    public UserSummary(Integer id, String name, String emailAddress, boolean currentlyLoggedOn)
    {
        super();
        this.id = id;
        this.name = name;
        this.emailAddress = emailAddress;
        this.currentUser = currentlyLoggedOn;
    }

    /**
     * Construct a User summary from a User object
     */
    public UserSummary(User user, boolean currentlyLoggedOn)
    {
        this(   user.getId(),
                user.getFirstName() + " " + user.getLastName(),
                user.getEmail(),
                currentlyLoggedOn);
    }

    /**
     * Returns the name of the user
     * @return
     */
    public String getName()
    {
        return name;
    }
    
    /**
     * Sets the name of the user
     * @param name
     */
    public void setName(String name)
    {
        this.name = name;
    }
    
    /**
     * Returns the users email address.
     * @return The email address
     */
    public String getEmailAddress()
    {
        return emailAddress;
    }
    
    /**
     * Sets the user's email address
     * @param emailAddress
     */
    public void setEmailAddress(String emailAddress)
    {
        this.emailAddress = emailAddress;
    }
    
    /**
     * Returns true if this user is currently logged on. 
     * This is important as you can't de-authorize yourself.
     * @return true if this user is the currently logged on user.
     */
    public boolean isCurrentUser()
    {
        return currentUser;
    }
    
    /**
     * Sets if the currently logged on user is this user
     * @param currentUser
     */
    public void setCurrentUser(boolean currentUser)
    {
        this.currentUser = currentUser;
    }
    
    /**
     * Returns the ID of the user this summary was generated from
     * @return the user ID
     */
    public Integer getId()
    {
        return id;
    }

    /**
     * Sets the ID of the user this summary was generated from
     * @param id The id of the user
     */
    public void setId(Integer id)
    {
        this.id = id;
    }

    private Integer id = 0;
    private String name = "";
    private String emailAddress = "";
    private boolean currentUser = false;
}
