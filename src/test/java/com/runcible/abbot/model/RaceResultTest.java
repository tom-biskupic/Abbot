package com.runcible.abbot.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class RaceResultTest extends ValidationTest
{
    @BeforeEach
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
    public void testValidationOk()
    {
        assertEquals(0,validate(makeResult()).size());
    }

    private RaceResult makeResult()
    {
        Boat boat = new Boat();
        return new RaceResult(testID,boat,testHandicap,false,testStart,testFinish,testStatus);
    }

    private Integer         testID = 1234;
    private Float           testHandicap = 12.0f;
    private Date            testStart = new Date();
    private Date            testFinish = new Date();
    private ResultStatus    testStatus = ResultStatus.DNC;
}
