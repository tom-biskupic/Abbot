package com.runcible.abbot.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Size;

import org.springframework.stereotype.Component;

/**
 * 	A set of races can be combine into a competition so that points can be allocated
 * 	and a competition winner identified.
 * 
 *  Competitions can be based on scratch or handicap results and either the bonus points
 *  or low points system can be used. Also the competition can specify how many races 
 *  each competitor can drop.
 */
@Entity
@Table(name="COMPETITION")
@Component
public class Competition implements ModelWithId
{

    public Competition()
    {
    }

    /**
     * Constructs a competition.
     * @param id database ID of this competition.
     * @param name          Name of the competition. Eg. "Laser club championship"
     * @param series		The series this competition is part of.
     * @param pointsSystem  What pointscore calculations should be applied
     * @param drops         The number of races that can be discarded from the series
     * @param resultType    The type of result this competition is based on
     *                      (scratch or handicap)
     * @fleet               Identifies the set of classes that will race in
     *                      this competition.
     */
    public Competition(   
            String          name, 
            Integer         raceSeriesId,
            PointsSystem    pointsSystem,
            Integer     	drops,
            ResultType		resultType,
            Fleet       	fleet)
    {
        this(null,name,raceSeriesId,pointsSystem,drops,resultType,fleet);
    }
 
    /**
     * Constructs a competition.
     * @param id database ID of this competition.
     * @param name Name of the competition. Eg. "Laser club championship"
     * @param pointsSystem What pointscore calculations should be applied
     */
    public Competition(	Integer         id, 
                        String          name,
                        Integer         raceSeriesId,
                        PointsSystem	pointsSystem,
                        Integer         drops,
                        ResultType      resultType,
                        Fleet          	fleet)
    {
        this.id = id;
        this.raceSeriesId = raceSeriesId;
        this.name = name;
        this.drops = drops;
        this.resultType = resultType;
        this.pointsSystem = pointsSystem;
        this.fleet = fleet;
    }
    
    /**
     * Returns the database ID of this competition
     * @return
     */
    @Id @GeneratedValue
    @Column(name= "COMP_ID")
    public Integer getId()
    {
        return id;
    }
    
    public void setId(Integer id)
    {
        this.id = id;
    }
    
    @Column(name = "NAME")
    public String getName()
    {
        return name;
    }
    
    public void setName(String name)
    {
        this.name = name;
    }
    
    @Column(name = "POINTS_SYSTEM")
    public PointsSystem getPointsSystem()
    {
        return pointsSystem;
    }
    
    public void setPointsSystem(PointsSystem pointsSystem)
    {
        this.pointsSystem = pointsSystem;
    }

    @Column(name = "DROPS")
    public Integer getDrops()
    {
        return drops;
    }

    public void setDrops(Integer drops)
    {
        this.drops = drops;
    }

    @Column(name="FLEET_SIZE")
    public Integer getFleetSize()
    {
    	return this.fleetSize;
    }
    
    public void setFleetSize(Integer fleetSize)
    {
    	this.fleetSize = fleetSize;
    }
    
    @Column(name = "RESULT_TYPE")
    public ResultType getResultType()
    {
        return resultType;
    }

    public void setResultType(ResultType resultType)
    {
        this.resultType = resultType;
    }

    @ManyToOne
    @JoinColumn(name="FLEET")
    public Fleet getFleet()
    {
        return fleet;
    }

    public void setFleet(Fleet fleet)
    {
        this.fleet = fleet;
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
    
    private Integer             id=null;
    
    @Size(min=1, message="Class name must be provided.")
    private String              name="";
    private PointsSystem        pointsSystem = PointsSystem.LOW_POINTS;
    private Fleet          		fleet = null;
    
    @DecimalMin(value="0",message="The number of drops must be selected")
    private Integer             drops = 0;
    
    @DecimalMin(value="1",message="The fleet size must be selected")
    private Integer             fleetSize = 0;
    
    private ResultType          resultType=ResultType.HANDICAP_RESULT;
    private	Integer             raceSeriesId=null;
}
