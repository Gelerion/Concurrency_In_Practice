package com.denis.concurrency.safe_construction.inner_class;

import com.denis.concurrency.safe_construction.Event;
import com.denis.concurrency.safe_construction.EventListener;
import com.denis.concurrency.safe_construction.EventSource;

/**
 * It is possible to create the escaped reference problem without using the this reference at all.
 * Non-static inner classes maintain an implicit copy of the this reference of their parent object,
 * so creating an anonymous inner class instance and passing it to an object visible from outside
 * the current thread has all the same risks as exposing the this reference itself.
 */
public class EventListener2 {

	public EventListener2(EventSource eventSource) {

		eventSource.registerListener(
				new EventListener(eventSource) {
					public void onEvent(Event e) {
						eventReceived(e);
					}
				});
	}

	public void eventReceived(Event e) {
	}
}
