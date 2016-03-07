package com.denis.concurrency.dead_lock;

public class SimpleDeadLock
{
	public static Object cacheLock = new Object();
	public static Object tableLock = new Object();

	public void oneMethod() {
		synchronized (cacheLock) {
			synchronized (tableLock) {
				doSomething();
			}
		}
	}

	public void anotherMethod() {
		synchronized (tableLock) {
			synchronized (cacheLock) {
				doSomethingElse();
			}
		}
	}

	private void doSomethingElse()
	{
	}


	private void doSomething()
	{
	}

}
