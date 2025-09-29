package com.runcible.abbot.service.points;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.runcible.abbot.model.PointsForBoat;

@ExtendWith(MockitoExtension.class)
public class PointsForBoatComparatorTest
{

    @BeforeEach
    public void setUp()
    {
        boat1Points.setPoints(Arrays.asList(points1));
        boat1Points.setTotalWithDrops(points1Total);
        
        boat2Points.setPoints(Arrays.asList(points2));
        boat2Points.setTotalWithDrops(points2Total);

        boat3Points.setPoints(Arrays.asList(pointsCountBack));
        boat3Points.setTotalWithDrops(pointsCountBackTotal);
    }
    
    @Test
    public void testSimple()
    {
        assertEquals(-1,fixture.compare(boat1Points, boat2Points));
        assertEquals(1,fixture.compare(boat2Points, boat1Points));
        assertEquals(0,fixture.compare(boat1Points, boat1Points));
    }

    @Test
    public void testNoPointsTie()
    {
        PointsForBoat emptyPoints = new PointsForBoat();
        emptyPoints.setTotalWithDrops(1.0f);
        
        assertEquals(0,fixture.compare(emptyPoints, emptyPoints));
    }

    @Test
    public void testCountBack()
    {
        assertEquals(1,fixture.compare(boat3Points, boat1Points));
        assertEquals(-1,fixture.compare(boat1Points, boat3Points));
    }
    
    private static final Float[] points1 = new Float[] { 3.0f, 1.0f, 2.0f, 1.0f};
    private static final Float points1Total = 7.0f;
    
    private static final Float[] points2 = new Float[] { 5.0f, 2.0f, 2.0f, 1.0f};
    private static final Float points2Total = 10.0f;
    
    private static final Float[] pointsCountBack = new Float[] { 4.0f, 1.0f, 1.0f, 1.0f};
    private static final Float pointsCountBackTotal = 7.0f;
    
    private static final PointsForBoat boat1Points = new PointsForBoat();
    private static final PointsForBoat boat2Points = new PointsForBoat();
    private static final PointsForBoat boat3Points = new PointsForBoat();
    
    private PointsForBoatComparator fixture = new PointsForBoatComparator();
}
