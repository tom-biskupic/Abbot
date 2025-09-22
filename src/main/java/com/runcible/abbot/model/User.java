package com.runcible.abbot.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;

import org.springframework.stereotype.Component;

/**
 * Models a user of the Abbot race management system. Each user has one or more 
 * race series.
 * 
 * The user class also defines data used to authenticate the user when logging
 * on.
 */
@Entity
@Table(name="USER_TBL")
@Component
public class User
{

    public User(String email, String password, String firstName, String lastName, String organisation, boolean isAdmin)
    {
    	this.password = password;
        this.isAdmin = isAdmin;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.organisation = organisation;
    }
    
    public User()
    {
        this.isAdmin = false;
        this.email = "";
        this.firstName = "";
        this.lastName = "";
        this.organisation = "";
    }
    
    public User(
    		Integer id, 
    		String 	email, 
    		String 	password,
    		String 	firstName,
			String 	lastName, 
			String 	organisation, 
			boolean isAdmin) 
    {
    	this(email,password,firstName,lastName,organisation,isAdmin);
		this.id = id;
	}

	@Id
    @GeneratedValue
    @Column(name="ID")
    public Integer getId()
    {
        return id;
    }
    
    public void setId( Integer value )
    {
        this.id = value;
    }
    
    @Column(name = "FIRST_NAME")
    public String getFirstName()
    {
        return firstName;
    }
    
    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }

    @Column(name = "LAST_NAME")    
    public String getLastName()
    {
        return lastName;
    }
    
    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }

    @Column(unique = true, name = "EMAIL")        
    public String getEmail()
    {
        return email;
    }
    
    public void setEmail(String email)
    {
        this.email = email;
    }

    @Column(name = "PASSWORD")            
    public String getPassword()
    {
        return password;
    }
    
    public void setPassword(String password)
    {
        this.password = password;
    }
    
    @Column(name="ORGANISATION")
    public String getOrganisation()
    {
        return organisation;
    }

    public void setOrganisation(String organisation)
    {
        this.organisation = organisation;
    }

    @Column(name = "IS_ADMIN")
    public boolean isAdministrator()
    {
        return isAdmin;
    }
    
    public void setAdministrator(boolean value)
    {
        this.isAdmin = value;
    }
    
    private Integer id;
    
    @Size(min=1, message="First Name must be provided.")
    private String firstName;
    
    @Size(min=1, message="Last Name must be provided.")
    private String lastName;
    
    private String organisation;
    
    @Size(min=1, message="Email must be provided.")
    private String email;
    
    @Size(min=1, message="Password must be provided.")
    private String password;
    
    private boolean isAdmin;
}
