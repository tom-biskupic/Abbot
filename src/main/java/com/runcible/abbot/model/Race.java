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
            Set<Competition> 		competitions)
    {
        this(null,raceSeriesId,raceDate,name,fleet,competitions);
    }

    public Race(    
            Integer                 raceId, 
            Integer                 raceSeriesId,
            Date                    raceDate,
            String                  name,
            Fleet                   fleet,
            Set<Competition> competitions)
    {
        this();
        
        this.raceId = raceId;
        this.raceSeriesId = raceSeriesId;
        this.raceDate = raceDate;
        this.name = name;
        this.fleet = fleet;
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

//    @ManyToMany(cascade={CascadeType.ALL})
//    @JoinColumn(name="RACE_RESULTS_ID")
//    public Collection<RaceResultModel> getRaceResults()
//    {
//        return raceResults;
//    }
//
//    public void setRaceResults(Collection<RaceResultModel> raceResults)
//    {
//        this.raceResults = raceResults;
//    }

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

    private   Integer                       raceId;
    private   Integer                       raceSeriesId;
    private   Date                          raceDate;
    private   String                        name;
    private   Fleet                         fleet;
    //private   Collection<RaceResultModel>   raceResults;
    private   RaceStatus                    raceStatus = RaceStatus.NOT_RUN;
    private   Set<Competition>              competitions;
}
