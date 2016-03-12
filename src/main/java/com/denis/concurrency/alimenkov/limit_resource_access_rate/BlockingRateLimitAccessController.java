package com.denis.concurrency.alimenkov.limit_resource_access_rate;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * On disruptor base
 */
public abstract class BlockingRateLimitAccessController {
    private int RING_SIZE = 60;
    public final Semaphore[] ring = new Semaphore[RING_SIZE];
    private final int maxRatePerSecond;
    // we can not clean semaphores for this second, and not previous one
    // so we are using some safe offset
    private int SAFE_OFFSET = 10;

    public BlockingRateLimitAccessController(int maxRatePerSecond) {
        this.maxRatePerSecond = maxRatePerSecond;
        for (int i = 0; i < ring.length; i++) {
            ring[i] = new Semaphore(maxRatePerSecond);
        }
    }

    //asking for this method per second
    //second is anr index of element in array
    public void acquireAccess() {
        while (!acquire()) {}
    }

    protected boolean acquire() {
        try {
            return tryToAcquire(getActiveRingCell());
        } catch (InterruptedException e) {
            return false;
        }
    }

    private boolean tryToAcquire(int cell) throws InterruptedException {
        Semaphore semaphore = ring[cell];
        long timeoutMs = calculateDelay();
        return semaphore.tryAcquire(timeoutMs, TimeUnit.MILLISECONDS);
    }

    protected abstract long calculateDelay();

    private int getActiveRingCell() {
        return (int) ((/*Clock.getTimeMillis()*/ System.currentTimeMillis() / 1000) % RING_SIZE);
    }

    //Reset not used semaphores
    // thread which works every 5 seconds
    public void clean() {
        int activeCell = getActiveRingCell();
        int left = (activeCell - SAFE_OFFSET + RING_SIZE) % RING_SIZE;
        int right = (activeCell - SAFE_OFFSET + 1) % RING_SIZE;
        for(int cell = right; cell != left; cell = nextCellInRing(cell)) {
            reset(cell);
        }
    }

    private void reset(int cell) {
        Semaphore semaphore = ring[cell];
        semaphore.drainPermits();
        semaphore.release(maxRatePerSecond);
    }

    private int nextCellInRing(int cell) {
        return (cell + 1) % RING_SIZE;
    }
}
