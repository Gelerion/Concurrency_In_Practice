package com.denis.concurrency.mastering_concurrency.chapter_1.k_nearest_neighbors.fine_grained;

import com.denis.concurrency.mastering_concurrency.chapter_1.k_nearest_neighbors.Distance;
import com.denis.concurrency.mastering_concurrency.chapter_1.k_nearest_neighbors.EuclideanDistanceCalculator;
import com.denis.concurrency.mastering_concurrency.chapter_1.k_nearest_neighbors.Sample;

import java.util.concurrent.CountDownLatch;

class IndividualDistanceTask implements Runnable {
    private Distance[] distances;
    private int index;
    private Sample localExample;
    private Sample example;
    private CountDownLatch endController;

    IndividualDistanceTask(Distance[] distances, int index, Sample
            localExample, Sample example, CountDownLatch endController) {
        this.distances = distances;
        this.index = index;
        this.localExample = localExample;
        this.example = example;
        this.endController = endController;
    }

    @Override
    public void run() {
        distances[index] = new Distance();
        distances[index].setIndex(index);
        distances[index].setDistance(EuclideanDistanceCalculator.calculate(localExample, example));
        endController.countDown();
    }
}
