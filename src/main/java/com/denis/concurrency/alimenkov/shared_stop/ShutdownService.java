package com.denis.concurrency.alimenkov.shared_stop;

import java.util.concurrent.atomic.AtomicBoolean;

public class ShutdownService {
    private static final AtomicBoolean stopped = new AtomicBoolean();

    public static void shutDown() {
        boolean result;
        do {
            result = stopped.compareAndSet(false, true);
        } while (!result);
    }

    public static boolean isCancelled() {
        return stopped.get();
    }
}
