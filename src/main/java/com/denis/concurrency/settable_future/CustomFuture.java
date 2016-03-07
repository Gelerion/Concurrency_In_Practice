package com.denis.concurrency.settable_future;

import java.util.concurrent.ExecutionException;

public interface CustomFuture<V>
{
	V get() throws InterruptedException, ExecutionException;

	void set(V value);
}
