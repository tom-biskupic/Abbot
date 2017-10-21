package com.runcible.abbot.service;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.runcible.abbot.model.BoatClass;
import com.runcible.abbot.model.Fleet;
import com.runcible.abbot.model.Race;
import com.runcible.abbot.model.RaceSeries;
import com.runcible.abbot.service.exceptions.NoSuchUser;
import com.runcible.abbot.service.exceptions.UserNotPermitted;

@Ignore
@RunWith(SpringRunner.class)
@DataJpaTest
public class RaceServiceIntegrationTest
{
    @Before
    public void setUp() throws NoSuchUser, UserNotPermitted
    {
        RaceSeries series = new RaceSeries();
        series.setName("Who Cares");
        raceSeriesService.add(series);
        
        BoatClass boatClass = new BoatClass();
        boatClass.setName(testBoatClassName);
        
        boatClassService.addBoatClass(series.getId(), boatClass);
        Fleet fleet = new Fleet();
        
        Race testRace = new Race();
        testRace.setFleet(fleet);
    }
    
    @Test
    public void testFindRacesForCompetition()
    {
        
    }
    
    private static final String testBoatClassName = "Laser";
    
    @MockBean
    private RaceSeriesAuthorizationService mockAuthService;

    @MockBean
    private LoggedOnUserService mockLoggedOnUser;
    
    @Autowired
    private FleetService fleetService;
    
    @Autowired
    private BoatClassService boatClassService;

    @Autowired
    private RaceSeriesService raceSeriesService;

}

