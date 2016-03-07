package com.denis.concurrency.settable_future.first_try.wait_notify.try_1_spurious_wakeups;

import com.denis.concurrency.settable_future.CustomFuture;

import java.util.concurrent.ExecutionException;

/**
 * Это не работает, потому что wait() допускает spurious wakeups. То есть, читатели рискуют уйти с null’ом в руках.
 * @param <V>
 */
public class SettableFuture<V> implements CustomFuture<V> {
    private V slot;

    @Override
    public V get() throws InterruptedException, ExecutionException {
        synchronized (this) {
            wait();
        }

        return slot;
    }

    @Override
    public void set(V value) {
        this.slot = value;
        synchronized (this) {
            notifyAll();
        }
    }
}
