package com.runcible.abbot.service.points;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.runcible.abbot.model.Competition;
import com.runcible.abbot.model.PointsSystem;
import com.runcible.abbot.model.RaceResult;
import com.runcible.abbot.model.ResultStatus;
import com.runcible.abbot.service.points.PointsCalculator;
import com.runcible.abbot.service.points.PointsCalculatorImpl;

@ExtendWith(MockitoExtension.class)
public class PointsCalculatorTest
{
    enum FinishState { DNS, DNF, FINISHED };
    
    @Test
    public void testLowPointsFinished()
    {
        setupMocks(PointsSystem.LOW_POINTS, FinishState.FINISHED, 20);
        
        assertEquals(Float.valueOf(1.0f),fixture.calculatePoints(mockCompetition, 10, 1, ResultStatus.FINISHED));
        assertEquals(Float.valueOf(2.0f),fixture.calculatePoints(mockCompetition, 10, 2, ResultStatus.FINISHED));
        assertEquals(Float.valueOf(10.0f),fixture.calculatePoints(mockCompetition, 10, 10, ResultStatus.FINISHED));
    }

    @Test
    public void testBonusPointsFinished()
    {
        setupMocks(PointsSystem.BONUS_POINTS, FinishState.FINISHED, 20);
        
        assertEquals(Float.valueOf(0.0f),fixture.calculatePoints(mockCompetition, 10, 1, ResultStatus.FINISHED));
        assertEquals(Float.valueOf(3.0f),fixture.calculatePoints(mockCompetition, 10, 2, ResultStatus.FINISHED));
        assertEquals(Float.valueOf(5.7f),fixture.calculatePoints(mockCompetition, 10, 3, ResultStatus.FINISHED));
        assertEquals(Float.valueOf(8.0f),fixture.calculatePoints(mockCompetition, 10, 4, ResultStatus.FINISHED));
        assertEquals(Float.valueOf(10.0f),fixture.calculatePoints(mockCompetition, 10, 5, ResultStatus.FINISHED));
        assertEquals(Float.valueOf(11.7f),fixture.calculatePoints(mockCompetition, 10, 6, ResultStatus.FINISHED));
        assertEquals(Float.valueOf(13.0f),fixture.calculatePoints(mockCompetition, 10, 7, ResultStatus.FINISHED));
        assertEquals(Float.valueOf(14.0f),fixture.calculatePoints(mockCompetition, 10, 8, ResultStatus.FINISHED));
    }

    @Test
    public void testLowPointsDNS()
    {
        setupMocks(PointsSystem.LOW_POINTS,FinishState.DNS, 20);
        
        assertEquals(Float.valueOf(21.0f),fixture.calculatePoints(mockCompetition, 10, 1, ResultStatus.DNS));
    }

    @Test
    public void testBonusPointsDNS()
    {
        setupMocks(PointsSystem.BONUS_POINTS,FinishState.DNS, 20);
        
        //
        //  Fleet size is 20 so result is 20+6+1
        //
        assertEquals(Float.valueOf(27.0f),fixture.calculatePoints(mockCompetition, 10, 1, ResultStatus.DNS));
    }

    @Test
    public void testBonusPointsDNS2()
    {
        setupMocks(PointsSystem.BONUS_POINTS,FinishState.DNS, 5);
        
        //
        //  Fleet size is 5 so result is 5+1 converted which is 
        //
        assertEquals(Float.valueOf(11.7f),fixture.calculatePoints(mockCompetition, 10, 1, ResultStatus.DNS));
    }

    @Test
    public void testLowPointsDNF()
    {
        setupMocks(PointsSystem.LOW_POINTS,FinishState.DNF, 20);
        
        assertEquals(Float.valueOf(11.0f),fixture.calculatePoints(mockCompetition, 10, 1, ResultStatus.DNF));
    }

    @Test
    public void testBonusPointsDNF()
    {
        setupMocks(PointsSystem.BONUS_POINTS,FinishState.DNF, 20);
        
        //
        //  10 starters so result is 10+1+6
        //
        assertEquals(Float.valueOf(17.0f),fixture.calculatePoints(mockCompetition, 10, 1, ResultStatus.DNF));
    }

    @Test
    public void testBonusPointsDNF2()
    {
        setupMocks(PointsSystem.BONUS_POINTS,FinishState.DNF, 20);
        
        //
        //  10 starters so result is 3+1 converted which is 11.7
        //
        assertEquals(Float.valueOf(8.0f),fixture.calculatePoints(mockCompetition, 3, 1, ResultStatus.DNF));
    }

    private void setupMocks(PointsSystem pointsSystem, FinishState finishState, int fleetSize)
    {
        when(mockCompetition.getPointsSystem()).thenReturn(pointsSystem);
        if (finishState == FinishState.DNS)
        {
            when(mockCompetition.getFleetSize()).thenReturn(fleetSize);
        }
    }
    
    
    @Mock Competition   mockCompetition;
    @Mock RaceResult    mockResult;
    
    PointsCalculator fixture = new PointsCalculatorImpl();
}
