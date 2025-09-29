package com.runcible.abbot.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class ResultStatusTest
{
	@Test
	public void testCompare()
	{
		assertEquals(-1,ResultStatus.compare(ResultStatus.FINISHED,ResultStatus.DNF));
		assertEquals(0,ResultStatus.compare(ResultStatus.FINISHED,ResultStatus.FINISHED));
		assertEquals(1,ResultStatus.compare(ResultStatus.DNS,ResultStatus.FINISHED));
	}
}
