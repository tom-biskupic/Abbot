package com.runcible.abbot.service;

import java.util.Comparator;

import com.runcible.abbot.model.RaceResult;
import com.runcible.abbot.model.ResultStatus;
import com.runcible.abbot.model.ResultType;

public class RaceResultComparator implements Comparator<RaceResult>
{
	public RaceResultComparator(ResultType resultType)
	{
		this.resultType = resultType;
	}
	
	@Override
	public int compare(RaceResult left, RaceResult right)
	{
		if ( 	left.getStatus() == ResultStatus.FINISHED
				&&
				right.getStatus() == ResultStatus.FINISHED )
		{
			if ( resultType.equals(ResultType.HANDICAP_RESULT) )
			{
	            return differenceToInt(right.getCorrectedTime() - left.getCorrectedTime());
			}
			else
			{
	            return differenceToInt(right.getSailingTime() - left.getSailingTime());
			}
		}
		else
		{
			return ResultStatus.compare(left.getStatus(), right.getStatus());
		}
	}

    protected int differenceToInt(int difference)
    {
        if ( difference > 0 )
        {
            return -1;
        }
        else if ( difference < 0 )
        {
            return 1;
        }
        return 0;
    }

	private ResultType resultType;
}
