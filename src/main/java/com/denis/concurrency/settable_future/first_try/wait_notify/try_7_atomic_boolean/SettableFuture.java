package com.denis.concurrency.settable_future.first_try.wait_notify.try_7_atomic_boolean;

import com.denis.concurrency.settable_future.CustomFuture;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Этот код тоже поломан: читатель может успешно пройти через while, обнаружить, что флажок уже выставлен,
 * но значение ещё не выставлено, и читатель рискует уйти с null’ом. Для видимости надо бы перенести
 * присвоение slot перед CAS’ом, но это разрешит повторную запись. От этой гонки не спасёт даже volatile на slot.
 */
public class SettableFuture<V> implements CustomFuture<V> {
    private V slot;
    private final AtomicBoolean isSet = new AtomicBoolean();

    @Override
    public V get() throws InterruptedException, ExecutionException {
        synchronized (this) {
            while (!isSet.get()) {
                wait();
            }
        }
        return slot;
    }

    @Override
    public void set(V value) {
        if (isSet.compareAndSet(false, true)) {
            slot = value;
            synchronized (this) {
                notifyAll();
            }
        }
    }
}