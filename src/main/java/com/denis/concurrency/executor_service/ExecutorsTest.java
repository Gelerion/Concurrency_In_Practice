package com.denis.concurrency.executor_service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExecutorsTest
{
	public static void main(String[] args)
	{
		//Uses ThreadPoolExecutor
		ExecutorService pool = Executors.newFixedThreadPool(2);
	}

}
