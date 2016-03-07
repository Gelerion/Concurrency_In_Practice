package com.denis.concurrency.thread_stop;

import java.util.IllegalFormatException;

public class PingPong
{
	private boolean wakeUp = true;

	private final Object mutex = new Object();

	public void ping()
	{
		synchronized (mutex)
		{
			while(!wakeUp) {
				try {
					System.out.println(Thread.currentThread() + " i am waiting for pong");
					mutex.wait(); }
				catch (InterruptedException e) {
					System.out.println("Job was interrupted");
					Thread.currentThread().interrupt(); }}

			System.out.println("Ping from " + Thread.currentThread());
			wakeUp = false;
			mutex.notifyAll();
			throw new ArrayIndexOutOfBoundsException();
		}
	}

	public void pong()
	{
		synchronized (mutex)
		{
			while(wakeUp) {
				try {
					System.out.println(Thread.currentThread() + " i am waiting for ping");
					mutex.wait(); }
				catch (InterruptedException e) {
					System.out.println("Job was interrupted");
					Thread.currentThread().interrupt(); }}

			System.out.println("Pong from " + Thread.currentThread());
			wakeUp = true;
			mutex.notifyAll();
		}
	}
}
