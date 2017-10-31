package com.runcible.abbot.service.points;

import java.util.List;

import com.runcible.abbot.model.Competition;
import com.runcible.abbot.model.RaceResult;

public interface RaceResultSorter
{
    /**
     * Sorts the race results in points order. Sort order depends on the competition
     * @param results The results to be sorted
     * @param competititon The competition these results are part of.
     * @return
     */
    public List<RaceResult> sortResults( List<RaceResult> results, Competition competititon);
}
