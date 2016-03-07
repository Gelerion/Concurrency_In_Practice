package com.denis.concurrency.patterns.observer.single_threaded.java_8_simple_impl;

import com.denis.concurrency.patterns.observer.single_threaded.simple_impl.Animal;
import com.denis.concurrency.patterns.observer.single_threaded.simple_impl.AnimalAddedListener;

public class Main_Java_8_2 {
    public static void main (String[] args) {
        // Create the zoo to store animals
        Zoo_Java_8 zoo = new Zoo_Java_8();

        // Register listeners to be notified when an animal is added
        AnimalAddedListener listener = zoo.registerAnimalAddedListener(
                (animal) -> System.out.println("Added a new animal with name '" + animal.getName() + "'")
        );
        // Add an animal notify the registered listeners
        zoo.addAnimal(new Animal("Tiger"));

        // Unregister the listener
        zoo.unregisterAnimalAddedListener(listener);

        // Add another animal, which will not print the name, since the listener
        // has been previously unregistered
        zoo.addAnimal(new Animal("Lion"));
    }
}
