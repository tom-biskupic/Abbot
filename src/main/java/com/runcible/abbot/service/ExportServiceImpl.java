package com.runcible.abbot.service;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.runcible.abbot.model.PointsForBoat;
import com.runcible.abbot.model.PointsTable;
import com.runcible.abbot.model.Race;
import com.runcible.abbot.model.RaceResult;
import com.runcible.abbot.model.RaceStatus;
import com.runcible.abbot.service.exceptions.NoSuchCompetition;
import com.runcible.abbot.service.exceptions.NoSuchFleet;
import com.runcible.abbot.service.exceptions.NoSuchUser;
import com.runcible.abbot.service.exceptions.UserNotPermitted;

@Component
public class ExportServiceImpl implements ExportService
{

    @Override
    public String exportCompetition(Integer raceSeriesID,Integer competitionID) 
            throws NoSuchCompetition, NoSuchUser, UserNotPermitted, NoSuchFleet
    {
        PointsTable pointsTable = pointsService.generatePointsTable(raceSeriesID, competitionID);
        
        StringBuffer result = new StringBuffer();
        result.append("<table class=\"results-table\">\n");
        result.append(indent(1,"<tr>\n"));
        result.append(indent(2,makeTH("Boat Name")));
        result.append(indent(2,makeTH("Place")));
        for(Race race : pointsTable.getRaces() )
        {
            result.append(indent(2,makeTH(pointsTableHeadingDateFormat.format(race.getRaceDate()))));
        }
        result.append(indent(2,makeTH("Total")));
        result.append(indent(2,makeTH("Total With Drops")));
        result.append(indent(1,"</tr>\n"));
        
        for(PointsForBoat pointsForBoat : pointsTable.getPointsForBoat() )
        {
            result.append(indent(1,"<tr>\n"));
            result.append(indent(2,makeTD(pointsForBoat.getBoat().getName())));
            result.append(indent(2,makeTD(new Integer(pointsForBoat.getPlace()).toString())));
            for( Float points : pointsForBoat.getPoints())
            {
                result.append(indent(2,makeTD(new Float(points).toString())));
            }
            
            ;
            result.append(indent(2,makeTD(String.format("%.1f", pointsForBoat.getTotal()))));
            result.append(indent(2,makeTD(String.format("%.1f", pointsForBoat.getTotalWithDrops()))));
            result.append(indent(1,"\t</tr>\n"));
        }
        result.append("</table>\n");
        
        return result.toString();
    }

    public String exportRaces(Integer raceSeriesID, Integer fleetID) throws NoSuchUser, UserNotPermitted
    {
        List<Race> races = raceService.getRacesForFleet(raceSeriesID, fleetID);
        StringBuffer buffer = new StringBuffer();
        
        for(Race race : races)
        {
            exportRace(race,buffer);
        }
        
        return buffer.toString();
    }
    
    private void exportRace(Race race, StringBuffer buffer) throws NoSuchUser, UserNotPermitted
    {
        List<RaceResult> results = raceResultService.findAll(race.getId());
        
        if ( race.getRaceStatus() == RaceStatus.NOT_RUN )
        {
            return;
        }
        
        buffer.append("<h3>"+race.getName()+" "+raceDateFormat.format(race.getRaceDate())+"</h3>\n");
        if ( race.getRaceStatus() == RaceStatus.ABANDONED )
        {
            buffer.append("<h3>Race abandoned</h3>");
        }
        else
        {
            buffer.append("<table class=\"results-table\">\n");
            buffer.append(indent(1,makeTH("Boat")));
            buffer.append(indent(1,makeTH("Number")));
            buffer.append(indent(1,makeTH("H'Cap")));
            buffer.append(indent(1,makeTH("H'Cap Place")));
            buffer.append(indent(1,makeTH("Start")));
            buffer.append(indent(1,makeTH("Finish")));
            buffer.append(indent(1,makeTH("Time")));
            buffer.append(indent(1,makeTH("Corrected")));

            for(RaceResult result : results)
            {
                buffer.append(indent(1,"<tr>\n"));
                
                boolean finished = result.getStatus().isFinished();
                boolean started = result.getStatus().isStarted();
                
                buffer.append(indent(1,makeTD(result.getBoat().getName())));
                buffer.append(indent(1,makeTD(result.getBoat().getSailNumber())));
                buffer.append(indent(1,makeTD(result.getHandicap().toString())));
                
                if ( finished )
                {
                    buffer.append(indent(1,makeTD(new Integer(result.getHandicapPlace()).toString())));
                }
                else
                {
                    buffer.append(indent(1,makeTD(result.getStatus().name())));
                }
                
                if ( started )
                {
                    buffer.append(indent(1,makeTD(raceTimeFormat.format(result.getStartTime()))));
                }
                else
                {
                    buffer.append(indent(1,makeTD(result.getStatus().name())));
                }

                if ( finished )
                {
                    buffer.append(indent(1,makeTD(raceTimeFormat.format(result.getFinishTime()))));
                    buffer.append(indent(1,makeTD(toTime(result.getSailingTime()))));
                    buffer.append(indent(1,makeTD(toTime(result.getCorrectedTime()))));
                }
                else
                {
                    buffer.append(indent(1,makeTD("")));
                    buffer.append(indent(1,makeTD("")));
                    buffer.append(indent(1,makeTD("")));
                }
                buffer.append(indent(1,"</tr>\n"));
            }
            buffer.append("</table>\n");
        }
    }

    private String toTime(Integer sailingTime)
    {
        int hours = (int) Math.floor(sailingTime/3600);
        int minutes = (int) Math.floor((sailingTime-(hours*3600))/60);
        int seconds = sailingTime % 60;
        
        return String.format("%02d:%02d:%02d", hours,minutes,seconds);
    }

    private String makeTD(String value)
    {
        return makeElement("td",value);
    }

    private String makeTH(String value)
    {
        return makeElement("th",value);
    }

    String makeElement(String element, String value)
    {
        return "<"+element+">"+value+"</"+element+">\n";
    }
    
    private String indent(int places,String value)
    {
        StringBuffer result = new StringBuffer();
        for (int i=0;i<places;i++)
        {
            result.append("\t");
        }
        result.append(value);
        return result.toString();
    }
    
    static  SimpleDateFormat pointsTableHeadingDateFormat = new SimpleDateFormat("dd/MM");
    static  SimpleDateFormat raceDateFormat = new SimpleDateFormat("dd/MM/YYYY");
    static SimpleDateFormat raceTimeFormat = new SimpleDateFormat("hh:mm:ss");
    
    static
    {
        //
        //  Ugly hack...
        //
        TimeZone timeZone = TimeZone.getTimeZone("Australia/Sydney");
        pointsTableHeadingDateFormat.setTimeZone(timeZone);
        raceDateFormat.setTimeZone(timeZone);
        raceTimeFormat.setTimeZone(timeZone);
    }
    
    @Autowired
    private PointsService pointsService;
    
    @Autowired
    private RaceService raceService;

    @Autowired
    private RaceResultService raceResultService;

}

