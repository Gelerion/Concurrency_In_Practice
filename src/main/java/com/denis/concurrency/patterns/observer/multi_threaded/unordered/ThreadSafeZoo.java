package com.denis.concurrency.patterns.observer.multi_threaded.unordered;

import com.denis.concurrency.patterns.observer.single_threaded.simple_impl.Animal;
import com.denis.concurrency.patterns.observer.single_threaded.simple_impl.AnimalAddedListener;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 1 - The concurrent access of each listener. Since multiple threads may notify a the list of
 * registered listeners of a new animal, each listener may be concurrently called by multiple threads.
 *
 * 2 - The concurrent access of the list of animals. Multiple threads may add to the list of animals,
 * requiring some concurrency mechanism to guard against a race-condition. This also leads to an issue
 * with the ordering of notifications. For example, a race-condition can occur where the list of registered
 * listeners is notified of the addition of animal 1 after being notified of the addition of animal 2,
 * even if animal 1 is added before animal 2 if the addition of animal 1 is performed in a separate
 * thread from the addition of animal 2 (i.e., thread 1 adds animal 1 and blocks before notifying the listeners;
 * thread 2 adds animal 2 and notifies the listeners, completing execution; thread 1 resumes and notifies
 * the listeners that animal 1 was added). While this is not always an issue (if the ordering of notifications
 * does not matter), in the general case, this presents an issue (in the case where ordering does not matter,
 * we simple ignore the race-condition, but it nonetheless exists).
 */
public class ThreadSafeZoo {

    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    protected final Lock writeLock = lock.writeLock();
    protected final Lock readLock = lock.readLock();

    private List<Animal> animals = Lists.newArrayList();
    private List<AnimalAddedListener> listeners = Lists.newArrayList();

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
