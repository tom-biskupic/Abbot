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
 * A Boat represents a boat registered to sail at the club in a particular series. 
 * The boat model include the identification details of the boat as well as information 
 * about the people who sail the boat.
 */
@Entity
@Table(name="BOATS")
@Component
public class Boat implements ModelWithId
{
    public Boat(    Integer         id,
                    Integer         raceSeriesID,
                    String          name,
                    String          sailNumber,
                    BoatClass       boatClass,
                    BoatDivision    division,
                    Boolean         visitor,
                    String          skipper,
                    String          crew)
    {
        this.id = id;
        this.raceSeriesID = raceSeriesID;
        this.name = name;
        this.sailNumber = sailNumber;
        this.boatClass = boatClass;
        this.divison = division;
        this.visitor = visitor;
        this.skipper = skipper;
        this.crew = crew;
    }

    public Boat(    Integer         raceSeriesID,
                    String          name,
                    String          sailNumber,
                    BoatClass       boatClass,
                    BoatDivision    division,
                    Boolean         visitor,
                    String          skipper,
                    String          crew)
    {
        this(   null,
                raceSeriesID,
                name,
                sailNumber,
                boatClass,
                division,
                visitor,
                skipper,
                crew);
    }

    public Boat()
    {
    }

    /**
     * Returns the internal ID of this boat. The ID is used to uniquely identify the boat
     * in the database.
     * @return the database ID of the boat
     */
    @Id
    @GeneratedValue
    public Integer getId()
    {
        return id;
    }

    /**
     * Sets the database ID of the boat.
     * @param id The ID of the boat in the database.
     */
    public void setId(Integer id)
    {
        this.id = id;
    }

    /**
     * Returns the race series ID this boat is part of
     * @return the race series ID this boat is part of
     */
    @Column(name="RACE_SERIES")
    public Integer getRaceSeriesID()
    {
        return raceSeriesID;
    }

    /**
     * Sets the race series ID this boat is part of
     * @param raceSeries The race series the boat is part of
     */
    public void setRaceSeriesID(Integer raceSeriesID)
    {
        this.raceSeriesID = raceSeriesID;
    }

    /**
     * Returns the name the boat is known by.
     * @return the boat name
     */
    @Column(name="NAME")
    public String getName()
    {
        return name;
    }

    /**
     * Sets the name the boat is known by
     * @param name the name of the boat
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * Returns the sail number or registration number of the boat. Sail numbers
     * are usually allocated by the class association.
     * @return the sail number.
     */
    @Column(name="SAIL_NUMBER")
    public String getSailNumber()
    {
        return sailNumber;
    }

    /**
     * Sets the sail number or the registration number of the boat. Sailnumbers
     * are usually allocated by the class association.
     * @param sailNumber
     */
    public void setSailNumber(String sailNumber)
    {
        this.sailNumber = sailNumber;
    }

    /**
     * Returns the class of this boat.
     * @return
     */
    @ManyToOne
    @JoinColumn(name="BOAT_CLASS")
    public BoatClass getBoatClass()
    {
        return boatClass;
    }

    /**
     * Sets the class of this boat
     * @param boatClass
     */
    public void setBoatClass(BoatClass boatClass)
    {
        this.boatClass = boatClass;
    }

    /**
     * Returns the divison of this boat.
     * @return
     */
    @ManyToOne
    @JoinColumn(name="DIVISION")
    public BoatDivision getDivision()
    {
        return this.divison;
    }

    /**
     * Sets the division of this boat
     * @param boatClass
     */
    public void setDivision(BoatDivision division)
    {
        this.divison = division;
    }

    /**
     * Returns true if this boat is a visitor to the club
     * @return
     */
    @Column(name="VISITOR")
    public Boolean getVisitor()
    {
        return visitor;
    }

    /**
     * Sets a flag indicating if this boat is a visitor to the club
     * @param visitor
     */
    public void setVisitor(Boolean visitor)
    {
        this.visitor = visitor;
    }

    /**
     * Returns the name of the boat skipper
     * @return
     */
    @Column(name="SKIPPER")
    public String getSkipper()
    {
        return skipper;
    }

    /**
     * Sets the name of the boat's crew
     * @param skipper
     */
    public void setSkipper(String skipper)
    {
        this.skipper = skipper;
    }

    /**
     * Returns the name of the boat's crew
     * @return
     */
    @Column(name="CREW")
    public String getCrew()
    {
        return crew;
    }

    /**
     * Sets the name of the boat's crew
     * @param crew
     */
    public void setCrew(String crew)
    {
        this.crew = crew;
    }
    
    private Integer         id=0;
    private Integer         raceSeriesID = null;
    private String          name="";
    private String          sailNumber="";
    private BoatClass       boatClass=null;
    private BoatDivision    divison=null;
    private Boolean         visitor=false;
    private String          skipper="";
    private String          crew="";
}
