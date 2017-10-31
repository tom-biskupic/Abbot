package com.runcible.abbot.service.points;

import com.runcible.abbot.model.PointsTable;

public interface PointsSorter
{
    /**
     * This sorts the boats in the points table by points. In case
     * of a tie it performs a count-back to try and find a winner.
     * 
     * Each BoatPoints entry is updated with the boats placing.
     * 
     * @param pointsTable The table to sort
     */
    public void sortPoints( PointsTable pointsTable );
}
