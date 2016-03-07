package com.denis.concurrency.thread_local;

public class InititalValueThreadLocal
{
	private static ThreadLocal<String> local;
	static {
		local = ThreadLocal.withInitial(() -> new Context().transactionId = Thread.currentThread().getName());
	}

	public static String getContext()
	{
		return local.get();
	}
}
