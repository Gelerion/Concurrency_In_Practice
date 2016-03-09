package com.denis.concurrency.settable_future.second_try.latch.try_3_working_code;

import com.denis.concurrency.settable_future.CustomFuture;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Semaphore;

public class SettableFuture<V> implements CustomFuture<V> {
    private final CountDownLatch latch = new CountDownLatch(1);
    private final Semaphore semaphore = new Semaphore(1);
    private V slot;

    @Override
    public V get() throws InterruptedException, ExecutionException {
        latch.await();
        return slot;
    }

    @Override
    public void set(V value) throws InterruptedException {
        semaphore.acquire();
        try {
            if (latch.getCount() != 0) {
                slot = value;
                latch.countDown();
            }
        } finally {
            semaphore.release();
        }
    }
}
