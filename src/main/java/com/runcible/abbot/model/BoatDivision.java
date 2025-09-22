package com.runcible.abbot.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;

/**
 * The BoatDivisionModel object represents a sub-division of a class. Classes can 
 * be sub-divided to facilitate competitors with similar abilities competing against one
 * another (such as Seniors or Juniors etc) or to represent sub-classes of boat (such as
 * Laser Radial)
 */
@Entity
@Table(name="BOAT_CLASS_DIVISON")
public class BoatDivision implements Cloneable, ModelWithId
{
    public BoatDivision(Integer id,String name,Float yardstick)
    {
        this.id = id;
        this.name = name;
        this.yardStick = yardstick;
    }

    public BoatDivision(String name,Float yardstick)
    {
        this(null,name,yardstick);
    }

    public BoatDivision()
    {
    }

    public BoatDivision clone()
    {
        return new BoatDivision(this.id,this.name,this.yardStick);
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
    
    @Column(name="DIVISION_NAME")
    public String getName()
    {
        return this.name;
    }
    
    public void setName(String name)
    {
        this.name = name;
    }

    @Column(name="YARDSTICK")
    public Float getYardStick()
    {
        return yardStick;
    }

    public void setYardStick(Float yardStick)
    {
        this.yardStick = yardStick;
    }
    
    private Integer id=0;
    
    @Size(min=1, message="Division name must be provided.")
    private String  name="";
    private Float   yardStick=0.0f;
}
