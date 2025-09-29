package com.runcible.abbot.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class BoatClassTest extends ValidationTest
{
    @BeforeEach
    public void setup() 
    {
        setupValidation();
    }


    @Test
    public void testNameRequired()
    {
        BoatClass fixture = makeBoatClass();
        fixture.setName("");
        assertEquals(1,validate(fixture).size());
    }

    @Test
    public void testValidationOk()
    {
        assertEquals(0,validate(makeBoatClass()).size());
    }

    private BoatClass makeBoatClass()
    {
        return new BoatClass(1,testName,112.0f);
    }
    
    private static final String testName = "Laser";
}
