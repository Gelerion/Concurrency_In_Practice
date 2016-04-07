package com.denis.concurrency.mastering_concurrency.chapter_1.k_nearest_neighbors.coarse_grained;

import com.denis.concurrency.mastering_concurrency.chapter_1.k_nearest_neighbors.Distance;
import com.denis.concurrency.mastering_concurrency.chapter_1.k_nearest_neighbors.EuclideanDistanceCalculator;
import com.denis.concurrency.mastering_concurrency.chapter_1.k_nearest_neighbors.Sample;

import java.util.List;
import java.util.concurrent.CountDownLatch;

class GroupDistanceTask implements Runnable {
    private Distance[] distances;
    private int startIndex, endIndex;
    private Sample example;
    private List<? extends Sample> dataSet;
    private CountDownLatch endController;

    GroupDistanceTask(Distance[] distances, int startIndex, int
            endIndex, List<? extends Sample> dataSet, Sample example, CountDownLatch
                              endController) {
        this.distances = distances;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        this.example = example;
        this.dataSet = dataSet;
        this.endController = endController;
    }

    public void run() {
        for (int index = startIndex; index < endIndex; index++) {
            Sample localExample = dataSet.get(index);
            distances[index] = new Distance();
            distances[index].setIndex(index);
            distances[index].setDistance(EuclideanDistanceCalculator
                    .calculate(localExample, example));
        }
        endController.countDown();
    }
}
