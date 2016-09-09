package com.runcible.abbot.web.model;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;

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
