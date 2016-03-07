package com.denis.concurrency.wait_notify_race_condition;

import java.util.LinkedList;
import java.util.Queue;

/**
 * This is what could potentially happen:
 * A consumer thread calls take() and sees that the buffer.isEmpty().
 * Before the consumer thread goes on to call wait(), a producer thread comes along and invokes a full give(), that is, buffer.add(data); notify();
 * The consumer thread will now call wait() (and miss the notify() that was just called).
 * If unlucky, the producer thread won't produce more give() as a result of the fact that the consumer thread never wakes up, and we have a dead-lock.
 * Once you understand the issue, the solution is obvious: Always perform give/notify and isEmpty/wait atomically.
 */
public class SomeQueue
{
	Queue<String> buffer = new LinkedList<String>();

	public void give(String data)
	{
		buffer.add(data);
		notify();                   // Since someone may be waiting in take!
	}

	public String take() throws InterruptedException
	{
		while (buffer.isEmpty())    // don't use "if" due to spurious wakeups.
			wait();
		return buffer.remove();
	}
}
