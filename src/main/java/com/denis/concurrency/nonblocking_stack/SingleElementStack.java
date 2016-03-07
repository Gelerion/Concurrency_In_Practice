package com.denis.concurrency.nonblocking_stack;

import java.util.concurrent.atomic.AtomicReference;

public class SingleElementStack
{
	private AtomicReference<String> value = new AtomicReference<>();

	public void push(String element)
	{
		boolean isSet = false;

		while (!isSet) {
			String existed = value.get();
			isSet = value.compareAndSet(existed, element);
		}
	}

	public String pop()
	{
		return value.get();
	}
}
