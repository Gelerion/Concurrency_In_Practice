package com.denis.concurrency.alimenkov.transparent_batching.impl;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class BatchTask<T> implements Future<T> {
    private Object data;

    public BatchTask(T data) {

    }

    public int getCreatedAt() {
        return 0;
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        return false;
    }

    @Override
    public boolean isCancelled() {
        return false;
    }

    @Override
    public boolean isDone() {
        return false;
    }

    @Override
    public T get() throws InterruptedException, ExecutionException {
        return null;
    }

    @Override
    public T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return null;
    }

    public T getData() {
        return null;
    }

    public void run() {

    }

    public void setException(InterruptedException exception) {
    }
}
