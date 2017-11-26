package com.runcible.abbot.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.stereotype.Component;

@Entity
@Table(name="HANDICAP_LIMITS")
@Component
public class HandicapLimit
{
    public HandicapLimit()
    {
        
    }
    
    public HandicapLimit(Fleet fleet, Float limit)
    {
        super();
        this.id = null;
        this.fleet = fleet;
        this.limit = limit;
    }

    public HandicapLimit(Integer id, Fleet fleet, Float limit)
    {
        super();
        this.id = id;
        this.fleet = fleet;
        this.limit = limit;
    }

    @Id
    @GeneratedValue
    @Column(name="HANDICAP_LIMIT_ID")
    public Integer getId()
    {
        return id;
    }
    
    public void setId(Integer id)
    {
        this.id = id;
    }
    
    @ManyToOne
    public Fleet getFleet()
    {
        return fleet;
    }
    
    public void setFleet(Fleet fleet)
    {
        this.fleet = fleet;
    }
    
    @Column(name="LIMIT")
    public Float getLimit()
    {
        return limit;
    }
    
    public void setLimit(Float limit)
    {
        this.limit = limit;
    }
    
    private Integer id;
    private Fleet   fleet;
    private Float   limit;
}
