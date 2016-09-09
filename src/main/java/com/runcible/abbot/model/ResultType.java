package com.runcible.abbot.model;

public enum ResultType
{
    SCRATCH_RESULT,
    HANDICAP_RESULT;
    
    private static final String[] DISPLAY_STRING = 
    {
        "Scratch",
        "Handicap"
    };
    
    public String toDisplayString()
    {
        return DISPLAY_STRING[ordinal()];
    }
    
    public static ResultType fromDisplayString(String value)
    {
        for( ResultType p : ResultType.values())
        {
            if ( p.toDisplayString().equals(value) )
            {
                return p;
            }
        }
        
        throw new IllegalArgumentException("Invalid ResultType value "+value);
    }

}
