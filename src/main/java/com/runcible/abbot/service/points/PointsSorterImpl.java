package com.runcible.abbot.service.points;

import java.util.Collections;

import org.springframework.stereotype.Component;

import com.runcible.abbot.model.PointsForBoat;
import com.runcible.abbot.model.PointsTable;

@Component
public class PointsSorterImpl implements PointsSorter
{
    @Override
    public void sortPoints(PointsTable pointsTable)
    {
        PointsForBoatComparator pointsForBoatComparator = new PointsForBoatComparator();
        Collections.sort(pointsTable.getPointsForBoat(),pointsForBoatComparator);
        
        int place = 1;
        PointsForBoat previousBoatPoints = null;
        
        for(PointsForBoat pointsForBoat : pointsTable.getPointsForBoat())
        {
            if ( previousBoatPoints != null )
            {
                //
                //  Was this boat tied with the previous? If so they also get the previous
                //  boats place
                //
                if ( pointsForBoatComparator.compare(pointsForBoat, previousBoatPoints) == 0)
                {
                    pointsForBoat.setPlace(previousBoatPoints.getPlace());
                }
                else
                {
                    pointsForBoat.setPlace(place);
                }
            }
            else
            {
                pointsForBoat.setPlace(place);
            }
            
            place++;
            previousBoatPoints = pointsForBoat;
        }
    }
}
