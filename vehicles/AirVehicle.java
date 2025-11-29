package vehicles;
import exceptions.*;

public abstract class AirVehicle extends Vehicle {
    private double maxAltitude;

    public AirVehicle(String id, String model, double maxSpeed, double currentMileage, double maxAltitude) throws InvalidOperationException {
        super(id, model, maxSpeed, currentMileage);
        if(maxAltitude <= 0) throw new InvalidOperationException("Altitude can't be less than 0");
        this.maxAltitude = maxAltitude;
    }
    
    @Override
    public double estimateJourneyTime(double distance) {
        return (distance / getMaxSpeed()) * 0.95;
    }
    public double getMaxAltitude() {
        return this.maxAltitude;
    }
}
