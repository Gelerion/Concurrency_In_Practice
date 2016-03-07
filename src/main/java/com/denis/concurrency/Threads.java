package com.denis.concurrency;

public class Threads
{
	public static void sleepQuietly(long millis) {
		try
		{
			Thread.sleep(millis);
		}
		catch (InterruptedException ignore) { /*NOP*/}
	}
}