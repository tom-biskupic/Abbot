package com.runcible.abbot.model;

import java.util.List;

public class HandicapTable
{
    public HandicapTable()
    {
        
    }
    
    public HandicapTable(Fleet fleet)
    {
        this.fleet = fleet;
    }
    
    public List<Race> getRaces()
    {
        return this.races;
    }
    
    public void setRaces(List<Race> races)
    {
        this.races = races;
    }

    public List<BoatHandicaps> getBoatHandicaps()
    {
        return boatHandicaps;
    }

    public void setBoatHandicaps(List<BoatHandicaps> handicaps)
    {
        this.boatHandicaps = handicaps;
    }

    public Fleet getFleet()
    {
        return fleet;
    }

    public void setFleet(Fleet fleet)
    {
        this.fleet = fleet;
    }

    private List<Race> races;
    private List<BoatHandicaps> boatHandicaps;
    private Fleet fleet;
}
