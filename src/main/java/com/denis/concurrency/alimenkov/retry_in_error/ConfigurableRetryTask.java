package com.denis.concurrency.alimenkov.retry_in_error;

import com.denis.concurrency.alimenkov.common.AlreadyStoppedException;

import static com.denis.concurrency.alimenkov.shared_stop.ShutdownService.isCancelled;

public abstract class ConfigurableRetryTask<T> {
    private /*final*/ long retryTimeoutMs;

    public T doTask() {
        do {
            try {
                return tryToDoTask();
            } catch (RuntimeException e) {
                onError(e);
            }
        } while (!isCancelled());

        // was stopped, we could ignore this one
        throw new AlreadyStoppedException("Task interrupted");
    }

    protected abstract void onError(RuntimeException e);

    //if fail sleep
    protected abstract T tryToDoTask();
}
