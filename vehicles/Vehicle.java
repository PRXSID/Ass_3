package vehicles;
import exceptions.*;
import java.util.List;

public abstract class Vehicle implements Comparable<Vehicle> {

    private String id;
    private String model;
    private double maxSpeed;
    private double currentMileage;

    public Vehicle(String id, String model, double maxSpeed, double currentMileage) throws InvalidOperationException {
        if (id == null || id.isEmpty()) {
            throw new InvalidOperationException("Vehicle ID cannot be empty!");
        }
        if(maxSpeed <= 0){
            throw new InvalidOperationException("Invalid max speed");
        }
        this.id = id;
        this.model = model;
        this.maxSpeed = maxSpeed;
        this.currentMileage = currentMileage;
    }

    public abstract void move(double distance) throws InvalidOperationException, InsufficientFuelException;
    public abstract double calculateFuelEfficiency();
    public abstract double estimateJourneyTime(double distance);

    public void displayInfo() {
        System.out.println("ID: " + id);
        System.out.println("Model: " + model);
        System.out.println("Max Speed: " + maxSpeed);
        System.out.println("Current Mileage: " + currentMileage);
    }

    public String getModel(){
        return this.model;
    }

    public double getCurrentMileage() {
        return this.currentMileage;
    }

    public String getId() {
        return this.id;
    }

    public double getMaxSpeed() {
        return this.maxSpeed;
    }

    protected void updateMileage(double distance) {
        this.currentMileage += distance;
    }

    @Override
    public int compareTo(Vehicle other) {
        return Double.compare(other.calculateFuelEfficiency(), this.calculateFuelEfficiency());
    }

    public static void checkUniqueId(List<Vehicle> fleet, String newId) throws InvalidOperationException {
        for (Vehicle v : fleet) {
            if (v.getId().equals(newId)) {
                throw new InvalidOperationException("Vehicle ID must be unique: " + newId);
            }
        }
    }
}