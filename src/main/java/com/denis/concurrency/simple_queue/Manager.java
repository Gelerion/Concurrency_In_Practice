package com.denis.concurrency.simple_queue;

import com.denis.concurrency.Threads;

import java.util.Random;

public class Manager
{
	public static void main(String[] args)
	{
		SingleElementBuffer buffer = new SingleElementBuffer();

		Thread putter_1 = new Thread(() -> {
			for (int i = 0; i < 25; i++)
			{
				try
				{
					int value = new Random().nextInt(100);
//					System.out.println("Putter_1 BEFORE put: " + value);
					buffer.put(value);
					System.out.println("Putter1 putted: " + value);

					//					System.out.println("Putter_1 AFTER put");
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}
			}
		});
		putter_1.setName("Putter_1");

		Thread putter_2 = new Thread(() -> {
			for (int i = 0; i < 25; i++)
			{
				try
				{
					int value = new Random().nextInt(500);
//					System.out.println("Putter_2 BEFORE put: " + value);
					buffer.put(value);
					System.out.println("Putter2 putted: " + value);
//					System.out.println("Putter_2 AFTER put");
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}
			}
		});
		putter_2.setName("Putter_2");


		Thread taker_2 = new Thread(() -> {
			for (int i = 0; i < 25; i++)
			{
				try
				{
					Threads.sleepQuietly(1000);
					Integer take = buffer.take();
					System.out.println("take = " + take);
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}
			}
		});

		putter_1.start();
		putter_2.start();
		taker_2.start();

		putter_1.setUncaughtExceptionHandler((t, e) -> {
			System.out.println(e + " " + t.getName());
		});
		Threads.sleepQuietly(5000);
		putter_1.stop();
	}
}
