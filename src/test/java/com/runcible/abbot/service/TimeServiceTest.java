package com.runcible.abbot.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Calendar;
import java.util.Date;

import org.junit.jupiter.api.Test;

import com.runcible.abbot.service.TimeService;
import com.runcible.abbot.service.TimeServiceImpl;

public class TimeServiceTest
{
    @Test
    public void testSubtractTime()
    {
        Date testStartDate = new Date();
        System.out.println(testStartDate.toString());
        
        Calendar finishCal = Calendar.getInstance();
        finishCal.setTime(testStartDate);
        finishCal.set(Calendar.HOUR_OF_DAY, finishCal.get(Calendar.HOUR_OF_DAY)+testHours);
        finishCal.set(Calendar.MINUTE, finishCal.get(Calendar.MINUTE)+testMinutes);
        finishCal.set(Calendar.SECOND, finishCal.get(Calendar.SECOND)+testSeconds);
        
        System.out.println(finishCal.getTime().toString());
        
        Integer result = fixture.subtractTime(testStartDate, finishCal.getTime());

        Integer expectedTotal = testHours*3600 + testMinutes * 60 + testSeconds;
        assertEquals(expectedTotal,result);
    }
    
    private static final int testMinutes = 59;
    private static final int testHours = 12;
    private static final int testSeconds = 59;
    
    private TimeService fixture = new TimeServiceImpl();
}
