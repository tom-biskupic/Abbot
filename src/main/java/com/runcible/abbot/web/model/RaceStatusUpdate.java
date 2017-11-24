package com.runcible.abbot.web.model;

import com.runcible.abbot.model.RaceStatus;
import com.runcible.abbot.model.ResultStatus;

public class RaceStatusUpdate
{

    public RaceStatus getRaceStatus()
    {
        return raceStatus;
    }
    
    public void setRaceStatus(RaceStatus raceStatus)
    {
        this.raceStatus = raceStatus;
    }
    
    public boolean getAddDNSBoats()
    {
        return addDNSBoats;
    }
    
    public void setAddDNSBoats(boolean addDNSBoats)
    {
        this.addDNSBoats = addDNSBoats;
    }
    
    public ResultStatus getResultStatusForNonStarters()
    {
        return resultStatusForNonStarters;
    }
    
    public void setResultStatusForNonStarters(
            ResultStatus resultStatusForNonStarters)
    {
        this.resultStatusForNonStarters = resultStatusForNonStarters;
    }
    
    public boolean getUpdateHandicaps()
    {
        return updateHandicaps;
    }
    
    public void setUpdateHandicaps(boolean updateHandicaps)
    {
        this.updateHandicaps = updateHandicaps;
    }
    
    private RaceStatus      raceStatus;
    private boolean         addDNSBoats;
    private ResultStatus    resultStatusForNonStarters;
    private boolean         updateHandicaps;
}
