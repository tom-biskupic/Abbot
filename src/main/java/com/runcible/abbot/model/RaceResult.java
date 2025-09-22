package com.runcible.abbot.model;

import java.util.Date;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

import org.springframework.stereotype.Component;

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
			Float        handicap, 
			boolean      overrideHandicap,
			Date         startTime, 
			Date         finishTime,
			ResultStatus status)
	{
		this( null,
		      raceId,
		      boat,
		      handicap,
		      overrideHandicap,
		      startTime,
		      finishTime,
		      status,
		      null,
		      null,
		      null,
		      null);
	}

	public RaceResult( 
			Integer      raceId, 
			Boat         boat, 
			Float        handicap,
			boolean      overrideHandicap,
			Date         startTime, 
			Date         finishTime,
			ResultStatus status,
			Integer		 sailingTime,
			Integer		 correctedTime,
			Integer      handicapPlace,
			Integer      scratchPlace )
	{
		this( null,
		      raceId,
		      boat,
		      handicap,
		      overrideHandicap,
		      startTime,
		      finishTime,
		      status,
		      sailingTime,
		      correctedTime,
		      handicapPlace,
		      scratchPlace);
	}

	public RaceResult(
			Integer      id, 
			Integer      raceId, 
			Boat         boat, 
			Float        handicap,
			boolean      overrideHandicap,
			Date         startTime, 
			Date 	     finishTime,
			ResultStatus status,
			Integer		 sailingTime,
			Integer		 correctedTime,
			Integer      handicapPlace,
			Integer      scratchPlace) 
	{
		super();
		this.id = id;
		this.raceId = raceId;
		this.boat = boat;
		this.handicap = handicap;
		this.overrideHandicap = overrideHandicap;
		this.startTime = startTime;
		this.finishTime = finishTime;
		this.status = status;
		this.sailingTime = sailingTime;
		this.correctedTime = correctedTime;
		this.handicapPlace = handicapPlace;
		this.scratchPlace = scratchPlace;
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
	public Float getHandicap() 
	{
		return handicap;
	}
	
	/**
	 * Sets the handicap applied for this boat in this race
	 * @param handicap the handicap of this boat in this race
	 */
	public void setHandicap(Float handicap) 
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

    /**
     * Returns the place this finisher got on scratch (i.e over the line)
     * @return the scratch place
     */
	public Integer getScratchPlace()
    {
        return scratchPlace;
    }

	/**
	 * Sets the place this finisher got on sratch (i.e over the line)
	 * @param scratchPlace the scratch place
	 */
    public void setScratchPlace(Integer scratchPlace)
    {
        this.scratchPlace = scratchPlace;
    }

    /**
     * Returns the place this finisher got on handicap time
     * @return the handicap place
     */
    public Integer getHandicapPlace()
    {
        return handicapPlace;
    }

    /**
     * Sets the place this finisher got on handicap time
     * @param handicapPlace the handicap place
     */
    public void setHandicapPlace(Integer handicapPlace)
    {
        this.handicapPlace = handicapPlace;
    }

    /**
     * Returns true if the handicap value was selected by the user
     * (instead of calculated)
     * @return if the handicap has been overriden
     */
    public Boolean getOverrideHandicap()
    {
        return overrideHandicap;
    }

    /**
     * Sets if the handicap value was selected by the user
     * (instead of calculated)
     * @param overrideHandicap if the handicap was overridden
     */
    public void setOverrideHandicap(Boolean overrideHandicap)
    {
        this.overrideHandicap = overrideHandicap;
    }

    private Integer            id=null;
	private Integer            raceId=null;
	
	@NotNull(message="A boat must be selected")
	private Boat               boat=null;
	private Float              handicap=0.0f;

	private	Date               startTime=null;
	private Date               finishTime=null;

	private Integer            sailingTime=null;
	private Integer            correctedTime=null;

	private Integer            scratchPlace=0;
	private Integer            handicapPlace=0;
	private Boolean            overrideHandicap=false;
	
    @NotNull(message="A result status must be provided")
	private ResultStatus       status = null;
}
