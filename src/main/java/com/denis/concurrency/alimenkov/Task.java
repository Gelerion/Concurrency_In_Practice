package com.denis.concurrency.alimenkov;

import static com.denis.concurrency.alimenkov.shared_stop.ShutdownService.isCancelled;

public abstract class Task implements Runnable {
    @Override
    public void run() {
        while (!isCancelled()) {
            try {
                tryToDoTask();
            } catch (RuntimeException e) {
                onError(e);
            }
        }
    }

    protected abstract void onError(RuntimeException e);

    protected abstract void tryToDoTask();
}
