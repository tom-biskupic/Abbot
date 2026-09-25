package com.runcible.abbot.service.exceptions;

public class FleetInUse extends Exception
{
	public FleetInUse(String s)
	{
		super(s);
	}

	private static final long serialVersionUID = 1L;
}
