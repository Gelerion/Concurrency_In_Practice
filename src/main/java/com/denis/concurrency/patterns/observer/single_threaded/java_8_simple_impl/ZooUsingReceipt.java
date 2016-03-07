package com.denis.concurrency.patterns.observer.single_threaded.java_8_simple_impl;

import com.denis.concurrency.patterns.observer.single_threaded.simple_impl.Animal;
import com.denis.concurrency.patterns.observer.single_threaded.simple_impl.AnimalAddedListener;
import com.google.common.collect.Lists;

import java.util.List;

public class ZooUsingReceipt {
    private List<Animal> animals = Lists.newArrayList();
    private List<AnimalAddedListener> listeners = Lists.newArrayList();

    public AnimalAddedListenerReceipt registerAnimalAddedListener(AnimalAddedListener listener) {
        // Add the listener to the list of registered listeners
        this.listeners.add(listener);
        return new AnimalAddedListenerReceipt(listener);
    }

    public void unregisterAnimalAddedListener(AnimalAddedListenerReceipt receipt) {
        // Remove the listener from the list of the registered listeners
        this.listeners.remove(receipt.getListener());
    }
}
