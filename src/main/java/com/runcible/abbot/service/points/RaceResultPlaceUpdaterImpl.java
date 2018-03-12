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
        //
        //  updatedResult will be null if this is called during a delete
        //
        if ( updatedResult != null && alreadyHaveResultForBoat(updatedResult,existingResults) )
        {
            throw new DuplicateResult();
        }

        List<RaceResult> allResults = buildResultList(
                updatedResult,
                existingResults);
        
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
                nextResult.setHandicapPlace(null);
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
                nextResult.setScratchPlace(null);
            }
        }
    }

    private List<RaceResult> buildResultList(
            RaceResult          updatedResult,
            List<RaceResult>    existingResults)
    {
        List<RaceResult> allResults = new ArrayList<RaceResult>();
        
        //
        //  If we have a result and it has an ID then find the result it
        //  is replacing in the list and remove that. Or rather just add
        //  everything else
        //
        if ( updatedResult != null && updatedResult.getId() != null )
        {
            for (RaceResult nextResult : existingResults )
            {
                if ( nextResult.getId() != updatedResult.getId() )
                {
                    allResults.add(nextResult);
                }
            }
        }
        else
        {
            allResults.addAll(existingResults);
        }
        
        if ( updatedResult != null )
        {
            allResults.add(updatedResult);
        }
        
        return allResults;
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
