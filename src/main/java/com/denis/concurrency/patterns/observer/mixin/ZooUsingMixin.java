package com.denis.concurrency.patterns.observer.mixin;

import com.denis.concurrency.patterns.observer.single_threaded.simple_impl.Animal;
import com.google.common.collect.Lists;

import java.util.List;

public class ZooUsingMixin extends ObservableSubjectMixin<ZooListener> {

    private List<Animal> animals = Lists.newArrayList();

    public void addAnimal(Animal animal) {
        this.animals.add(animal);

        this.notifyListeners((listener) -> listener.onAnimalAdded(animal));
    }

    public void removeAnimal(Animal animal) {
        // Remove the animal from the list of animals
        this.animals.remove(animal);
        // Notify the list of registered listeners
        this.notifyListeners((listener) -> listener.onAnimalRemoved(animal));
    }
}
