package com.denis.concurrency.alimenkov.resource_locking;

import com.google.common.util.concurrent.Striped;

import java.util.concurrent.locks.Lock;

public class ReduceSynchronizationPenalty<T> {
    private final Striped<Lock> stripes = Striped.lazyWeakLock(64);

    //like concurrent hash map
//    private ReadWriteLock allocateNamedLock(T resource) {
//        Lock stripeLock = stripes.get(resource);
//        stripeLock.lock();
//        try {
//            ResourceLock resourceLock = createResourceLock(resource);
//            resourceLock.counter++;
//            return resourceLock.rwLock;
//        } finally {
//            stripeLock.unlock();
//        }
//    }

}
