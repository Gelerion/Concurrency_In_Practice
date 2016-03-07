package com.denis.concurrency.nonblocking_stack;

import com.denis.concurrency.Threads;

public class Test
{
	public static void main(String[] args)
	{
//		SingleElementStack stack = new SingleElementStack();
		Stack<String> stack = new Stack<>();

		new Thread(() -> {
			for (int i = 0; i < 500; i++)
			{
				stack.push(String.valueOf(i));
			}
		}).start();

		new Thread(() -> {
			for (int i = 0; i < 500; i++)
			{
				Threads.sleepQuietly(1);
				System.out.println(stack.pop());
			}
		}).start();
	}
}
