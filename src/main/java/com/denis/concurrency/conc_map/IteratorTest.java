package com.denis.concurrency.conc_map;

import com.denis.concurrency.Threads;

import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class IteratorTest
{
	private static final Semaphore mutex = new Semaphore(0);
	private static final ReentrantLock locker = new ReentrantLock();
	private static final Condition minBufferSize = locker.newCondition();
	private static final ConcurrentHashMap<String, Integer> buffer = new ConcurrentHashMap<>();
	public static final int MIN_BUFF_SIZE = 5;
	private volatile static boolean print = true;

	private static AtomicInteger counter = new AtomicInteger();

	public static void main(String[] args)
	{

		ConcurrentHashMap m = new ConcurrentHashMap();
		m.put(1, 1);
		Iterator i = m.entrySet().iterator();
		m.remove(1);        // remove entry from map
		System.out.println(i.next()); //still shows entry 1=1
		i.remove();
		System.out.println(i.next()); // NoSuchElementException

//		new Thread(new Producer()).start();
//		new Thread(new Consumer()).start();
	}

	private static class Producer implements Runnable {

		@Override
		public void run()
		{
			Random random = new Random();

			while (true)
			{
				int key = random.nextInt(50);
				int value = random.nextInt(20);
//				int value = counter.incrementAndGet();
				if(print) System.out.println("Produce: key[" + key + "] value[" + value + "]");
//				buffer.put(String.valueOf(key), value);
//				if(print) System.out.println("Produce: key[" + value + "] value[" + value + "]");
//				buffer.put(String.valueOf(value), value);


				buffer.merge(String.valueOf(key), value, (existedValue, newValue) -> {
					if(print) System.out.println("Merging key[" + key + "] " + existedValue + " + " + newValue);
					newValue = existedValue + newValue;
					return newValue;
				});
//				System.out.println(buffer);

				if(buffer.size() > 20) mutex.release();
//				if(buffer.size() > MIN_BUFF_SIZE) minBufferSize.signalAll();

				Threads.sleepQuietly(250);
//				System.out.println("Buffer size: " + buffer.size());
			}
		}
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

//				while (buffer.size() < MIN_BUFF_SIZE) {
//					try
//					{ minBufferSize.await(); }
//					catch (InterruptedException e){}
//				}

				print = false;
				System.out.println("Consumer acquired mutex");
				System.out.println("Buffer stamp: size[" + buffer.size() + "] " + "body[" + buffer + "]");
				Iterator<Map.Entry<String, Integer>> iterator = buffer.entrySet().iterator();

				int counter = 0;
				while (iterator.hasNext())
				{
					Map.Entry<String, Integer> entry = iterator.next();

					synchronized (entry.getValue())
					{
						System.out.println("Consumed " + entry.getValue());
						counter++;
						iterator.remove();
						Threads.sleepQuietly(100);
					}
				}

				print = true;
				System.out.println("Iterator end consumed total [" + counter + "]");
				System.out.println("Buffer state: " + buffer);

			}
		}
	}
}
