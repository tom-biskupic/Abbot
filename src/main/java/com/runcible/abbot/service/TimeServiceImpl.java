package com.runcible.abbot.service;

import java.util.Calendar;
import java.util.Date;

import org.springframework.stereotype.Component;

@Component
public class TimeServiceImpl implements TimeService
{

    @Override
    public Integer subtractTime(Date start, Date finish)
    {
        Calendar startCal = getCal(start);
        Calendar resultCal = getCal(finish);
        
        subtractUnit(resultCal, startCal, Calendar.HOUR_OF_DAY);
        subtractUnit(resultCal, startCal, Calendar.MINUTE);
        subtractUnit(resultCal, startCal, Calendar.SECOND);
        
        return resultCal.get(Calendar.HOUR_OF_DAY)*3600 + resultCal.get(Calendar.MINUTE)*60
                    + resultCal.get(Calendar.SECOND);
    }

    private Calendar getCal(Date date)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal;
    }
    
    //
    //  Subtracts the specified from cal1. So if unitis Calendar.MINUTE then
    //  this will subtract the number of minutes in cal2 from cal1
    //
    private void subtractUnit( Calendar cal1, Calendar cal2, int unit)
    {
        cal1.set(unit,cal1.get(unit)-cal2.get(unit));
    }
}
