package com.runcible.abbot.service.points;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import com.runcible.abbot.model.PointsForBoat;

public class PointsForBoatComparator implements Comparator<PointsForBoat>
{
    @Override
    public int compare(PointsForBoat left, PointsForBoat right)
    {
        Float leftDrops = left.getTotalWithDrops();
        Float rightDrops = right.getTotalWithDrops();
        
        if (leftDrops.equals(rightDrops))
        {
            return countBack(left,right);
        }
        if ( leftDrops < rightDrops )
        {
            return -1;
        }
        else
        {
            return 1;
        }
    }

    private int countBack(PointsForBoat left, PointsForBoat right)
    {
        List<Float> leftPointsSorted = new ArrayList<Float>(left.getPoints());
        List<Float> rightPointsSorted = new ArrayList<Float>(right.getPoints());
        
        Collections.sort(leftPointsSorted);
        Collections.sort(rightPointsSorted);
        
        Iterator<Float> leftIter = leftPointsSorted.iterator();
        Iterator<Float> rightIter = rightPointsSorted.iterator();
        
        while(leftIter.hasNext() && rightIter.hasNext())
        {
            Float nextLeft = leftIter.next();
            Float nextRight = rightIter.next();
            
            if ( nextLeft < nextRight )
            {
                return 1;
            }
            else if ( nextLeft > nextRight )
            {
                return -1;
            }
        }
        
        return 0;
    }
}
