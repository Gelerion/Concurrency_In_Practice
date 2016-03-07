package com.denis.concurrency.interrupt_test;

public class InterruptTest
{
	public static void main(String[] args)
	{

		Thread.currentThread().interrupt();

		printInterrupted(1);

		Object o = new Object();
		try
		{
			synchronized (o)
			{
				printInterrupted(2);
				System.out.printf("A Time %d\n", System.currentTimeMillis());
				o.wait(100);
				System.out.printf("B Time %d\n", System.currentTimeMillis());
			}
		}
		catch (InterruptedException ie)
		{
			System.out.printf("WAS interrupted\n");
		}
		System.out.printf("C Time %d\n", System.currentTimeMillis());

		printInterrupted(3);

		Thread.currentThread().interrupt();

		printInterrupted(4);

		try
		{
			System.out.printf("D Time %d\n", System.currentTimeMillis());
			Thread.sleep(100);
			System.out.printf("E Time %d\n", System.currentTimeMillis());
		}
		catch (InterruptedException ie)
		{
			System.out.printf("WAS interrupted\n");
		}
		System.out.printf("F Time %d\n", System.currentTimeMillis());

		printInterrupted(5);

		try
		{
			System.out.printf("G Time %d\n", System.currentTimeMillis());
			Thread.sleep(100);
			System.out.printf("H Time %d\n", System.currentTimeMillis());
		}
		catch (InterruptedException ie)
		{
			System.out.printf("WAS interrupted\n");
		}
		System.out.printf("I Time %d\n", System.currentTimeMillis());

/*		while (true) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException ie) {
				break;
			}
		}*/

	}

	static void printInterrupted(int n)
	{
		System.out.printf("(%d) Am I interrupted? %s\n", n, Thread.currentThread().isInterrupted() ? "Yes" : "No");
	}
}
