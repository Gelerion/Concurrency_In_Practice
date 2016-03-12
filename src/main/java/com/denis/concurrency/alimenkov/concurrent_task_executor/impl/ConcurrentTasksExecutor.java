package com.denis.concurrency.alimenkov.concurrent_task_executor.impl;

import com.denis.concurrency.alimenkov.common.Validate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class ConcurrentTasksExecutor<T> {
    private final ExecutorCompletionService<T> completionService;
    private final int concurrentTasksLimit;
    //how many tasks were submitted
    private int tasksCount;

    private final List<T> results = new ArrayList<>();

    //defaults
    private long taskWaitTimeout = 1;
    private TimeUnit taskWaitTimeoutUnit = TimeUnit.MINUTES;

    public ConcurrentTasksExecutor(ExecutorService executor, int concurrentTasksLimit) {
        this.completionService = new ExecutorCompletionService<>(executor);
        this.concurrentTasksLimit = concurrentTasksLimit;
    }

    public void submit(Callable<T> task) {
        checkLimit();
        completionService.submit(task);
        tasksCount++;
    }

    private void checkLimit() {
        if (tasksCount >= concurrentTasksLimit) {
            waitForTaskCompletion();
        }
    }

    public List<T> waitForAllTasksCompletion() {
        int waitedTasksCount = tasksCount;
        for (int i = 0; i < waitedTasksCount; i++) {
            waitForTaskCompletion();
        }
        return results;
    }

    public T waitForTaskCompletion() {
        try {
            T result = tryToGetNextTaskResult();
            results.add(result);
            return result;
        } catch (InterruptedException e) {
            throw new IllegalStateException("Unexpected error", e);
        }
    }

    private T tryToGetNextTaskResult() throws InterruptedException {
        Future<T> result = completionService.poll(taskWaitTimeout, taskWaitTimeoutUnit);
        Validate.notNull(result, "Task wait timeout exceed on task: " + tasksCount);
        return getResult(result);
    }

    private T getResult(Future<T> future) throws InterruptedException {
        try {
            return future.get();
        } catch (ExecutionException e) {
            throw getCause(e);
        }
        finally {
            tasksCount--;
        }
    }

    @SuppressWarnings("unchecked")
    private <E extends RuntimeException> E getCause(ExecutionException e) {
        return (E) e.getCause();
    }

    public void setTaskWaitTimeout(int waitTime, TimeUnit unit) {
        this.taskWaitTimeout = waitTime;
        this.taskWaitTimeoutUnit = unit;
    }
}
