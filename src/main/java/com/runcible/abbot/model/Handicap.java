package com.runcible.abbot.model;

import javax.persistence.ManyToMany;

/**
 * Models the current handicap of a boat in a competition.
 */
public class Handicap
{
    
    public Handicap(Boat boat, Competition competition, Float handicap)
    {
        super();
        this.boat = boat;
        this.competition = competition;
        this.handicap = handicap;
    }

    /**
     * Returns the boat this handicap applies to
     * @return The boat this handicap applies to
     */
    @ManyToMany
    public Boat getBoat()
    {
        return boat;
    }
    
    /**
     * Sets the boat this handicap applies to
     * @param boat the boat this handicap applies to
     */
    public void setBoat(Boat boat)
    {
        this.boat = boat;
    }
    
    /**
     * Returns the competition this handicap applies to
     * @return the competition this handicap applies to
     */
    public Competition getCompetition()
    {
        return competition;
    }
    
    /**
     * Sets the competition this handicap applies to
     * @param competition the competition this handicap applies to
     */
    public void setCompetition(Competition competition)
    {
        this.competition = competition;
    }
    
    private Boat        boat;
    private Competition competition;
    private Float       handicap;
}
