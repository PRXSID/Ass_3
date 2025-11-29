package simulator;

import java.util.concurrent.locks.ReentrantLock;

/**
 * Shared Highway Distance Counter that tracks total kilometers traveled by all vehicles.
 * Demonstrates race condition when accessed without synchronization.
 */
public class HighwayDistanceCounter {
    
    // Shared counter - intentionally not synchronized for race condition demonstration
    public static int highwayDistance = 0;
    
    // Synchronized counter for corrected behavior
    private static int synchronizedHighwayDistance = 0;
    
    // ReentrantLock for synchronized access
    private static final ReentrantLock lock = new ReentrantLock();
    
    // Flag to toggle between synchronized and unsynchronized mode
    private static boolean useSynchronization = false;
    
    /**
     * Increments the highway distance counter (unsynchronized version)
     * This method demonstrates race condition when multiple threads call it
     */
    public static void incrementUnsynchronized(int km) {
        // Deliberately introducing race condition with a read-modify-write pattern
        int current = highwayDistance;
        // Simulating processing delay to increase chance of race condition
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        highwayDistance = current + km;
    }
    
    /**
     * Increments the highway distance counter (synchronized version using synchronized keyword)
     */
    public static synchronized void incrementSynchronized(int km) {
        synchronizedHighwayDistance += km;
    }
    
    /**
     * Increments the highway distance counter using ReentrantLock
     */
    public static void incrementWithLock(int km) {
        lock.lock();
        try {
            synchronizedHighwayDistance += km;
        } finally {
            lock.unlock();
        }
    }
    
    /**
     * Unified increment method that chooses based on synchronization mode
     */
    public static void increment(int km) {
        if (useSynchronization) {
            incrementWithLock(km);
        } else {
            incrementUnsynchronized(km);
        }
    }
    
    /**
     * Gets the current unsynchronized highway distance
     */
    public static int getUnsynchronizedDistance() {
        return highwayDistance;
    }
    
    /**
     * Gets the current synchronized highway distance
     */
    public static int getSynchronizedDistance() {
        return synchronizedHighwayDistance;
    }
    
    /**
     * Gets the current distance based on mode
     */
    public static int getDistance() {
        return useSynchronization ? synchronizedHighwayDistance : highwayDistance;
    }
    
    /**
     * Sets the synchronization mode
     */
    public static void setSynchronizationMode(boolean enabled) {
        useSynchronization = enabled;
    }
    
    /**
     * Gets the current synchronization mode
     */
    public static boolean isSynchronizationEnabled() {
        return useSynchronization;
    }
    
    /**
     * Resets both counters
     */
    public static void reset() {
        highwayDistance = 0;
        synchronizedHighwayDistance = 0;
    }
}
