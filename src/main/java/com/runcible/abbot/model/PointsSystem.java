package com.runcible.abbot.model;

public enum PointsSystem
{
    LOW_POINTS,
    BONUS_POINTS;
    
    private static final String[] DISPLAY_STRINGS =
    {
        "Low Points System",
        "Bonus Points System"
    };
    
    public String toDisplayString()
    {
        return DISPLAY_STRINGS[this.ordinal()];
    }
    
    public static PointsSystem fromDisplayString(String value)
    {
        for( PointsSystem p : PointsSystem.values())
        {
            if ( p.toDisplayString().equals(value) )
            {
                return p;
            }
        }
        
        throw new IllegalArgumentException("Invalid PointsSystem value "+value);
    }
}
