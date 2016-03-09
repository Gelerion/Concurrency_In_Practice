package com.denis.concurrency.settable_future.second_try.latch.try_2_race_condition;

import com.denis.concurrency.settable_future.CustomFuture;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;

/**
 * К сожалению, это гонка. Два потока входят в set(), оба обнаруживают латч в состоянии "можно писать",
 * записывают значение, и опускают латч. Несколько читателей могут прочитать разные значения.
 */
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
        if (latch.getCount() != 0) {
            slot = value;
            latch.countDown();
        }
    }
}