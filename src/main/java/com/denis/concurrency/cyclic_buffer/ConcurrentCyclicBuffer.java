package com.denis.concurrency.cyclic_buffer;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * get/put cyclic buffer based on array
 */
@SuppressWarnings("unchecked")
public class ConcurrentCyclicBuffer<T> {
    private final Lock locker = new ReentrantLock();
    private final Condition isEmpty = locker.newCondition();
    private final Condition isFull = locker.newCondition();

    private final Object[] array;
    private final int maxSize;
    private int head;
    private int tail;
    private int size;

    public ConcurrentCyclicBuffer(int capacity) {
        this.maxSize = capacity;
        this.array = new Object[maxSize];
        this.head = 0;
        this.tail = -1;
        this.size = 0; //publish
    }

    public T get() throws InterruptedException {
        try {
            locker.lockInterruptibly();

            if (size == 0) {
                while (size == 0) {
                    isEmpty.await();
                }
            }

            if(head == maxSize) head = 0;

            size--;
            // now we have some place for adding new values (size decreased by 1), wake up put()
            isFull.signal();
            return (T) array[head++];

        } finally {
            locker.unlock();
        }
    }

    public void put(T elem) throws InterruptedException {
        try {
            locker.lockInterruptibly();

            if(size == maxSize) {
                while (size == maxSize)
                    isFull.await();
            }


            if(tail == maxSize -1) tail = -1;

            array[++tail] = elem;
            size++;
            // now we have some objects which can be retrieved by get (size increased by 1), wake up get()
            isEmpty.signal();
        } finally {
            locker.unlock();
        }

    }

    public static void main(String[] args) throws InterruptedException {
        ConcurrentCyclicBuffer<String> buffer = new ConcurrentCyclicBuffer<>(5);
        buffer.get();
    }

}
