package com.denis.concurrency.patterns.observer.multi_threaded.unordered;

import com.denis.concurrency.patterns.observer.single_threaded.simple_impl.Animal;
import com.denis.concurrency.patterns.observer.single_threaded.simple_impl.AnimalAddedListener;

import java.util.concurrent.atomic.AtomicLong;

public class ThreadSafeCountingAnimalAddedListener implements AnimalAddedListener {
    private static AtomicLong animalsAddedCount = new AtomicLong(0);

    @Override
    public void onAnimalAdded(Animal animal) {
        // Increment the number of animals
        animalsAddedCount.incrementAndGet();
        // Print the number of animals
        System.out.println("Total animals added: " + animalsAddedCount);
    }
}
