package com.denis.concurrency.alimenkov.transparent_batching.usage;

import com.denis.concurrency.alimenkov.transparent_batching.impl.BatchTaskExecutor;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class Use {
    BatchTaskExecutor<String> tasksExecutor;
    private long taskWaitTimeOut;

    void index() {
        try {
            tasksExecutor.addTask("Task").get(taskWaitTimeOut, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }
}
