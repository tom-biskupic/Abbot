package com.runcible.abbot.service;

import static org.junit.Assert.*;

import org.junit.Test;

import com.runcible.abbot.model.RaceResult;
import com.runcible.abbot.model.ResultStatus;
import com.runcible.abbot.model.ResultType;

public class RaceResultComparatorTest
{

	@Test
	public void testHandicapComparison()
	{
		RaceResultComparator fixture = new RaceResultComparator(ResultType.HANDICAP_RESULT);
		assertEquals(1,fixture.compare(testFinishedResult2, testFinishedResult1));
		assertEquals(-1,fixture.compare(testFinishedResult1, testFinishedResult2));
		assertEquals(0,fixture.compare(testFinishedResult1, testFinishedResult1));
	}

	@Test
	public void testScratchComparison()
	{
		RaceResultComparator fixture = new RaceResultComparator(ResultType.SCRATCH_RESULT);
		assertEquals(-1,fixture.compare(testFinishedResult2, testFinishedResult1));
		assertEquals(1,fixture.compare(testFinishedResult1, testFinishedResult2));
		assertEquals(0,fixture.compare(testFinishedResult1, testFinishedResult1));
	}

	@Test
	public void testDifferentStatus()
	{
		RaceResultComparator fixture = new RaceResultComparator(ResultType.HANDICAP_RESULT);
		assertEquals(-1,fixture.compare(testDNFResult, testFinishedResult1));
		assertEquals(1,fixture.compare(testFinishedResult1,testDNFResult));
		assertEquals(1,fixture.compare(testDNFResult,testDNSResult));
		assertEquals(-1,fixture.compare(testDNSResult,testDNFResult));
		assertEquals(0,fixture.compare(testDNFResult, testDNFResult));
	}

	private RaceResult testFinishedResult1 = new RaceResult(null,null,null,null,null,ResultStatus.FINISHED,100,100);
	private RaceResult testFinishedResult2 = new RaceResult(null,null,null,null,null,ResultStatus.FINISHED,110,90);
	private RaceResult testDNFResult = new RaceResult(null,null,null,null,null,ResultStatus.DNF);
	private RaceResult testDNSResult = new RaceResult(null,null,null,null,null,ResultStatus.DNS);
}
