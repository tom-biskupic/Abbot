package com.runcible.abbot.model;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import org.springframework.stereotype.Component;

/**
 * The BoatClassModel object represents a class of boat such as a Laser or 12 ft skiff etc.
 * Each boat is a member of a class.
 */
@Entity
@Table(name="BOAT_CLASS")
@Component
public class BoatClass implements Cloneable, ModelWithId
{
    public BoatClass(
            Integer     id, 
            Integer     raceSeriesID,
            String      name,
            Float       yardstick )
    {
        this.id = id;
        this.name = name;
        this.yardStick = yardstick;
        this.raceSeriesId = raceSeriesID;
    }

    public BoatClass clone()
    {
        BoatClass b = new BoatClass(
                this.id,
                this.raceSeriesId,
                this.name,
                this.yardStick);
        
        for(BoatDivision d : this.divisions)
        {
            b.addDivision(d.clone());
        }
        
        return b;
    }
    
    public BoatClass(Integer seriesID, String className,Float yardstick)
    {
        this(null,seriesID,className,yardstick);
    }

    public BoatClass()
    {
    }

    @Id @GeneratedValue
    @Column(name= "CLASS_ID")
    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    @Column(name = "CLASSNAME")
    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }
    
    @Column(name = "YARDSTICK")
    public Float getYardStick()
    {
        return yardStick;
    }

    public void setYardStick(Float yardStick)
    {
        this.yardStick = yardStick;
    }

    @OneToMany(fetch = FetchType.EAGER, cascade={CascadeType.ALL})
    public Collection<BoatDivision> getDivisions()
    {
        return this.divisions;
    }
    
    public void setDivisions(Collection<BoatDivision> divisions)
    {
        this.divisions = divisions;
    }

    public void addDivision(BoatDivision oneUp)
    {
        this.divisions.add(oneUp);
    }
    
    public boolean hasDivisions()
    {
        return this.divisions == null || this.divisions.size() != 0;
    }

    public BoatDivision getDivision( String name )
    {
        for(BoatDivision division : this.divisions )
        {
            if ( division.getName().equals(name) )
            {
                return division;
            }
        }
        
        return null;
    }
    
    public BoatDivision getDivision( Integer id )
    {
        for(BoatDivision division : this.divisions )
        {
            if ( division.getId().equals(id) )
            {
                return division;
            }
        }
        
        return null;
    }
    
    public void removeDivision( BoatDivision division )
    {
    	divisions.remove(division);
    }
    
    @Column
    public Integer getRaceSeriesId()
    {
        return this.raceSeriesId;
    }
    
    public void setRaceSeriesId(Integer raceSeriesID)
    {
        this.raceSeriesId = raceSeriesID;
    }
    
    private Integer                         id = 0;
    
    @Size(min=1, message="Class name must be provided.")
    private String                          name = "";
    
    private Integer                         raceSeriesId = null;
    private Float                           yardStick=0.0f;
    private Collection<BoatDivision>   		divisions = new ArrayList<BoatDivision>();
}
