package com.denis.concurrency.settable_future.second_try.latch.try_2;

import com.denis.concurrency.settable_future.CustomFuture;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;

public class SettableFuture<V> implements CustomFuture<V> {
    private final CountDownLatch latch = new CountDownLatch(1);
    private V slot;

    @Override
    public V get() throws InterruptedException, ExecutionException {
        latch.await();
        return slot;
    }

    @Override
    public void set(V value) {
        slot = value;
        latch.countDown();
    }
}