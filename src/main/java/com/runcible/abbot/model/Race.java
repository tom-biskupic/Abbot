package com.runcible.abbot.model;

import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * A RaceModel represent a boat race on a given day for a single class. 
 * Each boat race records results for each registered boat in that class 
 * for that day.
 * 
 * Each race can contribute points to one or more competitions.
 */
@Entity
@Table(name="RACE")
public class Race implements ModelWithId
{
    public Race()
    {
    }

    public Race(
            Integer                 raceSeriesId,
            Date                    raceDate,
            String                  name,
            Fleet                   fleet,
            boolean                 shortCourse,
            Set<Competition> 		competitions)
    {
        this(null,raceSeriesId,raceDate,name,fleet,shortCourse,competitions);
    }

    public Race(    
            Integer                 raceId, 
            Integer                 raceSeriesId,
            Date                    raceDate,
            String                  name,
            Fleet                   fleet,
            boolean                 shortCourse,
            Set<Competition> competitions)
    {
        this();
        
        this.raceId = raceId;
        this.raceSeriesId = raceSeriesId;
        this.raceDate = raceDate;
        this.name = name;
        this.fleet = fleet;
        this.shortCourseRace = shortCourse;
        this.competitions = competitions;
    }

    @Id
    @GeneratedValue
    @Column(name="RACE_ID")    
    public Integer getId()
    {
        return raceId;
    }

    public void setId(Integer raceId)
    {
        this.raceId = raceId;
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

    @Column(name="RACE_DATE")
    public Date getRaceDate()
    {
        return raceDate;
    }

    public void setRaceDate(Date raceDate)
    {
        this.raceDate = raceDate;
    }

    @Column(name="NAME")
    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
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

    public RaceStatus getRaceStatus()
    {
        return raceStatus;
    }

    public void setRaceStatus(RaceStatus raceStatus)
    {
        this.raceStatus = raceStatus;
    }
    
    @ManyToMany(fetch=FetchType.EAGER)
    @JoinColumn(name="COMPETITION")
    public Set<Competition> getCompetitions()
    {
        return competitions;
    }
    
    public void setCompetitions(Set<Competition> competitions)
    {
        this.competitions = competitions;
    }
    
    @Transient
    public String getCompetitionsAsString()
    {
        String result = "";
        
        for(Competition competition : competitions)
        {
            if ( result.isEmpty() )
            {
                result += competition.getName();
            }
            else
            {
                result += "," + competition.getName();
            }
        }
        return result;
    }

    public void removeFromCompetition(Competition competition)
    {
        if ( competitions.contains(competition) )
        {
            competitions.remove(competition);        
        }
    }

    public void addToCompetition(Competition competition)
    {
        if ( ! competitions.contains(competition))
        {
            competitions.add(competition);
        }
    }

    /**
     * Returns true if this is a short-course race. Used to distinguish short course
     * races from normal races in cases where we have a race series that is a mixture
     * @return true if this is a short course race
     */
    public boolean isShortCourseRace()
    {
        return shortCourseRace;
    }

    /**
     * Returns true if this is a short-course race. Used to distinguish short course
     * races from normal races in cases where we have a race series that is a mixture
     * @param shortCourseRace true if this is a short course race 
     * 
     */
    public void setShortCourseRace(boolean shortCourseRace)
    {
        this.shortCourseRace = shortCourseRace;
    }

    private   Integer                       raceId;
    private   Integer                       raceSeriesId;
    
    @NotNull(message="A race date is required")
    private   Date                          raceDate;
    
    private   String                        name;
    
    @NotNull(message="A fleet must be selected")
    private   Fleet                         fleet;

    private   RaceStatus                    raceStatus = RaceStatus.NOT_RUN;
    
    @Size(min=1,message="At least one competition must be selected")
    private   Set<Competition>              competitions;
    
    private   boolean                       shortCourseRace;
}
