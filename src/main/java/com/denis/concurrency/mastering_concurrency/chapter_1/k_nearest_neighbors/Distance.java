package com.denis.concurrency.mastering_concurrency.chapter_1.k_nearest_neighbors;

public class Distance implements Comparable<Distance>{
    private int index;
    private double distance;

    public int getIndex() {
        return index;
    }

    public Distance setIndex(int index) {
        this.index = index;
        return this;
    }

    public double getDistance() {
        return distance;
    }

    public Distance setDistance(double distance) {
        this.distance = distance;
        return this;
    }


    @Override
    public int compareTo(Distance o) {
        return Double.compare(distance, o.distance);
    }
}
