package com.runcible.abbot.service;

import java.util.Date;

public interface TimeService
{
    /**
     * Subtracts start time from finish time and returns the result. Assumes both
     * are on the same day.
     * @param start The start time
     * @param finish The finish time
     * @return finish minus start
     */
    public Integer subtractTime(Date start, Date finish);
    
}
