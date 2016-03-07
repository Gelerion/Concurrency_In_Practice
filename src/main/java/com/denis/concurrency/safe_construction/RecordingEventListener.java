package com.denis.concurrency.safe_construction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Because the Java language specification requires that a call to super() be the first statement
 * in a subclass constructor, our not-yet-constructed event listener is already registered with
 * the event source before we can finish the initialization of the subclass fields. Now we have a data race
 * for the list field. If the event listener decides to send an event from within the registration call,
 * or we just get unlucky and an event arrives at exactly the wrong moment, RecordingEventListener.onEvent()
 * could get called while list still has the default value of null, and would then throw a NullPointerException exception.
 * Class methods like onEvent() shouldn't have to code against final fields not being initialized.
 */
public class RecordingEventListener extends EventListener
{
	private final List list;

	public RecordingEventListener(EventSource eventSource) {
		super(eventSource);
		list = Collections.synchronizedList(new ArrayList());
	}

	public void onEvent(Event e) {
		System.out.println(getClass().getSimpleName());
		list.add(e);
		super.onEvent(e);
	}

	public Event[] getEvents() {
		return (Event[]) list.toArray(new Event[0]);
	}
}
