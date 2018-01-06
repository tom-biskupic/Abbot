package com.runcible.abbot.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.springframework.stereotype.Component;

@Entity
@Table(name="HANDICAP_LIMITS")
@Component
public class HandicapLimit
{
    public HandicapLimit()
    {
        
    }
    
    /**
     * Creates a handicap limit for a fleet in a race series
     * @param raceSeriesID The race series this applies to
     * @param fleet The fleet this is related to
     * @param limit The limit (what this means depend on the handicap system).
     */
    public HandicapLimit(Integer raceSeriesID, Fleet fleet, Float limit)
    {
        this(null,raceSeriesID,fleet,limit);
    }

    /**
     * Creates a handicap limit for a fleet in a race series
     * @param id The ID of the new limit
     * @param raceSeriesID The race series this applies to
     * @param fleet The fleet this is related to
     * @param limit The limit (what this means depend on the handicap system).
     */
    public HandicapLimit(Integer id, Integer raceSeriesID, Fleet fleet, Float limit)
    {
        super();
        this.id = id;
        this.raceSeriesID = raceSeriesID;
        this.fleet = fleet;
        this.limit = limit;
    }

    /**
     * Returns the ID of this hanicap limit
     * @return the ID of this hanicap limit
     */
    @Id
    @GeneratedValue
    @Column(name="HANDICAP_LIMIT_ID")
    public Integer getId()
    {
        return id;
    }
    
    /**
     * Sets the ID of this hanicap limit
     * @param id the ID of this hanicap limit
     */
    public void setId(Integer id)
    {
        this.id = id;
    }

    /**
     * Returns the fleet this handicap limit applies to
     * @return the fleet this handicap limit applies to
     */
    @OneToOne
    @JoinColumn(name="FLEET")
    public Fleet getFleet()
    {
        return fleet;
    }
    
    /**
     * Sets the fleet this handicap limit applies to
     * @param fleet the fleet this handicap limit applies to
     */
    public void setFleet(Fleet fleet)
    {
        this.fleet = fleet;
    }
    
    /**
     * Returns the race series ID this handicap limit applies to
     * @return the race series ID this handicap limit applies to
     */
    @Column(name="RACE_SERIES_ID")
    public Integer getRaceSeriesID()
    {
        return raceSeriesID;
    }

    /**
     * Sets the race series ID this handicap limit applies to
     * @param raceSeriesID the race series ID this handicap limit applies to
     */
    public void setRaceSeriesID(Integer raceSeriesID)
    {
        this.raceSeriesID = raceSeriesID;
    }

    /**
     * Returns the limit value for the handicap. What this means
     * depends on the handicap system. For example for Mark Foy handicap
     * it is the maximum number of minutes.
     * @return the limit value for the handicap
     */
    @Column(name="LIMIT")
    public Float getLimit()
    {
        return limit;
    }
    
    /**
     * Returns the limit value for the handicap. What this means
     * depends on the handicap system. For example for Mark Foy handicap
     * it is the maximum number of minutes.
     * @param limit the limit value for the handicap
     */
    public void setLimit(Float limit)
    {
        this.limit = limit;
    }
    
    private Integer id;
    private Integer raceSeriesID;
    
    @NotNull(message="A fleet must be selected")
    private Fleet   fleet;
    
    private Float   limit;
}
