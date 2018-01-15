package com.runcible.abbot.service.points;

import java.util.List;

import com.runcible.abbot.model.RaceResult;
import com.runcible.abbot.model.ResultType;

public interface RaceResultSorter
{
    /**
     * Sorts the race results in points order. Sort order depends on the competition
     * @param results The results to be sorted
     * @param resultType The type of result to sort on (handicap/scratch)
     * @return
     */
    public List<RaceResult> sortResults( 
            List<RaceResult> results, ResultType resultType);
}
