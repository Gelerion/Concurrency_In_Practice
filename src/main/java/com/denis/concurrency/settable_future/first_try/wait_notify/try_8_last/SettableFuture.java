package com.denis.concurrency.settable_future.first_try.wait_notify.try_8_last;

import com.denis.concurrency.settable_future.CustomFuture;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicReference;

@SuppressWarnings("all")
public class SettableFuture<V> implements CustomFuture<V> {
    private static final Object NOT_SET = new Object();
    private final AtomicReference<V> slot = new AtomicReference<>((V) NOT_SET);

    @Override
    public V get() throws InterruptedException, ExecutionException {
        if (slot.get() == NOT_SET) {
            synchronized (this) {
                while (slot.get() == NOT_SET) {
                    wait();
                }
            }
        }

        return slot.get();
    }

    @Override
    public void set(V value) {
        if (slot.get() == NOT_SET) {
            if (slot.compareAndSet((V) NOT_SET, value)) {
                synchronized (this) {
                    notifyAll();
                }
            }
        }
    }
}