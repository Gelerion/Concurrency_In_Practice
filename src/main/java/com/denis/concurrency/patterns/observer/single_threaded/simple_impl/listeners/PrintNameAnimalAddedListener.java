package com.denis.concurrency.patterns.observer.single_threaded.simple_impl.listeners;

import com.denis.concurrency.patterns.observer.single_threaded.simple_impl.Animal;
import com.denis.concurrency.patterns.observer.single_threaded.simple_impl.AnimalAddedListener;

public class PrintNameAnimalAddedListener implements AnimalAddedListener {
    @Override
    public void onAnimalAdded(Animal animal) {
        // Print the name of the newly added animal
        System.out.println("Added a new animal with name '" + animal.getName() + "'");
    }
}