package com.runcible.abbot.service.points;

import java.util.List;

import com.runcible.abbot.model.RaceResult;
import com.runcible.abbot.service.exceptions.DuplicateResult;

public interface RaceResultPlaceUpdater
{

    public void updateResultPlaces(RaceResult updatedResult, List<RaceResult> existingResults) throws DuplicateResult;
}
