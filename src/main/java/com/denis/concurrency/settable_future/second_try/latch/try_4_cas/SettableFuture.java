package com.denis.concurrency.settable_future.second_try.latch.try_4_cas;

import com.denis.concurrency.settable_future.CustomFuture;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicBoolean;

public class SettableFuture<V> implements CustomFuture<V> {
    private final CountDownLatch latch = new CountDownLatch(1);
    private final AtomicBoolean isSet = new AtomicBoolean();
    private V slot;

    @Override
    public V get() throws InterruptedException, ExecutionException {
        latch.await();
        return slot;
    }

    @Override
    public void set(V value) throws InterruptedException {
        if (!isSet.get() && isSet.compareAndSet(false, true)) {
            slot = value;
            latch.countDown();
        }
    }
}
