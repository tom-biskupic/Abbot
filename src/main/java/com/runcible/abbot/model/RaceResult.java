package com.runcible.abbot.model;

import java.util.Calendar;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * Models a result for a single boat in a single race.
 */
@Entity
@Table(name="RACE_RESULT_TBL")
@Component
public class RaceResult 
{
    public RaceResult()
    {
        
    }
    
	public RaceResult( 
			Integer      raceId, 
			Boat         boat, 
			Integer      handicap, 
			Date         startTime, 
			Date                 finishTime,
			ResultStatus status)
	{
		this(null,raceId,boat,handicap,startTime,finishTime,status);
	}

	public RaceResult(
			Integer      id, 
			Integer      raceId, 
			Boat         boat, 
			Integer      handicap, 
			Date         startTime, 
			Date 	       finishTime,
			ResultStatus status) 
	{
		super();
		this.id = id;
		this.raceId = raceId;
		this.boat = boat;
		this.handicap = handicap;
		this.startTime = startTime;
		this.finishTime = finishTime;
		this.status = status;
	}

	/**
	 * Returns the auto-generated internal ID of this result
	 * @return the auto-generated internal ID of this result
	 */
    @Id
    @GeneratedValue
    @Column(name="ID")
	public Integer getId() 
	{
		return id;
	}

    /**
     * Sets the auto-generated internal ID of this result
     * @param id the auto-generated internal ID of this result
     */
	public void setId(Integer id) 
	{
		this.id = id;
	}

	/**
	 * The ID of the race this result is associated with
	 * @return The race ID
	 */
	@Column(name="RACE_ID")
	public Integer getRaceId() 
	{
		return raceId;
	}
	
	/**
	 * Sets the ID of the race this result is associated
	 * with
	 * @param raceId The race ID
	 */
	public void setRaceId(Integer raceId) 
	{
		this.raceId = raceId;
	}
	
	/**
	 * Returns the boat this is the result for in this race
	 * @return The boat for this result
	 */
	@ManyToOne(optional=false,fetch=FetchType.EAGER, cascade={CascadeType.DETACH,CascadeType.MERGE})
	public Boat getBoat() 
	{
		return boat;
	}
	
	/**
	 * Sets the boat this is the result for in this race
	 * @param boat The boat for this result
	 */
	public void setBoat(Boat boat) 
	{
		this.boat = boat;
	}
	
	/**
	 * Returns the handicap applied for this boat in this race 
	 * @return
	 */
	@Column(name="HANDICAP")
	public Integer getHandicap() 
	{
		return handicap;
	}
	
	/**
	 * Sets the handicap applied for this boat in this race
	 * @param handicap the handicap of this boat in this race
	 */
	public void setHandicap(Integer handicap) 
	{
		this.handicap = handicap;
	}
	
	/**
	 * The time/date when this race was started
	 * @return The start time
	 */
	@Column(name="START_TIME")
    //@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ")
	//@DateTimeFormat(iso = ISO.TIME)
	//@Temporal(TemporalType.TIME)
	public Date getStartTime() 
	{
		return startTime;
	}
	
	/**
	 * Sets time/date when this race was started
	 * @param startTime The start time
	 */
	public void setStartTime(Date startTime) 
	{
		this.startTime = startTime;
	}
	
	/**
	 * Returns the time/date when the competitor finished. Can be null
	 * @return the time/date when the competitor finished. Can be null
	 */
	@Column(name="FINISH_TIME")
	public Date getFinishTime() 
	{
		return finishTime;
	}
	
	/**
	 * Sets the time/date when the competitor finished. Can be null
	 * @param finishTime the time/date when the competitor finished. Can be null
	 */
	public void setFinishTime(Date finishTime) 
	{
		this.finishTime = finishTime;
	}
	
	/**
	 * The result status (i.e. finished, DNF etc).
	 * @return the finish status
	 */
	@Column(name="STATUS")
	public ResultStatus getStatus() 
	{
		return status;
	}
	
	/**
	 * Sets the result status (i.e. finished, DNF etc).
	 * @param status result status
	 */
	public void setStatus(ResultStatus status) 
	{
		this.status = status;
	}
	
	/**
	 * Returns the sailing time (in seconds) which is calculated by subtracting the
	 * start time from the finish time
	 * @return the sailing time
	 */
    @Column(name="SAILING_TIME")
	public Integer getSailingTime()
    {
        return sailingTime;
    }

	/**
	 * Sets the sailing time (in seconds) which is calculated by subtracting the
     * start time from the finish time
	 * @param sailingTime The sailing time
	 */
    public void setSailingTime(Integer sailingTime)
    {
        this.sailingTime = sailingTime;
    }

    /**
     * Returns the corrected time (in seconds) which is calculated by subtracting the
     * handicap time from the sailing time
     * @return the corrected time
     */
    @Column(name="CORRECTED_TIME")
    public Integer getCorrectedTime()
    {
        return correctedTime;
    }

    /**
     * Sets the corrected time (in seconds) which is calculated by subtracting the
     * handicap time from the sailing time
     * @param correctedTime The corrected time
     */
    public void setCorrectedTime(Integer correctedTime)
    {
        this.correctedTime = correctedTime;
    }

	private Integer            id=null;
	private Integer            raceId=null;
	
	@NotNull(message="A boat must be selected")
	private Boat               boat=null;
	private Integer            handicap=0;

	@NotNull(message="A start time must be provided")
	private	Date               startTime=null;

	@NotNull(message="A finish time must be provided")
	private Date               finishTime=null;

	private Integer            sailingTime=null;
	private Integer            correctedTime=null;

    @NotNull(message="A result status must be provided")
	private ResultStatus       status = ResultStatus.FINISHED;
}
