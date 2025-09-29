package com.runcible.abbot.service.points;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.runcible.abbot.model.PointsForBoat;
import com.runcible.abbot.model.PointsTable;

@ExtendWith(MockitoExtension.class)
public class PointsSorterTest
{
    @Test
    public void testSimple()
    {
        PointsForBoat testPoints1 = new PointsForBoat();
        testPoints1.setTotalWithDrops(Float.valueOf(1.0f));
        
        PointsForBoat testPoints2 = new PointsForBoat();
        testPoints2.setTotalWithDrops(Float.valueOf(3.0f));
        
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
        testPoints1.setTotalWithDrops(Float.valueOf(1.0f));
        
        PointsForBoat testPoints2 = new PointsForBoat();
        testPoints2.setTotalWithDrops(Float.valueOf(1.0f));

        PointsForBoat testPoints3 = new PointsForBoat();
        testPoints3.setTotalWithDrops(Float.valueOf(2.0f));

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
