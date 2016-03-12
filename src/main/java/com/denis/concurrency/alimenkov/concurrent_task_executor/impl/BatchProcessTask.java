package com.denis.concurrency.alimenkov.concurrent_task_executor.impl;

import java.util.Date;
import java.util.concurrent.Callable;

public class BatchProcessTask implements Callable<Boolean> {

    public BatchProcessTask(SerpBatch serp, Date date) {}

    @Override
    public Boolean call() throws Exception {
        return null;
    }
}
