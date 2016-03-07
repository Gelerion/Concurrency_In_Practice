package com.denis.concurrency.settable_future.first_try.wait_notify.try_4_race_condition_on_set;

import com.denis.concurrency.settable_future.CustomFuture;

import java.util.concurrent.ExecutionException;

/**
 * Усталый, но довольный, Вася деплоит это в продакшн, а через день к нему с дрыном прибегает ПМ и начинает
 * пороть Васю за intermittent гейзенбаги. Потирая ушибленные места, Василий осознает,
 * что в этом коде гонка на set(): два писателя могут оба выставить значения, потому что
 * каждый из них не увидит апдейт другого. А вот get()-ы смогут прочитать эти разные значения, если очень не повезёт.
 */
public class SettableFuture<V> implements CustomFuture<V> {
    private V slot;

    @Override
    public V get() throws InterruptedException, ExecutionException {
        synchronized (this) {
            while (slot == null) {
                wait();
            }
        }
        return slot;
    }

    @Override
    public void set(V value) {
        if (slot == null) { // ещё не записали?
            this.slot = value;
            synchronized (this) {
                notifyAll();
            }
        }
    }
}
