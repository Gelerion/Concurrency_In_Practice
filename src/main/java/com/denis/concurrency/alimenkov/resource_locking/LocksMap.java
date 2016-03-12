package com.denis.concurrency.alimenkov.resource_locking;

import com.google.common.util.concurrent.Striped;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class LocksMap<T> {
    //T - resource
    private final ConcurrentHashMap<T, ResourceLock> locks = new ConcurrentHashMap<>();
    private final Striped<Lock> stripes = Striped.lazyWeakLock(64);

    private ReadWriteLock allocateNamedLock(T resource) {
        Lock stripeLock = stripes.get(resource);
        stripeLock.lock();
        try {
            ResourceLock resourceLock = createResourceLock(resource);
            resourceLock.counter++;
            return resourceLock.rwLock;
        } finally {
            stripeLock.unlock();
        }
    }

    private ResourceLock createResourceLock(T resource) {
        ResourceLock resourceLock = locks.get(resource);
        if (resourceLock == null) {
            ReadWriteLock lock = createReadWriteLock(resource);
            resourceLock = new ResourceLock(lock);
            locks.put(resource, resourceLock);
        }

        return resourceLock;
    }

    private void decreaseAccessCount(T resource, ResourceLock resourceLock) {
        resourceLock.counter--;
        if (resourceLock.counter == 0) {
            locks.remove(resource);
        }
    }

    //protected for tests
    protected ReadWriteLock createReadWriteLock(T resource) {
        return new ReentrantReadWriteLock();
    }

    public static class ResourceLock {
        private int counter;
        private final ReadWriteLock rwLock;

        public ResourceLock(ReadWriteLock rwLock) {
            this.rwLock = rwLock;
        }
    }
}
