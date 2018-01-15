package com.runcible.abbot.controllers;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.runcible.abbot.model.Boat;
import com.runcible.abbot.model.BoatClass;
import com.runcible.abbot.model.Competition;
import com.runcible.abbot.model.Fleet;
import com.runcible.abbot.model.FleetSelector;
import com.runcible.abbot.model.Handicap;
import com.runcible.abbot.model.HandicapLimit;
import com.runcible.abbot.model.PointsSystem;
import com.runcible.abbot.model.Race;
import com.runcible.abbot.model.RaceDay;
import com.runcible.abbot.model.RaceSeries;
import com.runcible.abbot.model.RaceSeriesType;
import com.runcible.abbot.model.ResultType;
import com.runcible.abbot.model.User;

public class MvcTestWithJSON
{

    public MvcTestWithJSON()
    {
        super();
        
        competitionSet.add(testCompetition);
        Date testRaceTime = Calendar.getInstance().getTime();
		testRace = new Race(
                TEST_RACE_ID,
                TEST_RACE_SERIES_ID,
                testRaceTime,
                TEST_RACE_NAME,
                testFleet,
                false,
                competitionSet);
        
		testRaceDayList.add(new RaceDay(testRaceTime));
		
		testHandicapList.add(testHandicap);
    }

    public byte[] convertObjectToJsonBytes(Object object) throws IOException
    {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return mapper.writeValueAsBytes(object);
    }

    protected void setupMockMVC()
    {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    protected MediaType contentType = new MediaType(
            MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));
    
    protected static final Integer  TEST_ID = 1234;
    protected static final String   TEST_NAME = "Fred Bloggs";
    protected static final String   TEST_EMAIL = "fred@nowhere.com";

    protected static final Integer  TEST_RACE_SERIES_ID = 123;
    protected static final Integer  TEST_HANDICAP_LIMIT_ID = 444;
    
    protected static final String FIRST_NAME="Fred";
    protected static final String LAST_NAME="Bloggs";
    protected static final String EMAIL="fred@nowhere.com";
    protected static final String PASSWORD="Password01!";
    protected static final String ORGANISATION="Nowhere Inc";
    protected static final boolean IS_ADMIN=false;
    
    protected User user = new User(EMAIL,PASSWORD,FIRST_NAME,LAST_NAME,ORGANISATION,IS_ADMIN);
    
    protected static final String SERIES_NAME = "2015/2016 Season";
    protected static final RaceSeriesType SERIES_TYPE = RaceSeriesType.SEASON;
    protected static final String COMMENT = "Comment";
    
    protected RaceSeries raceSeries = new RaceSeries(SERIES_TYPE,SERIES_NAME,COMMENT);

    protected static final Integer TEST_BOAT_CLASS_ID=999;
    protected static final String  TEST_BOAT_CLASS_NAME="Sabot";
    protected static final Float   TEST_BOAT_CLASS_YARDSTICK=123.0f;
    
    protected static final BoatClass testBoatClass = new BoatClass(
            TEST_BOAT_CLASS_ID,
            TEST_RACE_SERIES_ID, 
            TEST_BOAT_CLASS_NAME, 
            TEST_BOAT_CLASS_YARDSTICK);
    
    protected static final Integer TEST_BOAT_ID=334455;
    protected static final String  TEST_SAIL_NUMBER="3072";
    protected static final String  TEST_BOAT_NAME="A Grand Day Out";
    protected static final String  TEST_BOAT_SKIPPER="Fred";
    protected static final String  TEST_BOAT_CREW="Fred";
    
    protected static final Boat testBoat = new Boat(
            TEST_BOAT_ID,
            TEST_RACE_SERIES_ID,
            TEST_BOAT_NAME,
            TEST_SAIL_NUMBER,
            testBoatClass,
            null,
            false,
            TEST_BOAT_SKIPPER,
            TEST_BOAT_CREW);
    
    protected static final Integer               TEST_FLEET_ID = 24;
    protected static final String                TEST_FLEET_NAME="Sabots";
    protected static final Set<FleetSelector>    TEST_FLEET_SELECTORS = new HashSet<FleetSelector>(
            Arrays.asList(new FleetSelector(testBoatClass, null)));
    
    protected static final Fleet testFleet = new Fleet(
            TEST_FLEET_ID, TEST_RACE_SERIES_ID,TEST_FLEET_NAME,TEST_FLEET_SELECTORS);

    protected static final Integer  TEST_COMPETITION_ID = 71;
    protected static final String   TEST_COMPETITION_NAME = "Sabot Season Points";
    protected static final PointsSystem TEST_POINTS_SYSTEM = PointsSystem.LOW_POINTS;
    protected static final Integer  TEST_COMPETITION_DROPS = 6;
    protected static final ResultType TEST_RESULT_TYPE = ResultType.HANDICAP_RESULT;
    
    protected Competition testCompetition = new Competition(
            TEST_COMPETITION_ID,
            TEST_COMPETITION_NAME,
            TEST_RACE_SERIES_ID,
            TEST_POINTS_SYSTEM,
            TEST_COMPETITION_DROPS,
            TEST_RESULT_TYPE,
            testFleet);
            
    protected Integer TEST_RACE_ID = 1212;
    protected String  TEST_RACE_NAME="The Muppet's Trophy";

    protected Set<Competition> competitionSet = new HashSet<Competition>();
    
    protected Race testRace;

    protected List<RaceDay> testRaceDayList = new ArrayList<RaceDay>();
    
    protected static final Float TEST_HANDICAP_LIMIT_VALUE = 34.0f;
    
    protected static final HandicapLimit testHandicapLimit = new HandicapLimit(
            TEST_HANDICAP_LIMIT_ID,
            TEST_RACE_SERIES_ID,
            testFleet,
            TEST_HANDICAP_LIMIT_VALUE);
 
    protected static final Integer  TEST_HANDICAP_ID = 999;
    protected static final Float    TEST_HANDICAP_VALUE = 1.0f;
    protected static final Handicap testHandicap 
        = new Handicap(TEST_HANDICAP_ID,TEST_BOAT_ID,TEST_HANDICAP_VALUE);
    
    protected List<Handicap> testHandicapList = new ArrayList<Handicap>();
    
    @Autowired
    protected WebApplicationContext wac;

    protected MockMvc mockMvc;
}