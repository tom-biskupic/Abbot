package com.runcible.abbot.service.points;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.runcible.abbot.model.Competition;
import com.runcible.abbot.model.PointsForBoat;

@ExtendWith(MockitoExtension.class)
public class PointsTotalCalculatorTest
{

    @Test
    public void testSimple()
    {
        PointsForBoat pointsForBoat = setupPointsForBoat();
        Competition competition = setupCompetition(1);
        
        fixture.updateTotals(competition, pointsForBoat);
        assertEquals(Float.valueOf(6.0f),pointsForBoat.getTotal());
        
        //
        //  Check the worse one got dropped
        //
        assertEquals(Float.valueOf(1.0f+2.0f),pointsForBoat.getTotalWithDrops());
    }

    @Test
    public void testMoreDropsThanPoints()
    {
        PointsForBoat pointsForBoat = setupPointsForBoat();
        Competition competition = setupCompetition(5);
        
        fixture.updateTotals(competition, pointsForBoat);
        assertEquals(Float.valueOf(6.0f),pointsForBoat.getTotal());
        
        assertEquals(Float.valueOf(0.0f),pointsForBoat.getTotalWithDrops());
    }

    private Competition setupCompetition(int drops)
    {
        Competition competition = new Competition();
        competition.setDrops(drops);
        return competition;
    }

    private PointsForBoat setupPointsForBoat()
    {
        Float[] points = new Float[] {1.0f,3.0f,2.0f};
        
        PointsForBoat pointsForBoat = new PointsForBoat();
        pointsForBoat.setPoints(Arrays.asList(points));
        return pointsForBoat;
    }


    private PointsTotalCalculatorImpl fixture = new PointsTotalCalculatorImpl();
}
