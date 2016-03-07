package com.denis.concurrency.patterns.observer.single_threaded.java_8_simple_impl;

import com.denis.concurrency.patterns.observer.single_threaded.simple_impl.Animal;
import com.denis.concurrency.patterns.observer.single_threaded.simple_impl.AnimalAddedListener;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * act as the subject, responsible for storing a list of the animals
 * in the zoo and notifying a collection of registered listeners each time a new animal is added to the zoo
 */
public class Zoo_Java_8 {
    private List<Animal> animals = Lists.newArrayList();
    private List<AnimalAddedListener> listeners = Lists.newArrayList();

    public void addAnimal(Animal animal) {
        // Add the animal to the list of animals
        animals.add(animal);

        // Notify the list of registered listeners
        notifyAnimalListeners(animal);
    }

    public AnimalAddedListener registerAnimalAddedListener(AnimalAddedListener listener) {
        // Add the listener to the list of registered listeners
        listeners.add(listener);

        return listener;
    }

    public void unregisterAnimalAddedListener(AnimalAddedListener listener) {
        // Remove the listener from the list of the registered listeners
        listeners.remove(listener);
    }

    protected void notifyAnimalListeners(Animal animal) {
        // Notify each of the listeners in the list of registered listeners
        listeners.forEach(listener -> listener.onAnimalAdded(animal));
    }
}
