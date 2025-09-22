package com.runcible.abbot.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import org.springframework.stereotype.Component;

/**
 * Models the handicap of a boat after a given race.
 */
@Entity
@Table(name="HANDICAP")
@Component
public class Handicap
{
    public Handicap()
    {
    }
    
    public Handicap(Integer id, Integer boatID, Integer raceID, Float value)
    {
        super();
        this.id = id;
        this.boatID = boatID;
        this.raceID = raceID;
        this.value = value;
    }

    @Id
    @GeneratedValue
    @Column(name="ID")
    public Integer getId()
    {
        return this.id;
    }
    
    public void setId(Integer id)
    {
        this.id = id;
    }
    
    /**
     * Returns the ID of the boat this handicap applies to
     * @return The boat this handicap applies to
     */
    @Column(name="BOAT_ID")
    public Integer getBoatID()
    {
        return boatID;
    }
    
    /**
     * Sets the boat this handicap applies to
     * @param boat the boat ID this handicap applies to
     */
    public void setBoatID(Integer boatID)
    {
        this.boatID = boatID;
    }

    /**
     * Returns the ID of the race this handicap was set (so the
     * handicap is the outcome of this race)
     * @return The race this handicap applies to
     */
    @Column(name="RACE_ID")
    public Integer getRaceID()
    {
        return raceID;
    }
    
    /**
     * Sets the race this handicap applies to
     * @param raceID the ID of the race this handicap was set
     */
    public void setRaceID(Integer raceID)
    {
        this.raceID = raceID;
    }

    /**
     * Returns the current handicap value. This could be minutes or
     * could be a proportion depending on the handicap system in use
     * @return the current handicap
     */
    @Column(name="VALUE")
    public Float getValue()
    {
        return value;
    }

    /**
     * Sets the current handicap value. This could be minutes or
     * could be a proportion depending on the handicap system in use
     * @param value the current handicap value
     */
    public void setValue(Float value)
    {
        this.value = value;
    }

    /**
     * Returns the number of times this boat has won in the season.
     * The the handicap adjustment changes with the number of wins 
     * @return the number of wins.
     */
    public Integer getNumberOfWins()
    {
        return numberOfWins;
    }

    /**
     * Sets the number of wins for this boat
     * @param numberOfWins
     */
    public void setNumberOfWins(Integer numberOfWins)
    {
        this.numberOfWins = numberOfWins;
    }

    private Integer     id;
    private Integer     boatID;
    private Integer     raceID;
    private Float       value = 0.0f;
    private Integer     numberOfWins = 0;
}
