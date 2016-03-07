package com.denis.concurrency.patterns.observer.single_threaded.simple_impl;

import com.denis.concurrency.patterns.observer.single_threaded.simple_impl.listeners.CountingAnimalAddedListener;
import com.denis.concurrency.patterns.observer.single_threaded.simple_impl.listeners.PrintNameAnimalAddedListener;

public class Main {
    public static void main (String[] args) {
        Zoo zoo = new Zoo();
        // Register listeners to be notified when an animal is added
        zoo.registerAnimalAddedListener(new PrintNameAnimalAddedListener());
        zoo.registerAnimalAddedListener(new CountingAnimalAddedListener());
        // Add an animal notify the registered listeners
        zoo.addAnimal(new Animal("Tiger"));
        zoo.addAnimal(new Animal("Lion"));
        zoo.addAnimal(new Animal("Bear"));
    }
}
