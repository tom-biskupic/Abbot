package com.runcible.abbot.model;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

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
            Set<FleetSelector>  fleetClasses,
            boolean             competeOnYardstick)
    {
        super();
        this.id = id;
        this.raceSeriesId = raceSeriesId;
        this.fleetClasses = fleetClasses;
        this.fleetName = fleetName;
        this.competeOnYardstick = competeOnYardstick;
    }

    public Fleet(
    		Integer             raceSeriesId,
            String              fleetName,
            Set<FleetSelector>  fleetClasses,
            boolean             competeOnYardstick)
    {
        super();
        this.raceSeriesId = raceSeriesId;
        this.fleetClasses = fleetClasses;
        this.fleetName = fleetName;
        this.competeOnYardstick = competeOnYardstick;
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

    /**
     * Returns true if when combining results from races where this
     * fleet competes, the results should be adjusted for each vessels
     * yardstick. The effect is that the scratch result is adjusted
     * by the vessels yardstick and then a handicap is applied after that
     * @return if the fleet competes on yardstick
     */
    @Column
    public Boolean getCompeteOnYardstick()
    {
        return competeOnYardstick;
    }

    /**
     * Sets if when combining results from races where this
     * fleet competes, the results should be adjusted for each vessels
     * yardstick. The effect is that the scratch result is adjusted
     * by the vessels yardstick and then a handicap is applied after that

     * @param competeOnYardstick true if the fleet competes on yardstick
     */
    public void setCompeteOnYardstick(Boolean competeOnYardstick)
    {
        this.competeOnYardstick = competeOnYardstick;
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
    private Boolean             competeOnYardstick = false;
}
