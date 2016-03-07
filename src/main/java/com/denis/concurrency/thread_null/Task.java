package com.denis.concurrency.thread_null;

/**
 * Maybe because you think that assigning 'null' to an object will simply make that object
 * eligible for garbage collection (as in this case thread1 will have no other active references)
 * and hence the thread should stop execution. Well... threads don't get terminated that way.
 */
public class Task implements Runnable
{
	public static void main(String[] args)
	{
		Thread thread = new Thread(new Task());
		thread.start();
		thread = null;

		//thread continue to work like nothing has happens
		System.out.println("NULL");

	}

	@Override
	public void run()
	{
		int counter = 0;
		while (counter < 500)
		{
			System.out.println(Thread.currentThread());
			try
			{
				Thread.sleep(1000);
			}
			catch (InterruptedException e)
			{
			}
			counter++;
		}
	}
}
