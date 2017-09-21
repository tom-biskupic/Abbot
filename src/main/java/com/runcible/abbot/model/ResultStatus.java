package com.runcible.abbot.model;

public enum ResultStatus 
{
	FINISHED,
	DNC, 	// 	Did not start; did not come to the starting area
	DNS, 	// 	Did not start (other than DNC and OCS)
	OCS, 	// 	Did not start; on the course side of the starting line at her
			//	starting signal and failed to start, or broke rule 30.1
	ZFP,	// 	20% penalty under rule 30.2
	UFD,	//	Disqualification under rule 30.3
	BFD,	//	Disqualification under rule 30.4
	SCP,	//	Scoring Penalty applied
	DNF, 	//	Did not finish
	RET,	// 	Retired
	DSQ,	// 	Disqualification
	DNE,	// 	Disqualification that is not excludable
	RDG,	// 	Redress given
	DPI 	//	Discretionary penalty imposed
}
