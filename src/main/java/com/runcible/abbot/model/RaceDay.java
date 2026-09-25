package com.runcible.abbot.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * Models a day on which races occur. There could be one or more races on
 * any race day
 */
public class RaceDay 
{
	public RaceDay( LocalDate day, List<Race> races)
	{
		this.day = day;
		this.races = races;
	}

	public RaceDay(LocalDate day)
	{
		this.day = day;
	}

	/**
	 * Returns the date of this race day
	 * @return
	 */
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	public LocalDate getDay()
	{
		return day;
	}

	/**
	 * Sets the date of this RaceDay
	 * @param day the date of the race day
	 */
	public void setDay(LocalDate day)
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

	private LocalDate day;
	private List<Race> races = new ArrayList<Race>();
}
