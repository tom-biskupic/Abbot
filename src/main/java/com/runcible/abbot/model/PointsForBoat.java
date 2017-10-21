package com.runcible.abbot.model;

import java.util.ArrayList;
import java.util.List;

/**
 * This class holds the set of results for a competition for a single boat.
 *
 */
public class PointsForBoat
{
    public PointsForBoat()
    {
    }

    public PointsForBoat(Boat boat)
    {
        this.boat = boat;
    }
    
    public Boat getBoat()
    {
        return boat;
    }

    public void setBoat(Boat boat)
    {
        this.boat = boat;
    }

    public List<Float> getPoints()
    {
        return points;
    }

    public void setPoints(List<Float> value)
    {
        this.points = value;
    }

    private Boat        boat = null;
    private List<Float> points = new ArrayList<Float>();
}
