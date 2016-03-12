package com.denis.concurrency.alimenkov.concurrent_task_executor;

import com.denis.concurrency.alimenkov.concurrent_task_executor.impl.BatchProcessTask;
import com.denis.concurrency.alimenkov.concurrent_task_executor.impl.BatchProvider;
import com.denis.concurrency.alimenkov.concurrent_task_executor.impl.ConcurrentTasksExecutor;
import com.denis.concurrency.alimenkov.concurrent_task_executor.impl.SerpBatch;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class SubmitTasksInSingleThread {
    private ExecutorService executorService = Executors.newSingleThreadExecutor();
    private BatchProvider batchProvider;

    private void processBatches(Date date) {
        ConcurrentTasksExecutor<Boolean> tasksExecutor =
                new ConcurrentTasksExecutor<>(executorService, 5); //max tasks in a queue
        tasksExecutor.setTaskWaitTimeout(1, TimeUnit.MINUTES); //or default
        List<SerpBatch> ready = batchProvider.getReadyBatches(date);
        for (SerpBatch batch : ready) {
            tasksExecutor.submit(new BatchProcessTask(batch, date));
        }
        tasksExecutor.waitForAllTasksCompletion();
    }

}
