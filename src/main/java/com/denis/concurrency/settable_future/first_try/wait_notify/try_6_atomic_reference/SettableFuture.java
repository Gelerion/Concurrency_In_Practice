package com.denis.concurrency.settable_future.first_try.wait_notify.try_6_atomic_reference;

import com.denis.concurrency.settable_future.CustomFuture;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Вася доволен собой. Ближе к концу рабочего дня Вася обнаруживает, что этот
 * код весьма гадко ведёт себя при передаче null-ов: слот-то вроде выставляется,
 * но читатели сидят себе в цикле по wait() и не думают оттуда выходить
 */
public class SettableFuture<V> implements CustomFuture<V> {
    private final AtomicReference<V> slot = new AtomicReference<>();

    @Override
    public V get() throws InterruptedException, ExecutionException {
        synchronized (this) {
            while (slot.get() == null) {
                wait();
            }
        }
        return slot.get();
    }

    //fails if value is null
    @Override
    public void set(V value) {
        if (slot.compareAndSet(null, value)) {
            synchronized (this) {
                notifyAll();
            }
        }
    }
}
