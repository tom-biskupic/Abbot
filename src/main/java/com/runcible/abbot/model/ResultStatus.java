package com.runcible.abbot.model;

public enum ResultStatus 
{
	FINISHED,
	DNF, 	//	Did not finish
	OCS, 	// 	Did not start; on the course side of the starting line at her
			//	starting signal and failed to start, or broke rule 30.1
	ZFP,	// 	20% penalty under rule 30.2
	UFD,	//	Disqualification under rule 30.3
	BFD,	//	Disqualification under rule 30.4
	SCP,	//	Scoring Penalty applied
	RET,	// 	Retired
	DSQ,	// 	Disqualification
	DNE,	// 	Disqualification that is not excludable
	RDG,	// 	Redress given
	DPI, 	//	Discretionary penalty imposed
	DNC, 	// 	Did not start; did not come to the starting area
	DNS; 	// 	Did not start (other than DNC and OCS)
	
	public static int compare( ResultStatus left, ResultStatus right)
	{
		int leftOrdinal = left.ordinal(); 
		int rightOrdinal = right.ordinal();
		if ( leftOrdinal == rightOrdinal )
		{
			return 0;
		}
		else if ( leftOrdinal > rightOrdinal )
		{
			return -1;
		}
		else
		{
			return 1;
		}
	}
}
