package com.runcible.abbot.web.model;

import jakarta.validation.constraints.Size;

import jakarta.validation.constraints.Email;

public class UserToAuthorize
{
    public UserToAuthorize()
    {
        
    }
    
    public UserToAuthorize(String emailAddress)
    {
        this.emailAddress = emailAddress;
    }
    
    public String getEmailAddress()
    {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress)
    {
        this.emailAddress = emailAddress;
    }

    @Size(min=1,message="Email address cannot be empty")
    @Email(message="Email address is not valid")
    private String emailAddress = "";
}
