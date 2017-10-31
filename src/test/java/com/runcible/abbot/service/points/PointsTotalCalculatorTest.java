package com.runcible.abbot.service.points;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import com.runcible.abbot.model.Competition;
import com.runcible.abbot.model.PointsForBoat;
import com.runcible.abbot.service.points.PointsTotalCalculatorImpl;

@RunWith(MockitoJUnitRunner.class)
public class PointsTotalCalculatorTest
{

    @Test
    public void testSimple()
    {
        PointsForBoat pointsForBoat = setupPointsForBoat();
        Competition competition = setupCompetition(1);
        
        fixture.updateTotals(competition, pointsForBoat);
        assertEquals(new Float(6.0f),pointsForBoat.getTotal());
        
        //
        //  Check the worse one got dropped
        //
        assertEquals(new Float(1.0+2.0),pointsForBoat.getTotalWithDrops());
    }

    @Test
    public void testMoreDropsThanPoints()
    {
        PointsForBoat pointsForBoat = setupPointsForBoat();
        Competition competition = setupCompetition(5);
        
        fixture.updateTotals(competition, pointsForBoat);
        assertEquals(new Float(6.0f),pointsForBoat.getTotal());
        
        assertEquals(new Float(0.0),pointsForBoat.getTotalWithDrops());
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
