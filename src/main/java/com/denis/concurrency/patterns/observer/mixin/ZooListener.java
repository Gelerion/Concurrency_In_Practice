package com.denis.concurrency.patterns.observer.mixin;

import com.denis.concurrency.patterns.observer.single_threaded.simple_impl.Animal;

//Complex listener
public interface ZooListener {

    //Default acts as Adapter pattern
    default void onAnimalAdded(Animal animal) {}

    default void onAnimalRemoved(Animal animal) {}
}
