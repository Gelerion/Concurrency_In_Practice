package com.denis.concurrency.mastering_concurrency.chapter_1.k_nearest_neighbors.step_1_single_thread;

import com.denis.concurrency.mastering_concurrency.chapter_1.k_nearest_neighbors.Distance;
import com.denis.concurrency.mastering_concurrency.chapter_1.k_nearest_neighbors.EuclideanDistanceCalculator;
import com.denis.concurrency.mastering_concurrency.chapter_1.k_nearest_neighbors.Sample;

import java.util.*;

class KnnClassifier {
    private List<? extends Sample> dataSet;
    private int k;

    public KnnClassifier(List<? extends Sample> dataSet, int k) {
        this.dataSet = dataSet;
        this.k = k;
    }

    public String classify(Sample example) {
        Distance[] distances = new Distance[dataSet.size()];

        int index = 0;

        for (Sample localExample : dataSet) {
            distances[index] = new Distance();
            distances[index].setIndex(index);
            distances[index].setDistance(EuclideanDistanceCalculator.calculate(localExample, example));
            index++;
        }

        //sort the examples from lower to higher distance
        Arrays.sort(distances);

        //count the tag with most instances in the k-nearest examples
        HashMap<String, Integer> results = new HashMap<>();
        for (int i = 0; i < k; i++) {
            Sample localExample = dataSet.get(distances[i].getIndex());
            String tag = localExample.getTag();
            results.merge(tag, 1, (a, b) -> a + b);
        }

        return Collections.max(results.entrySet(), Map.Entry.comparingByValue()).getKey();
    }
}
