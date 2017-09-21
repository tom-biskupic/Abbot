package com.runcible.abbot.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.stereotype.Component;
	
/**
 * A fleet is a collection of boat classes competing with each other.
 */
@Entity
@Table(name="FLEET")
@Component
public class Fleet implements ModelWithId
{
    public Fleet(
    		Integer				id,
    		Integer  			raceSeriesId,
            String              fleetName,
            Set<FleetSelector>  fleetClasses)
    {
        super();
        this.id = id;
        this.raceSeriesId = raceSeriesId;
        this.fleetClasses = fleetClasses;
        this.fleetName = fleetName;
    }

    public Fleet(
    		Integer             raceSeriesId,
            String              fleetName,
            Set<FleetSelector>  fleetClasses)
    {
        super();
        this.raceSeriesId = raceSeriesId;
        this.fleetClasses = fleetClasses;
        this.fleetName = fleetName;
    }

    public Fleet()
    {
    }

    @Id
    @GeneratedValue
    @Column(name="FLEET_ID")
    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    @Column(name="FLEET_NAME")
    public String getFleetName()
    {
        return fleetName;
    }

    public void setFleetName(String fleetName)
    {
        this.fleetName = fleetName;
    }

    /**
     * Returns the classes that are part of the fleet.
     * @return collection of classes in the fleet.
     */
    @OneToMany(fetch = FetchType.EAGER, cascade={CascadeType.ALL})
    public Set<FleetSelector> getFleetClasses()
    {
        return fleetClasses;
    }

    /**
     * Sets the classes that are part of the fleet.
     * @param fleetClasses classes competing in the fleet
     */
    public void setFleetClasses(Set<FleetSelector> classes)
    {
        this.fleetClasses = classes;
    }

    @Column
    public Integer getRaceSeriesId()
    {
    	return this.raceSeriesId;
    }

    public void setRaceSeriesId(Integer raceSeriesId)
    {
    	this.raceSeriesId = raceSeriesId;
    }
    
    /**
     * Returns true if this fleet consists of multiple classes or divisions
     */
    @Transient
    public boolean isMultiFleet()
    {
        return this.fleetClasses != null & this.fleetClasses.size() > 1;
    }
    
    private Integer             id=0;
    private	Integer             raceSeriesId;
    private String              fleetName="";
    private Set<FleetSelector>  fleetClasses = new HashSet<FleetSelector>();
}
