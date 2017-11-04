package com.runcible.abbot.service.points;

import com.runcible.abbot.model.Competition;
import com.runcible.abbot.model.ResultStatus;

public interface PointsCalculator
{
	/**
	 * Caclutes the points for this result in this competition.
	 * Take into account the finishing place and the number of started
	 * 
	 * @param competition The compeitition this reusult contributes towards
	 * @param numberOfStarters The number of starters in this race
	 * @param place The place this result finished in. Not relevant to DNC/DNS/DNF results
	 * @param status The status of the result (i.e. Finished, DNF etc).
	 * @return The points for this result
	 */
	public Float calculatePoints(Competition competition, int numberOfStarters, int place, ResultStatus status);
}
