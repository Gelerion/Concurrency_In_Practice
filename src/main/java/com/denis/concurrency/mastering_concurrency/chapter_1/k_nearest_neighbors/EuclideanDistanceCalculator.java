package com.denis.concurrency.mastering_concurrency.chapter_1.k_nearest_neighbors;

public class EuclideanDistanceCalculator {

    public static double calculate(Sample example1, Sample example2) {
        double ret = 0.0d;

        double[] data1 = example1.getExample();
        double[] data2 = example2.getExample();

        if (data1.length!=data2.length) {
            throw new IllegalArgumentException ("Vector doesn't have the same length");
        }

        for (int i = 0; i < data1.length; i++) {
            ret += Math.pow(data1[i] - data2[i], 2);
        }

        return Math.sqrt(ret);
    }
}
