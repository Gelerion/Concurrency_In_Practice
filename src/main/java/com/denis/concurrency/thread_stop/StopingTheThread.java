package com.denis.concurrency.thread_stop;

import com.denis.concurrency.Threads;

public class StopingTheThread
{

	public static void main(String[] args)
	{
		PingPong job = new PingPong();

		Thread thread = new Thread(new Worker(job)::run);
		thread.setName("Worker-1");
		Thread thread2 = new Thread(new Worker2(job)::run);
		thread2.setName("Worker-2");

		System.out.println("I am " + Thread.currentThread());

		thread.setUncaughtExceptionHandler((t, error) -> {
			System.out.println("Thread: " + t + " just throw an error: " + error.getMessage());
		});

		thread.start();
		thread2.start();

		Threads.sleepQuietly(2700);
		//		thread2.stop();
		//		Thread.currentThread().stop();
	}

	private static class Worker implements Runnable
	{
		PingPong job;

		public Worker(PingPong job)
		{
			this.job = job;
		}

		@Override
		/**
		 * I could catch ThreadDeath with Throwable, then setUncaughtExceptionHandler will not work
		 */ public void run()
		{
			System.out.println("I " + Thread.currentThread() + " have started");
			while (true)
			{
				Threads.sleepQuietly(500);
				job.ping();
			}
		}
	}

	private static class Worker2 implements Runnable
	{
		PingPong job;

		public Worker2(PingPong job)
		{
			this.job = job;
		}

		@Override
		/**
		 * I could catch ThreadDeath with Throwable, then setUncaughtExceptionHandler will not work
		 */ public void run()
		{
			System.out.println("I " + Thread.currentThread() + " have started");
			while (true)
			{
				job.pong();
				Threads.sleepQuietly(2000);
			}
		}
	}
}
