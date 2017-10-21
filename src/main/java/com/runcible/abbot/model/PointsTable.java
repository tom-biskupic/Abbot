package com.runcible.abbot.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The Pointstable contains the complete points results for a competition
 * 
 */
public class PointsTable
{
    public PointsTable(Competition competition)
    {
        this.competition = competition;
    }
    
    public List<Race> getRaces()
    {
        return this.races;
    }
    
    public void setRaces(List<Race> races)
    {
        this.races = races;
    }
    
    public List<PointsForBoat> getPointsForBoat()
    {
        return pointsForBoat;
    }

    public void setPointsForBoat(List<PointsForBoat> pointsForBoat)
    {
        this.pointsForBoat = pointsForBoat;
    }

    public Competition getCompetition()
    {
        return competition;
    }

    public void setCompetition(Competition competition)
    {
        this.competition = competition;
    }

    private List<Race>              races = new ArrayList<Race>();
    private List<PointsForBoat>     pointsForBoat = new ArrayList<PointsForBoat>();
    
    private Competition competition;
}
