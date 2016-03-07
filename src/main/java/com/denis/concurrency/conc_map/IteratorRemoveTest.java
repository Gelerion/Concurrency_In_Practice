package com.denis.concurrency.conc_map;

import com.denis.concurrency.Threads;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;

public class IteratorRemoveTest
{
	private static final Semaphore mutex = new Semaphore(0);
	private static final ConcurrentHashMap<String, CustomObject> buffer = new ConcurrentHashMap<>();

	public static void main(String[] args)
	{
		ConcurrentHashMap<String, String> map = new ConcurrentHashMap<>();

		buffer.put("1", new CustomObject().setValue(1));
		buffer.put("2", new CustomObject().setValue(2));
		buffer.put("3", new CustomObject().setValue(3));
		buffer.put("4", new CustomObject().setValue(4));
		buffer.put("5", new CustomObject().setValue(5));

		new Thread(new Consumer()).start();
		mutex.release();

		Threads.sleepQuietly(100);

		for (Map.Entry<String, CustomObject> entry : buffer.entrySet())
		{
			String key = entry.getKey();
			CustomObject value = entry.getValue();
			if(key.equals("2")) {
				System.out.println("Before sync value = " + value);
				Threads.sleepQuietly(3000);
				synchronized (value)
				{
					System.out.println("value = " + value);
//					Threads.sleepQuietly(5000);
					buffer.put("2", value.setValue(987));
				}
			}
		}

//		buffer.forEach((key, value) -> {
//			if(key.equals("2")) {
//				synchronized (value)
//				{
//					Threads.sleepQuietly(1800);
//					buffer.put("2", 987);
//				}
//				System.out.println(buffer);
//			}
//		});

	}

	private static class Consumer implements Runnable {

		@Override
		public void run()
		{
			while (true)
			{
				try
				{ mutex.acquire(); }
				catch (InterruptedException e){}

				System.out.println("Consumer acquired mutex");
				System.out.println("Buffer stamp: size[" + buffer.size() + "] " + "body[" + buffer + "]");
				Iterator<Map.Entry<String, CustomObject>> iterator = buffer.entrySet().iterator();

				int counter = 0;
				while (iterator.hasNext())
				{
					Map.Entry<String, CustomObject> entry = iterator.next();

					synchronized(entry.getValue())
					{
						System.out.println("Consumed " + entry.getValue());
						counter++;
						iterator.remove();
					}
					Threads.sleepQuietly(1000);
				}

				System.out.println("Iterator end consumed total [" + counter + "]");
				System.out.println("Buffer state: " + buffer);

			}
		}
	}
}
