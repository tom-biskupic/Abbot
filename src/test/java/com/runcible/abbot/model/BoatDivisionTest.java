package com.runcible.abbot.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class BoatDivisionTest extends ValidationTest
{
    @BeforeEach
    public void setUp()
    {
        setupValidation();
    }
    
    @Test
    public void testNameRequired()
    {
        BoatDivision fixture = makeBoatDivision();
        fixture.setName("");
        assertEquals(1,validate(fixture).size());
    }

    @Test
    public void testValidationOk()
    {
        assertEquals(0,validate(makeBoatDivision()).size());
    }

    private BoatDivision makeBoatDivision()
    {
        return new BoatDivision(testName,1234.0f);
    }
    
    private static final String testName = "Name";
}
