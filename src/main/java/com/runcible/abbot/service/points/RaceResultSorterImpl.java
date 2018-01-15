package com.runcible.abbot.service.points;

import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Component;

import com.runcible.abbot.model.RaceResult;
import com.runcible.abbot.model.ResultType;

@Component
public class RaceResultSorterImpl implements RaceResultSorter
{
    @Override
    public List<RaceResult> sortResults(
            List<RaceResult>    results,
            ResultType          resultType)
    {
        Collections.sort(
                results,
                new RaceResultComparator(resultType));

        return results;
    }

}
