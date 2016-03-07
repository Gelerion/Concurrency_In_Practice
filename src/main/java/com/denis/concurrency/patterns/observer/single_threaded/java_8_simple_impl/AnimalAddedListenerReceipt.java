package com.denis.concurrency.patterns.observer.single_threaded.java_8_simple_impl;

import com.denis.concurrency.patterns.observer.single_threaded.simple_impl.AnimalAddedListener;

/**
 * While the receipt implementation provided above is trivial, this mechanism allows
 * other information to be stored that can later be used for unregistering a listener
 * (i.e., if the unregistration algorithm depends on the state of the subject at the time the listener was registered).
 * If the unregistration algoritm simple requires a reference to the previously registered listener,
 * the receipt technique may be burdensome and should be avoided.
 */
public class AnimalAddedListenerReceipt {
    private final AnimalAddedListener listener;

    public AnimalAddedListenerReceipt(AnimalAddedListener listener) {
        this.listener = listener;
    }

    public final AnimalAddedListener getListener() {
        return this.listener;
    }
}
