package com.denis.concurrency.thread_local;

public class CustomThreadLocal
{
	private static final ThreadLocal<Context> THREAD_LOCAL = new ThreadLocal<>();

	static void set(Context user)
	{
		THREAD_LOCAL.set(user);
	}

	static void unset()
	{
		THREAD_LOCAL.remove();
	}

	static Context get()
	{
		return THREAD_LOCAL.get();
	}
}
