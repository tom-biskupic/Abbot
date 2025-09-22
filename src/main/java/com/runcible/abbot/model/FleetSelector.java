package com.runcible.abbot.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import org.springframework.stereotype.Component;

/**
 *  A Fleet Selector selects one class or a division of a class to be included in a fleet 
 */
@Entity
@Table(name="FLEET_SELECTOR")
@Component
public class FleetSelector implements ModelWithId
{
    public FleetSelector(
    		Integer			id,
            BoatClass      	boatClass,
            BoatDivision	boatDivision)
    {
        super();
        this.id = id;
        this.boatClass = boatClass;
        this.boatDivision = boatDivision;
    }

    public FleetSelector(
            BoatClass      	boatClass,
            BoatDivision	boatDivision)
    {
        this(null,boatClass,boatDivision);
    }

    public FleetSelector()
    {
        super();
        this.boatClass = null;
        this.boatDivision = null;
    }

    @Id
    @GeneratedValue
    @Column(name="FLEET_SELECTOR_ID")
    public Integer getId()
    {
        return id;
    }
    
    public void setId(Integer id)
    {
        this.id = id;
    }
    
    @ManyToOne
    @JoinColumn(name="CLASS")
    public BoatClass getBoatClass()
    {
        return boatClass;
    }
    
    public void setBoatClass(BoatClass boatClass)
    {
        this.boatClass = boatClass;
    }

    @ManyToOne
    @JoinColumn(name="DIVISION")
    public BoatDivision getBoatDivision()
    {
        return boatDivision;
    }
    
    public void setBoatDivision(BoatDivision boatDivision)
    {
        this.boatDivision = boatDivision;
    }

    /**
     * Returns true if the passed class/division matches this selector.
     */
    public boolean matches( BoatClass boatClass, BoatDivision division)
    {
        if ( boatClass.getId() == this.boatClass.getId() )
        {
            if ( division != null && this.boatDivision != null)
            {
                return ( division.getId() == this.boatDivision.getId() );
            }
            else if ( division == null && this.boatDivision == null )
            {
                return true;
            }
        }
        return false;
    }
    
    private Integer             id;
    private BoatClass			boatClass;
    private BoatDivision		boatDivision;
}
