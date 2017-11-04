package com.runcible.abbot.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.stereotype.Component;

/**
 * Models the current handicap of a boat in a competition.
 */
@Entity
@Table(name="HANDICAP")
@Component
public class Handicap
{
    
    public Handicap(Integer id, Integer boatID, Float value)
    {
        super();
        this.id = id;
        this.boatID = boatID;
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

    private Integer     id;
    private Integer     boatID;
    private Float       value;
}
