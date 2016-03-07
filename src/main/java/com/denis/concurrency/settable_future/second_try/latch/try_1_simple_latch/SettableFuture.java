package com.denis.concurrency.settable_future.second_try.latch.try_1_simple_latch;

import com.denis.concurrency.settable_future.CustomFuture;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;

/**
 * этот код допускает несколько записей. Петя молниеносно исправляет
 */
public class SettableFuture<V> implements CustomFuture<V> {
/*    Actions prior to "releasing" synchronizer methods such as Lock.unlock, Semaphore.release,
    and CountDownLatch.countDown happen-before actions subsequent
    to a successful "acquiring" method such as Lock.lock, Semaphore.acquire,
    Condition.await, and CountDownLatch.await on the same synchronizer object in another thread.*/
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