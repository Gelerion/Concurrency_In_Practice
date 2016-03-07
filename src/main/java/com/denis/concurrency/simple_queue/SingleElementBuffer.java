package com.denis.concurrency.simple_queue;

public class SingleElementBuffer
{
	private Integer elem;

	public synchronized void put(Integer value) throws InterruptedException
	{
		System.out.println(Thread.currentThread() + " ENTER");
		while (this.elem != null)
		{
			System.out.println(Thread.currentThread() + " is waiting");
			this.wait();
			System.out.println(Thread.currentThread() + " wakeUp");
		}

		this.elem = value;
		this.notifyAll();
	}

	public synchronized Integer take() throws InterruptedException
	{
		while (this.elem == null)
		{
			this.wait();
		}

		Integer result = this.elem;
		this.elem = null;
		this.notifyAll();
		return result;
	}
}
