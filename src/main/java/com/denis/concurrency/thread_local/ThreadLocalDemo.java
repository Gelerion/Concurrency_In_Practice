package com.denis.concurrency.thread_local;

public class ThreadLocalDemo
{
	public static void main(String[] args)
	{
		//I cant initialize here
//		Context context = new Context();
//		context.transactionId = "Gelerion";
//		CustomThreadLocal.set(context);

/*		new Thread(new ThreadLocalDemo()::customThreadLocalRun).start();
		new Thread(new ThreadLocalDemo()::customThreadLocalRun).start();
		new Thread(new ThreadLocalDemo()::customThreadLocalRun).start();*/

		new Thread(new ThreadLocalDemo()::initialValueThreadLocalRun).start();
		new Thread(new ThreadLocalDemo()::initialValueThreadLocalRun).start();
		new Thread(new ThreadLocalDemo()::initialValueThreadLocalRun).start();
	}

	public void customThreadLocalRun()
	{
		Context context = new Context();
//		context.transactionId = "Gelerion";
		context.transactionId = Thread.currentThread().getName();

		CustomThreadLocal.set(context);

		new BusinessService().businessMethod();

		CustomThreadLocal.unset();
	}

	public void initialValueThreadLocalRun()
	{
		String context = InititalValueThreadLocal.getContext();
		System.out.println(context);
	}

}
