package com.denis.concurrency.patterns.observer.single_threaded.java_8_simple_impl;

import com.denis.concurrency.patterns.observer.single_threaded.simple_impl.Animal;
import com.denis.concurrency.patterns.observer.single_threaded.simple_impl.Zoo;

public class Main_Java_8_1 {
    /**
     * The ability to register implicitly created listeners introduces a problem:
     * Since the object is created within the scope of the registration call, storing a reference
     * to the concrete listener may not be possible. This means that a listener registered using a lambda
     * function or anonymous inner class may not be unregistered (since the unregister method requires
     * a reference to a listener that has been previously registered). A simple solution to this problem is
     * to return a reference to the registered listener in the registerAnimalAddedListener method.
     * Using this technique, client code may store a refernce to the listener created in place which
     * can be used to unregister the listener at a later time.
     */
    public static void main (String[] args) {
        // Create the zoo to store animals
        Zoo zoo = new Zoo();
        // Register listeners to be notified when an animal is added
        zoo.registerAnimalAddedListener(
                (animal) -> System.out.println("Added a new animal with name '" + animal.getName() + "'") //-> could not be unregistered
        );
        // Add an animal notify the registered listeners
        zoo.addAnimal(new Animal("Tiger"));
    }
}
