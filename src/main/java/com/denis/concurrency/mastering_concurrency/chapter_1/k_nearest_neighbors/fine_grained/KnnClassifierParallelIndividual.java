package com.denis.concurrency.mastering_concurrency.chapter_1.k_nearest_neighbors.fine_grained;

import com.denis.concurrency.mastering_concurrency.chapter_1.k_nearest_neighbors.Distance;
import com.denis.concurrency.mastering_concurrency.chapter_1.k_nearest_neighbors.Sample;

import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * fine-grained granularity (small tasks with high intercommunication)
 * <p>
 * The concurrent solution presented may have a problem. You are
 * executing too many tasks.
 */
public class KnnClassifierParallelIndividual {
    private List<? extends Sample> dataSet;
    private int k;
    private ThreadPoolExecutor executor;
    private int numThreads;
    private boolean parallelSort;

    public KnnClassifierParallelIndividual(List<? extends Sample> dataSet,
                                           int k, int factor, boolean parallelSort) {
        this.dataSet = dataSet;
        this.k = k;
        numThreads = factor * (Runtime.getRuntime().availableProcessors());
        executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(numThreads);
        this.parallelSort = parallelSort;
    }

    /**
     * Note that although all the tasks share the array of distances, we don’t need to use any
     * synchronization mechanism because each task will modify a different position of the
     * array.
     */
    public String classify(Sample example) throws Exception {
        Distance[] distances = new Distance[dataSet.size()];
        CountDownLatch endController = new CountDownLatch(dataSet.size());

        int index = 0;
        for (Sample localExample : dataSet) {
            IndividualDistanceTask task = new IndividualDistanceTask(distances,
                    index, localExample, example, endController);
            executor.execute(task);
            index++;
        }
        endController.await();

        if (parallelSort) {
            Arrays.parallelSort(distances);
        } else {
            Arrays.sort(distances);
        }

        HashMap<String, Integer> results = new HashMap<>();
        for (int i = 0; i < k; i++) {
            Sample localExample = dataSet.get(distances[i].getIndex());
            String tag = localExample.getTag();
            results.merge(tag, 1, (a, b) -> a + b);
        }

        return Collections.max(results.entrySet(), Map.Entry.comparingByValue()).getKey();
    }

    public void destroy() {
        executor.shutdown();
    }
}
