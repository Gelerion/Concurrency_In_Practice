package com.denis.concurrency.settable_future.first_try.wait_notify.try_5_volatile_solution;

import com.denis.concurrency.settable_future.CustomFuture;

import java.util.concurrent.ExecutionException;

/**
 * Этим, кстати, Вася поправит и проблему с безопасной публикацией, но вряд ли это заметит.
 *
 * На следующее утро Васю встретит уже близко знакомый ему ПМ и спросит, почему баги всё ещё
 * проявляются, хотя и реже? Удивленный Вася пойдёт читать форумы и внезапно осознает, что пара
 * пераций над volatile не атомарна: может оказаться, что slot == null в обоих потоках-писателях,
 * и они всё равно запишут оба значения. Просветившись, Вася сделает следующий локально-оптимальный шаг:
 */
public class SettableFuture<V> implements CustomFuture<V> {
    //модификатор volatile
    private volatile V slot;

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
        if (slot == null) {
            this.slot = value;
            synchronized (this) {
                notifyAll();
            }
        }
    }
}

