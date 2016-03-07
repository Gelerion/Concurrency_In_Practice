package com.denis.concurrency.safe_construction;

/**
 * On first inspection, the EventListener class looks harmless. The registration of the listener,
 * which publishes a reference to the new object where other threads might be able to see it,
 * is the last thing that the constructor does. But even ignoring all the Java Memory Model (JMM)
 * issues such as differences in visibility across threads and memory access reordering, this code still
 * is in danger of exposing an incompletely constructed EventListener object to other threads.
 * Consider what happens when EventListener is subclassed.
 */
public class EventListener
{
	public EventListener(EventSource eventSource)
	{
		// do our initialization

		// register ourselves with the event source
		eventSource.registerListener(this);
	}

	public void onEvent(Event e) {
		System.out.println(getClass().getSimpleName());
		//handle event
	}
}
