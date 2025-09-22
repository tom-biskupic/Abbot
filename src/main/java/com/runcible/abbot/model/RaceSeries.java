package com.runcible.abbot.model;

import java.util.Calendar;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;

import org.springframework.stereotype.Component;

/**
 * Models a race series which is a collection of races with
 * a registered boat etc
 */
@Entity
@Table(name="RACE_SERIES_TBL")
@Component
public class RaceSeries implements ModelWithId
{
	public RaceSeries(	Integer 		id,
						RaceSeriesType 	seriesType,
						String			name,
						String          comment,
						Date            dateCreated,
    					Date            lastUpdated)
	{
		super();
		this.id = id;
		this.seriesType = seriesType;
		this.name = name;
		this.comment = comment;
		this.dateCreated = dateCreated;
		this.lastUpdated = lastUpdated;
	}

	public RaceSeries(	
			RaceSeriesType 	seriesType,
			String			name,
			String          comment,
			Date            dateCreated,
			Date            lastUpdated)
	{
		this(null,seriesType,name,comment,dateCreated,lastUpdated);
	}
	
	public RaceSeries(	
			RaceSeriesType 	seriesType,
			String			name,
			String          comment)
	{
		this(null,seriesType,name,comment,Calendar.getInstance().getTime(),Calendar.getInstance().getTime());
	}

	public RaceSeries()
	{
	}

    /**
     * Returns the auto-generated ID of this raceSeries
     * @return the ID of this raceSeries
     */
    @Id
    @GeneratedValue
    @Column(name="ID")
    public Integer getId()
    {
        return id;
    }

    /**
     * Sets the ID of this raceSeries
     * @param id the ID of this raceSeries
     */
    public void setId(Integer id)
    {
        this.id = id;
    }

    /**
     * Returns the type of the race series
     * @return the type of the race series
     */
    @Column(name="SERIES_TYPE")
    public RaceSeriesType getSeriesType()
    {
        return seriesType;
    }

    /**
     * Sets the type of the race series
     * @param seriesType the type of the race series
     */
    public void setSeriesType(RaceSeriesType seriesType)
    {
        this.seriesType = seriesType;
    }

    /**
     * Returns the name of the race series
     * @return the name of the race series
     */
    @Column(name="NAME")
    public String getName()
    {
        return name;
    }
    
    /**
     * Sets the name of the race series
     * @param name The name of the race series
     */
    public void setName(String name)
    {
        this.name = name;
    }
    
    
    /**
     * The date the race series was created
     * @return The date the race series was created
     */
    @Column(name="DATE_CREATED")
    public Date getDateCreated()
    {
        return dateCreated;
    }
    
    /**
     * Sets the date the race series was created
     * @param dateCreated the date the race series was created
     */
    public void setDateCreated(Date dateCreated)
    {
        this.dateCreated = dateCreated;
    }
    
    /**
     * Returns the date this series was last updated. An update
     * means the addition of a race or race result
     * @return the date the series was last updated.
     */
    @Column(name="LAST_UPDATED")
    public Date getLastUpdated()
    {
        return lastUpdated;
    }
    
    /**
     * Sets the date this series was last updated.
     * @param lastUpdated the date this series was last updated
     */
    public void setLastUpdated(Date lastUpdated)
    {
        this.lastUpdated = lastUpdated;
    }

    /**
     * Returns the comment associated with this race series
     * @return the race series comment
     */
    public String getComment()
    {
        return comment;
    }

    /**
     * Sets a comment to be associated with the race series
     * @param comment the comment
     */
    public void setComment(String comment)
    {
        this.comment = comment;
    }

    private Integer         id = 0;
    private RaceSeriesType  seriesType = RaceSeriesType.SEASON;
    
    @Size(min=1, message="Name must be provided.")
    private String          name = "";
    
    private String          comment = "";
    private Date            dateCreated;
    private Date            lastUpdated;
}
