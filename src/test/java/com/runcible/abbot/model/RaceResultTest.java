package com.runcible.abbot.model;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;

public class RaceResultTest extends ValidationTest
{
    @Before
    public void setup() 
    {
        setupValidation();
    }

    @Test
    public void testBoatModelRequired()
    {
        RaceResult result = makeResult();
        result.setBoat(null);
        
        assertEquals(1,validate(result).size());    
    }

    
    @Test
    public void testStartTimeRequired()
    {
        RaceResult result = makeResult();
        result.setStartTime(null);
        
        assertEquals(1,validate(result).size());    
    }

    @Test
    public void testFinishTimeRequired()
    {
        RaceResult result = makeResult();
        result.setFinishTime(null);
        
        assertEquals(1,validate(result).size());
    }

    @Test
    public void testValidationOk()
    {
        assertEquals(0,validate(makeResult()).size());
    }

    private RaceResult makeResult()
    {
        Boat boat = new Boat();
        return new RaceResult(testID,boat,testHandicap,testStart,testFinish,testStatus);
    }

    private Integer testID = 1234;
    private Integer testHandicap = 12;
    private Date testStart = new Date();
    private Date testFinish = new Date();
    private ResultStatus testStatus = ResultStatus.DNC;
}
