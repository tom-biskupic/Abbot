package com.runcible.abbot.service.points;

import com.runcible.abbot.model.Competition;
import com.runcible.abbot.model.PointsForBoat;

public interface PointsTotalCalculator
{
    /**
     * Calculates the total points for the boat and calculates the total with
     * drops (based on the number of drops specified in the competition).
     * @param competition The competition these results come under
     * @param boatPoints The boat points to update.
     */
    public void updateTotals(Competition competition, PointsForBoat boatPoints);
}
