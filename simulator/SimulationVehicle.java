package simulator;

/**
 * SimulationVehicle represents a vehicle in the highway simulation.
 * Each vehicle runs in its own thread and periodically updates its mileage,
 * fuel level, and the shared Highway Distance Counter.
 */
public class SimulationVehicle extends Thread {
    
    // Vehicle properties
    private final String vehicleId;
    private double mileage;
    private double fuelLevel;
    private final double maxFuel;
    
    // Vehicle status
    public enum Status {
        RUNNING, PAUSED, OUT_OF_FUEL, STOPPED
    }
    private volatile Status status;
    
    // Control flags
    private volatile boolean running;
    private volatile boolean paused;
    
    // Reference to callback for UI updates
    private VehicleUpdateCallback updateCallback;
    
    // Constants
    private static final double FUEL_CONSUMPTION_RATE = 0.5; // fuel per km
    private static final int UPDATE_INTERVAL_MS = 1000; // 1 second
    
    /**
     * Constructor for SimulationVehicle
     */
    public SimulationVehicle(String vehicleId, double initialFuel) {
        this.vehicleId = vehicleId;
        this.mileage = 0;
        this.fuelLevel = initialFuel;
        this.maxFuel = initialFuel;
        this.status = Status.PAUSED;
        this.running = false;
        this.paused = true;
    }
    
    /**
     * Sets the callback for UI updates
     */
    public void setUpdateCallback(VehicleUpdateCallback callback) {
        this.updateCallback = callback;
    }
    
    /**
     * Main thread execution
     */
    @Override
    public void run() {
        running = true;
        while (running) {
            try {
                if (!paused && fuelLevel > 0) {
                    // Increment mileage by 1 km
                    mileage += 1;
                    
                    // Decrease fuel
                    fuelLevel -= FUEL_CONSUMPTION_RATE;
                    if (fuelLevel < 0) {
                        fuelLevel = 0;
                    }
                    
                    // Update shared Highway Distance Counter
                    HighwayDistanceCounter.increment(1);
                    
                    // Update status
                    if (fuelLevel <= 0) {
                        status = Status.OUT_OF_FUEL;
                        paused = true;
                    } else {
                        status = Status.RUNNING;
                    }
                    
                    // Notify UI
                    if (updateCallback != null) {
                        updateCallback.onVehicleUpdate(this);
                    }
                } else if (paused && fuelLevel > 0) {
                    status = Status.PAUSED;
                }
                
                Thread.sleep(UPDATE_INTERVAL_MS);
                
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        status = Status.STOPPED;
        if (updateCallback != null) {
            updateCallback.onVehicleUpdate(this);
        }
    }
    
    /**
     * Starts/resumes the vehicle simulation
     */
    public void resumeVehicle() {
        if (fuelLevel > 0) {
            paused = false;
            status = Status.RUNNING;
        }
    }
    
    /**
     * Pauses the vehicle simulation
     */
    public void pauseVehicle() {
        paused = true;
        if (fuelLevel > 0) {
            status = Status.PAUSED;
        }
    }
    
    /**
     * Stops the vehicle simulation permanently
     */
    public void stopVehicle() {
        running = false;
        paused = true;
        status = Status.STOPPED;
        this.interrupt();
    }
    
    /**
     * Refuels the vehicle
     */
    public void refuel(double amount) {
        fuelLevel = Math.min(fuelLevel + amount, maxFuel);
        if (fuelLevel > 0 && status == Status.OUT_OF_FUEL) {
            status = Status.PAUSED;
        }
    }
    
    /**
     * Refuels the vehicle to maximum capacity
     */
    public void refuelFull() {
        fuelLevel = maxFuel;
        if (status == Status.OUT_OF_FUEL) {
            status = Status.PAUSED;
            // Automatically resume after refueling
            resumeVehicle();
        }
    }
    
    // Getters
    public String getVehicleId() {
        return vehicleId;
    }
    
    public double getMileage() {
        return mileage;
    }
    
    public double getFuelLevel() {
        return fuelLevel;
    }
    
    public double getMaxFuel() {
        return maxFuel;
    }
    
    public Status getVehicleStatus() {
        return status;
    }
    
    public boolean isPaused() {
        return paused;
    }
    
    public boolean isRunning() {
        return running;
    }
    
    /**
     * Interface for vehicle update callbacks
     */
    public interface VehicleUpdateCallback {
        void onVehicleUpdate(SimulationVehicle vehicle);
    }
}
