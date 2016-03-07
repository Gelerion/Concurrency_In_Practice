package com.denis.concurrency.patterns.observer.single_threaded.simple_impl;

/**
 * Acts like Observer interface
 */
public interface AnimalAddedListener {

    void onAnimalAdded(Animal animal);
}
