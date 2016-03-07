package com.denis.concurrency.patterns.observer.blocking_listeners;

import com.denis.concurrency.patterns.observer.single_threaded.simple_impl.Animal;
import com.denis.concurrency.patterns.observer.single_threaded.simple_impl.AnimalAddedListener;

/**
 * 1 - Dispatch a new thread within the listener. Instead of executing the logic of the listener sequentially,
 * have the listener dispatch a new thread, off-loading the execution of the listener logic to the new thread.
 * After the thread has been dispatched, return from the listener method, allowing other listeners to begin execution,
 * while concurrently allowing the dispatched thread to execute the logic of the listener.
 * <p>
 * 2 - Dispatch a new thread within the subject. Instead of iterating through the list of registered listeners
 * sequentially, have the notification method of a subject dispatch a new thread and iterate through the list of
 * registered listeners. This allows the notification method to return immediately, while allowing the concurrent
 * execution of each listener. Note that some thread-safety mechanism is needed to ensure that concurrent modification
 * of the list of registered listeners does not occur.
 * <p>
 * 3 - Queue the listener function invocations and have a set of threads execute the listener functions.
 * Instead of simply iterating through each listener in the list of registered listeners, have the execution
 * of the listener methods encapsulated in some functor and queue these functors. Once these functors have been
 * queued, a thread or set of threads (possibly from a thread pool) can pop each functor from the queue and
 * execute the listener logic. This amounts to a Producer-Consumer problem, where the notification process
 * produces a set of executable functors that are queued, while the threads consume these functors from the queue
 * and execute each. The functor must store the parameters to be provided to the listener method at the time of
 * creation, not at the time of execution (which may be some indeterminate time after creation). For example,
 * the functor is created to store the state of the listener method execution at the time the functor is created,
 * allowing the consumer threads to execute the functor at a later time as if it were being executed at the time
 * of creation. This functionality can be approximated in Java as follows
 */
public class AnimalAddedFunctor {
    private final AnimalAddedListener listener;
    private final Animal parameter;

    public AnimalAddedFunctor(AnimalAddedListener listener, Animal parameter) {
        this.listener = listener;
        this.parameter = parameter;
    }

    public void execute() {
        // Execute the listener with the parameter provided during creation
        this.listener.onAnimalAdded(this.parameter);
    }

    /**
     * Instead of iterating through each listener and executing the listeners immediately, a functor is created
     * and queued for later execution. Once a functor has been queued for each of the listeners that would have
     * been invoked, the notification method returns control to the client code. At some later time, the consumer
     * threads execute these functors, which in turn executes each listener as if it had been executed when the
     * notification method was invoked. This technique is call paramerter binding in other languages and is approximated
     * in the above example (essentially, the single parameter of the above listener is stored, or bound, allowing the
     * execute() method to be called with zero arguments). If the listener accepted more than one parameter, each
     * parameter would be stored in the functor in a manner similar to the single parameter above.
     */
}

