package com.runcible.abbot.service.points;

import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Component;

import com.runcible.abbot.model.Competition;
import com.runcible.abbot.model.RaceResult;

@Component
public class RaceResultSorterImpl implements RaceResultSorter
{
    @Override
    public List<RaceResult> sortResults(
            List<RaceResult>    results,
            Competition         competititon)
    {
        Collections.sort(
                results,
                new RaceResultComparator(competititon.getResultType()));

        return results;
    }

}
