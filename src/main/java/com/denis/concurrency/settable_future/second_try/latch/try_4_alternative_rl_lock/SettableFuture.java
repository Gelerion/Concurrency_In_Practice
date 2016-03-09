package com.denis.concurrency.settable_future.second_try.latch.try_4_alternative_rl_lock;

import com.denis.concurrency.settable_future.CustomFuture;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.locks.ReentrantLock;

public class SettableFuture<V> implements CustomFuture<V> {
    private final CountDownLatch latch = new CountDownLatch(1);
    private final ReentrantLock lock = new ReentrantLock();
    private V slot;

    @Override
    public V get() throws InterruptedException, ExecutionException {
        latch.await();
        return slot;
    }

    @Override
    public void set(V value) throws InterruptedException {
        if (lock.tryLock()) {
            try {
                if (latch.getCount() != 0) {
                    slot = value;
                    latch.countDown();
                }
            } finally {
                lock.unlock();
            }
        }
    }
}

