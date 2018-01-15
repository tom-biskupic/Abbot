package com.runcible.abbot.model;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

public class RaceModelTest extends ValidationTest
{

    @Before
    public void setup() 
    {
        setupValidation();
    }

    @Test
    public void testRaceDateRequired()
    {
        Race fixture = makeRace();
        fixture.setRaceDate(null);
        assertEquals(1,validate(fixture).size()); 
    }

    @Test
    public void testFleetRequired()
    {
        Race fixture = makeRace();
        fixture.setFleet(null);
        assertEquals(1,validate(fixture).size()); 
    }

    @Test
    public void testCompetitionRequired()
    {
        Race fixture = makeRace();
        fixture.getCompetitions().clear();
        assertEquals(1,validate(fixture).size()); 
    }

    private Race makeRace()
    {
        Set<Competition> competitions = new HashSet<Competition>();
        competitions.add(new Competition());
        
        Race fixture = new Race(0,0,new Date(),"Name",new Fleet(),false, competitions);
        
        return fixture;
    }
}
