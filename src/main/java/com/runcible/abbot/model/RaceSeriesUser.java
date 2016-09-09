package com.runcible.abbot.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.stereotype.Component;

/**
 * The RaceSeriesUser defines a permission for a user to access a race series. 
 * Each race series user grants one user access to a race series. These are not linked to the
 * RaceSeries as otherwise the RaceSeries object would contain full user object (which is a
 * security risk!)
 *  
 * @author tom
 *
 */
@Entity
@Table(name="RACE_SERIES_USERS_TBL")
@Component
public class RaceSeriesUser 
{
    public RaceSeriesUser()
    {
        this(null,null,null);
    }
    
	public RaceSeriesUser(
			RaceSeries 	raceSeries,
			User		user )
	{
		this(null,raceSeries,user);
	}

   public RaceSeriesUser(
        Integer     id,
        RaceSeries  raceSeries,
        User        user )
    {
       this.id = id;
       this.raceSeries = raceSeries;
       this.user = user;
    }

    /**
     * Returns the auto-generated ID of this RaceSeriesUser
     * @return the ID of this RaceSeriesUser
     */
    @Id
    @GeneratedValue
    @Column(name="ID")
    public Integer getId()
    {
        return id;
    }

    /**
     * Sets the ID of this RaceSeriesUser
     * @param id the ID of this RaceSeriesUser
     */
    public void setId(Integer id)
    {
        this.id = id;
    }

    @ManyToOne
    @JoinColumn(name="RACE_SERIES_ID")
	public RaceSeries getRaceSeries() 
	{
		return raceSeries;
	}

	public void setRaceSeries(RaceSeries raceSeries) 
	{
		this.raceSeries = raceSeries;
	}

    @ManyToOne
    @JoinColumn(name="USER_ID")
	public User getUser() 
	{
		return user;
	}

	public void setUser(User user) 
	{
		this.user = user;
	}

    private Integer 	id;
	private RaceSeries 	raceSeries;
	private User		user;
}
