package com.denis.concurrency.thread_local;

public class BusinessService
{
	public void businessMethod() {
		// get the context from thread local
		Context context = CustomThreadLocal.get();
		System.out.println(context.transactionId);
	}
}
