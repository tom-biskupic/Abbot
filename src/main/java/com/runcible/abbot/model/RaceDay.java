package com.runcible.abbot.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Models a day on which races occur. There could be one or more races on
 * any race day
 */
public class RaceDay 
{
	public RaceDay( Date day, List<Race> races)
	{
		this.day = day;
		this.races = races;
	}

	public RaceDay(Date day)
	{
		this.day = day;
	}

	/**
	 * Returns the date of this race day
	 * @return
	 */
	public Date getDay() 
	{
		return day;
	}
	
	/**
	 * Sets the date of this RaceDay
	 * @param day the date of the race day
	 */
	public void setDay(Date day) 
	{
		this.day = day;
	}
	
	/**
	 * Returns the list of races for this race
	 * day
	 * @return The list of races on this day
	 */
	public List<Race> getRaces() 
	{
		return races;
	}
	
	/**
	 * Sets the list of races on this race day
	 * @param races the list of races on this race day
	 */
	public void setRaces(List<Race> races) 
	{
		this.races = races;
	}

	private Date day;
	private List<Race> races = new ArrayList<Race>();
}
