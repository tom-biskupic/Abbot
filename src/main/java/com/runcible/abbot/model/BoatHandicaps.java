package com.runcible.abbot.model;

import java.util.ArrayList;
import java.util.List;

/**
 * BoatHandicaps holds a list of handicaps for a given boat over an entire race
 * series.
 * @author tom
 *
 */
public class BoatHandicaps
{
    public BoatHandicaps()
    {
        
    }
    
    public BoatHandicaps(Boat boat)
    {
        this.boat = boat;
    }
    
    public Boat getBoat()
    {
        return this.boat;
    }

    public void setBoat(Boat boat)
    {
        this.boat = boat;
    }

    /**
     * Returns the handicaps for each boat for the race series.
     * The handicap order matches the list of completed races in 
     * the parent object
     * @return
     */
    public List<Float> getHandicaps()
    {
        return this.handicaps;
    }

    public void setHandicaps(List<Float> handicaps)
    {
        this.handicaps = handicaps;
    }

    private Boat        boat;
    private List<Float> handicaps = new ArrayList<Float>();
}

