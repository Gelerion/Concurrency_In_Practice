package com.denis.concurrency.settable_future.first_try.wait_notify.try_3_many_writers_diff_values;

import com.denis.concurrency.settable_future.CustomFuture;

import java.util.concurrent.ExecutionException;

/**
 * В этом случае каждый читатель обязан будет синхронизироваться на том же объекте, что и писатели в set(),
 * что как будто даёт гарантии безопасной публикации. Более того, именно такая идиома рекомендуется в
 * Javadoc к wait(). К сожалению, с безопасной публикацией ещё не всё чисто,
 * но Васины стресс-тесты уже проходят и теперь он может подумать о вечном. Во-первых, неплохо было
 * бы обеспечить однократную запись. А то чего доброго, получим в нескольких читателях разные значения.
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
        this.slot = value;
        synchronized (this) {
            notifyAll();
        }
    }
}
