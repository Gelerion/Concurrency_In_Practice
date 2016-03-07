package com.denis.concurrency.patterns.observer.multi_threaded.ordered;

import com.denis.concurrency.patterns.observer.single_threaded.simple_impl.Animal;
import com.denis.concurrency.patterns.observer.single_threaded.simple_impl.AnimalAddedListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * With fair-ordering enabled, all threads accessing the register, unregister, and notify methods will
 * approximate a FIFO ordering of read and write lock acquisition. For example, one thread (thread 1)
 * registers a listener and another thread (thread 2), after the execution of the registration starts,
 * attempts to notify the registered listeners. Additionally, thread 3 attempts to notify the registered
 * listeners after thread 2 is waiting on the read lock. Using the fair-ordering approach, thread 1 will
 * first register the listener; thread 2 will then notify the listeners; lastly, thread 3 will notify the
 * listeners. This ensures that the order in which the actions were initiated is the order in which the
 * actions were taken.
 */
public class OrderedThreadSafeZoo {

    /*
            Fairness is set to true
     */
    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock(true);
    protected final Lock readLock = readWriteLock.readLock();
    protected final Lock writeLock = readWriteLock.writeLock();

    private List<Animal> animals = new ArrayList<>();
    private List<AnimalAddedListener> listeners = new ArrayList<>();

    public void addAnimal(Animal animal) {
        // Add the animal to the list of animals
        this.animals.add(animal);
        // Notify the list of registered listeners
        this.notifyAnimalAddedListeners(animal);
    }

    public AnimalAddedListener registerAnimalAddedListener(AnimalAddedListener listener) {
        // Lock the list of listeners for writing
        this.writeLock.lock();
        try {
            // Add the listener to the list of registered listeners
            this.listeners.add(listener);
        } finally {
            // Unlock the writer lock
            this.writeLock.unlock();
        }
        return listener;
    }

    public void unregisterAnimalAddedListener(AnimalAddedListener listener) {
        // Lock the list of listeners for writing
        this.writeLock.lock();
        try {
            // Remove the listener from the list of the registered listeners
            this.listeners.remove(listener);
        } finally {
            // Unlock the writer lock
            this.writeLock.unlock();
        }
    }

    public void notifyAnimalAddedListeners(Animal animal) {
        // Lock the list of listeners for reading
        this.readLock.lock();
        try {
            // Notify each of the listeners in the list of registered listeners
            this.listeners.forEach(listener -> listener.onAnimalAdded(animal));
        } finally {
            // Unlock the reader lock
            this.readLock.unlock();
        }
    }
}
