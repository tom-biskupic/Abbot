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
 
    /**
     * Returns the boat this points list is for
     * @return the boat this points list relates to
     */
    public Boat getBoat()
    {
        return boat;
    }

    /**
     * Sets the boat this points list applies to
     * @param boat the boat this points list applies to
     */
    public void setBoat(Boat boat)
    {
        this.boat = boat;
    }

    /**
     * Returns the list of points for this boat. There is 
     * value for each race in the competition and the value
     * corresponds with the boats score for that race
     * 
     * @return list of points (one per race)
     */
    public List<Float> getPoints()
    {
        return points;
    }

    /**
     * Sets the list of points for this boat. There is 
     * value for each race in the competition and the value
     * corresponds with the boats score for that race

     * @param value list of points (one per race)
     */
    public void setPoints(List<Float> value)
    {
        this.points = value;
    }

    /**
     * Returns the total points for this boat
     * @return the total points
     */
    public Float getTotal()
    {
        return this.total;
    }

    /**
     * Sets the total points for this boat
     * @param value the total points
     */
    public void setTotal(Float value)
    {
        this.total = value;
    }

    /**
     * Returns the total after dropping the worst races (based on the
     * number of dropped races specified in the competition)
     * @return
     */
    public Float getTotalWithDrops()
    {
        return this.totalWithDrops;
    }
    
    /**
     * Sets the total after dropping the worst races (based on the
     * number of dropped races specified in the competition)
     * @param value The total with drops
     */
    public void setTotalWithDrops(Float value)
    {
        this.totalWithDrops = value;
    }
    
    /**
     * Returns the placing of this boat in the competition
     * @return This boats place
     */
    public int getPlace()
    {
        return place;
    }

    /**
     * Sets the placing of this boat in the competition
     * @param place this boats place
     */
    public void setPlace(int place)
    {
        this.place = place;
    }


    private Boat        boat = null;
    private List<Float> points = new ArrayList<Float>();
    private Float       total;
    private Float       totalWithDrops;
    private int         place;
}
