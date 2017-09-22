package com.runcible.abbot.model;

import java.time.LocalTime;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
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
			LocalTime    startTime, 
			LocalTime    finishTime,
			ResultStatus status)
	{
		this(null,raceId,boat,handicap,startTime,finishTime,status);
	}

	public RaceResult(
			Integer      id, 
			Integer      raceId, 
			Boat         boat, 
			Integer      handicap, 
			LocalTime    startTime, 
			LocalTime 	 finishTime,
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
	public LocalTime getStartTime() 
	{
		return startTime;
	}
	
	/**
	 * Sets time/date when this race was started
	 * @param startTime The start time
	 */
	public void setStartTime(LocalTime startTime) 
	{
		this.startTime = startTime;
	}
	
	/**
	 * Returns the time/date when the competitor finished. Can be null
	 * @return the time/date when the competitor finished. Can be null
	 */
	@Column(name="FINISH_TIME")
	public LocalTime getFinishTime() 
	{
		return finishTime;
	}
	
	/**
	 * Sets the time/date when the competitor finished. Can be null
	 * @param finishTime the time/date when the competitor finished. Can be null
	 */
	public void setFinishTime(LocalTime finishTime) 
	{
		this.finishTime = finishTime;
	}
	
	/**
	 * The result status (i.e. finished, DNF etc).
	 * @return the finish status
	 */
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
	
	private Integer            id=null;
	private Integer            raceId=null;
	private Boat               boat=null;
	private Integer            handicap=0;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
	private	LocalTime          startTime=null;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
	private LocalTime          finishTime=null;
	private ResultStatus       status = ResultStatus.FINISHED;
}
