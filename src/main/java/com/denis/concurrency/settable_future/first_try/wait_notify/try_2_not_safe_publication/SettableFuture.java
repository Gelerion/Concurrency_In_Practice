package com.denis.concurrency.settable_future.first_try.wait_notify.try_2_not_safe_publication;

import com.denis.concurrency.settable_future.CustomFuture;

import java.util.concurrent.ExecutionException;

/**
 * Как будто бы починено, но на самом деле пара set() / get() не является так называемой "безопасной публикацией".
 * Так, если один поток set()'ом выставит slot в какое-нибудь значение, то второй поток может прийти в get(),
 * обнаружить slot != null и с ним вернуться. Модель памяти при этом <b>не гарантирует</b>, что все записи в объект,
 * опубликованный вторым потоком, будут видны первому. Безопасная публикация есть довольно интересный феномен,
 * который позволяет хорошо локализовывать проблемы с видимостью значений. Большая часть конкаррентных JDK’шных
 * классов безопасно публикует объекты, например, Executor.submit() и прочие.
 */
public class SettableFuture<V> implements CustomFuture<V> {
    private V slot;

    @Override
    public V get() throws InterruptedException, ExecutionException {
        while (slot == null) {
            synchronized (this) {
                wait();
            }
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
