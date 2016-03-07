package com.denis.concurrency.shutdown_hook;

/**
 * ---------- Number of Shutdown hooks:
 * There is no limitations on number of shutdown hooks, one can attach as many shutdown hooks as he wants.
 * ---------- When to attach Shutdown hook:
 * Anytime!!! One can attach a shutdown hook at any instance of time, but before JVM starts shutting down.
 * If one tries to register a shutdown hook after JVM starts shutting down, then it will throw an IllegalStateException with message, "Shutdown in progress"
 * ---------- Attaching same hook again:
 * One can't attach same hook again and if it happens, it will throw  IllegalArgumentException with message "Hook previously registered"
 * ---------- De-Register a Hook:
 * One can de-register a hook as well by simply calling method Runtime.removeShutdownHook(Thread hook)
 * PS: Most of the time, shutdown hooks are registered using anonymous inner classes, but since we don't have any reference available for them,
 * we should not use anonymous inner classes for hooks that we may de-register, because we need to pass there reference in removeShutdownHook(Thread hook) method.
 * ---------- Keep an eye on Concurrency:
 * In case one have attached more than one shutdown hook, then they will run in parallel and hence pron to all issues related to threads, e.g.
 * deadlocks or race conditions. Java Doc for the method also state that:
 * ---------- Reliability of Shutdown Hook:
 * JVM tries his best to execute shutdown hooks at the time of going down, but it can't be guranteed, e.g. when JVM is killed using -kill command on Linux or
 * Terminate Process on windows, then JVM exits instantly or it crashes because of some native code invocation.
 * ---------- Keep an eye on Time Consumption by hooks:
 * One of the important thing to note is that shutdown hooks should not be time consuming. Consider the scenario when user logs off from OS, then
 * OS assign very limited time to gracefully shutdown, hence in such scenarios JVM can forcefully exit.
 */
public class AddShutDownHookSample
{
	public void attachShutDownHook() {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				System.out.println("Inside shutdown hook");
			}
		});

		System.out.println("Shut Down Hook Attached");
	}

	public static void main(String[] args)
	{
		AddShutDownHookSample sample = new AddShutDownHookSample();
		sample.attachShutDownHook();

		System.out.println("Last instruction of Program....");
		System.exit(0);
	}
}
