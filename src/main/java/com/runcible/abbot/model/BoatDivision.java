package com.runcible.abbot.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

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
    private String  name="";
    private Float   yardStick=0.0f;
}
