package vehicles;
import exceptions.*;

public abstract class LandVehicle extends Vehicle {
    
    private int numWheels;

    public LandVehicle(String id, String model, double maxSpeed, double currentMileage, int numWheels) throws InvalidOperationException {
        super(id, model, maxSpeed, currentMileage);
        if(numWheels <= 0){
            throw new InvalidOperationException("Invalid number of wheels");
        }
        this.numWheels = numWheels;
    }

    @Override
    public double estimateJourneyTime(double distance) {
        return (distance / getMaxSpeed()) * 1.1;
    }

    public int getNumWheels() {
        return this.numWheels;
    }
}
