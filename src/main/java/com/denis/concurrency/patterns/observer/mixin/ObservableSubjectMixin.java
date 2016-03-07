package com.denis.concurrency.patterns.observer.mixin;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.function.Consumer;

public abstract class ObservableSubjectMixin<T> {

    private List<T> listeners = Lists.newArrayList();

    public T registerListener(T listener) {
        // Add the listener to the list of registered listeners
        this.listeners.add(listener);
        return listener;
    }

    public void unregisterAnimalAddedListener(T listener) {
        // Remove the listener from the list of the registered listeners
        this.listeners.remove(listener);
    }

    public void notifyListeners(Consumer<? super T> algorithm) {
        // Execute some function on each of the listeners
        this.listeners.forEach(algorithm);
    }
}
