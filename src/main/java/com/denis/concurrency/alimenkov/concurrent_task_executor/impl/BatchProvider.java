package com.denis.concurrency.alimenkov.concurrent_task_executor.impl;

import java.util.Date;
import java.util.List;

public interface BatchProvider {
    List<SerpBatch> getReadyBatches(Date date);
}
