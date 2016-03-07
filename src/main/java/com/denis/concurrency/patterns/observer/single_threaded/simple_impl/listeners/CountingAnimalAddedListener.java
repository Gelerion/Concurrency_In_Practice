package com.denis.concurrency.patterns.observer.single_threaded.simple_impl.listeners;

import com.denis.concurrency.patterns.observer.single_threaded.simple_impl.Animal;
import com.denis.concurrency.patterns.observer.single_threaded.simple_impl.AnimalAddedListener;

public class CountingAnimalAddedListener implements AnimalAddedListener {
    private static int animalsAddedCount = 0;

    @Override
    public void onAnimalAdded(Animal animal) {
        // Increment the number of animals
        animalsAddedCount++;
        // Print the number of animals
        System.out.println("Total animals added: " + animalsAddedCount);
    }
}
