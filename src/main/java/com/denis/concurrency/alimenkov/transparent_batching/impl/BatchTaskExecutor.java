package com.denis.concurrency.alimenkov.transparent_batching.impl;

import com.denis.concurrency.alimenkov.common.Validate;

import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static com.denis.concurrency.alimenkov.shared_stop.ShutdownService.isCancelled;

public class BatchTaskExecutor<T>  {
    private ArrayBlockingQueue<BatchTask<T>> tasks = new ArrayBlockingQueue<>(256);
    private ArrayList<BatchTask<T>> batch;
    private BatchDelayStrategy batchDelayStrategy;
    private BatchProcessor processor;
    private long TASK_WAIT_TIMEOUT = 2;

    public Future<T> addTask(T data) {
        BatchTask<T> task = new BatchTask<>(data);
        tasks.add(task);
        return task;
    }


    private class BatchTaskMonitor implements Runnable {

        @Override
        public void run() {
            while (!isCancelled())  {
                tryToProcessNextBatch();
            }
        }

        private void tryToProcessNextBatch() {
            try {
                waitForNextBatch();
                processBatch();
                releaseTasks();
            } catch (InterruptedException e) {
                failTasks(e);
            } finally {
                clearBatch();
            }
        }

        private void releaseTasks() {
            for (BatchTask<T> task : batch) {
                task.run();
            }
        }

        private void processBatch() {
            tasks.drainTo(batch);
            for (BatchTask<T> task : batch) {
                processor.processTask(task.getData());
            }
            processor.batchProcessed();
        }

        private void clearBatch() {

        }

        private void failTasks(InterruptedException e) {
            for (BatchTask<T> task : batch) {
                task.setException(e);
            }
        }

        private void waitForNextBatch() throws InterruptedException {
            BatchTask<T> newTask = tasks.poll(TASK_WAIT_TIMEOUT, TimeUnit.SECONDS);
            Validate.notNull(newTask, "Task waiting timeout exceed");
            batch.add(newTask);
            int delay = batchDelayStrategy.getBatchWaitDelay(newTask.getCreatedAt(), tasks.size() + 1);
            if (delay > 0) {
                Thread.sleep(delay);
            }

        }
    }
}
