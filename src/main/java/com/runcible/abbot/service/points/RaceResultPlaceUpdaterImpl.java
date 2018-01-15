package com.runcible.abbot.service.points;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.runcible.abbot.model.RaceResult;
import com.runcible.abbot.model.ResultType;
import com.runcible.abbot.service.exceptions.DuplicateResult;

@Component
public class RaceResultPlaceUpdaterImpl implements RaceResultPlaceUpdater
{

    @Override
    public void updateResultPlaces(
            RaceResult          updatedResult,
            List<RaceResult>    existingResults) throws DuplicateResult
    {
        if ( updatedResult != null && alreadyHaveResultForBoat(updatedResult,existingResults) )
        {
            throw new DuplicateResult();
        }
        
        List<RaceResult> allResults = new ArrayList<RaceResult>();
        allResults.addAll(existingResults);
        
        if ( updatedResult != null )
        {
            allResults.add(updatedResult);
        }
        
        raceResultSorter.sortResults(allResults, ResultType.HANDICAP_RESULT);
        
        int nextPlace=1;
        for(RaceResult nextResult : allResults)
        {
            if (nextResult.getStatus().isFinished() )
            {
                nextResult.setHandicapPlace(nextPlace++);
            }
            else
            {
                nextResult.setHandicapPlace(0);
            }
        }
        
        raceResultSorter.sortResults(allResults, ResultType.SCRATCH_RESULT);
        
        nextPlace=1;
        for(RaceResult nextResult : allResults)
        {
            if (nextResult.getStatus().isFinished() )
            {
                nextResult.setScratchPlace(nextPlace++);
            }
            else
            {
                nextResult.setScratchPlace(0);
            }
        }
    }

    private boolean alreadyHaveResultForBoat(
            RaceResult          result,
            List<RaceResult>    allResults)
    {
        for(RaceResult nextResult : allResults )
        {
            if (    nextResult.getBoat().getId() == result.getBoat().getId()
                    &&
                    nextResult.getId() != result.getId() )
            {
                return true;
            }
        }
        
        return false;
    }

    private @Autowired RaceResultSorter raceResultSorter; 
}
