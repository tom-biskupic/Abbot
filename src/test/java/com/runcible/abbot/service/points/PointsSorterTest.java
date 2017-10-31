package com.runcible.abbot.service.points;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import com.runcible.abbot.model.PointsForBoat;
import com.runcible.abbot.model.PointsTable;

@RunWith(MockitoJUnitRunner.class)
public class PointsSorterTest
{
    @Test
    public void testSimple()
    {
        PointsForBoat testPoints1 = new PointsForBoat();
        testPoints1.setTotalWithDrops(new Float(1.0));
        
        PointsForBoat testPoints2 = new PointsForBoat();
        testPoints2.setTotalWithDrops(new Float(3.0));
        
        PointsTable table = new PointsTable(null);
        table.getPointsForBoat().add(testPoints1);
        table.getPointsForBoat().add(testPoints2);
        
        fixture.sortPoints(table);
        assertEquals(testPoints1,table.getPointsForBoat().get(0));
        assertEquals(1,table.getPointsForBoat().get(0).getPlace());
        
        assertEquals(testPoints2,table.getPointsForBoat().get(1));
        assertEquals(2,table.getPointsForBoat().get(1).getPlace());

    }
    
    @Test
    public void testTie()
    {
        PointsForBoat testPoints1 = new PointsForBoat();
        testPoints1.setTotalWithDrops(new Float(1.0));
        
        PointsForBoat testPoints2 = new PointsForBoat();
        testPoints2.setTotalWithDrops(new Float(1.0));

        PointsForBoat testPoints3 = new PointsForBoat();
        testPoints3.setTotalWithDrops(new Float(2.0));

        PointsTable table = new PointsTable(null);
        table.getPointsForBoat().add(testPoints1);
        table.getPointsForBoat().add(testPoints2);
        table.getPointsForBoat().add(testPoints3);
        
        fixture.sortPoints(table);
        assertEquals(testPoints1,table.getPointsForBoat().get(0));
        assertEquals(1,table.getPointsForBoat().get(0).getPlace());
        
        assertEquals(testPoints2,table.getPointsForBoat().get(1));
        assertEquals(1,table.getPointsForBoat().get(1).getPlace());

        assertEquals(testPoints3,table.getPointsForBoat().get(2));
        assertEquals(3,table.getPointsForBoat().get(2).getPlace());
    }

    private PointsSorter fixture = new PointsSorterImpl();
}
