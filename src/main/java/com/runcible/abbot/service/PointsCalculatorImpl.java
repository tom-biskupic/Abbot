package com.runcible.abbot.service;

import org.springframework.stereotype.Component;

import com.runcible.abbot.model.Competition;
import com.runcible.abbot.model.PointsSystem;
import com.runcible.abbot.model.RaceResult;

@Component
public class PointsCalculatorImpl implements PointsCalculator
{

	@Override
	public Float calculatePoints(Competition competition, int numberOfStarters, int place, RaceResult result)
	{
		Float points;
		if ( ! result.isStarted())
		{
		    points = calcPoints(competition.getPointsSystem(),competition.getFleetSize()+1);
		}
		else if ( !result.isFinished() )
		{
			points = calcPoints(competition.getPointsSystem(),numberOfStarters+1);
		}
		else
		{
			points = calcPoints(competition.getPointsSystem(),place);
		}
		return points;
	}

    private Float calcPoints(PointsSystem pointsSystem, Integer place)
    {
        if ( pointsSystem == PointsSystem.LOW_POINTS )
        {
            return new Float(place);
        }
        else
        {
            if ( place <= 7 )
            {
                //
                //  Table is zero index based but place is one based
                //
                return BonusPointsTable[place-1];
            }
            else
            {
                //
                //  So seventh place is 13, 8th is 14 an so on
                //
                return new Float(place+6); 
            }
        }
    }

    private static final Float BonusPointsTable[] = {0.0f,3.0f,5.7f,8.0f,10.0f,11.7f,13.0f};
   
}
