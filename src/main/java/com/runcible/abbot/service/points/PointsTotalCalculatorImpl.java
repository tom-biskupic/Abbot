package com.runcible.abbot.service.points;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Component;

import com.runcible.abbot.model.Competition;
import com.runcible.abbot.model.PointsForBoat;

@Component
public class PointsTotalCalculatorImpl implements PointsTotalCalculator
{

    @Override
    public void updateTotals(Competition competition, PointsForBoat boatPoints)
    {
        boatPoints.setTotal(sumList(boatPoints.getPoints()));
        
        List<Float> pointsAfterDrops = new ArrayList<Float>(boatPoints.getPoints());
        
        Collections.sort(pointsAfterDrops);
        Collections.reverse(pointsAfterDrops);
        
        for(int i=0;i<competition.getDrops();i++)
        {
            if ( pointsAfterDrops.isEmpty() )
            {
                break;
            }
            pointsAfterDrops.remove(0);
        }
        
        boatPoints.setTotalWithDrops(sumList(pointsAfterDrops));
    }

    private Float sumList(List<Float> floats)
    {
        Float total = new Float(0.0f);

        for(Float points : floats)
        {
            total += points;
        }
        
        return total;
    }

}
