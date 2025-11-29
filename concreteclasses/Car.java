package concreteclasses;

import vehicles.*;
import interfaces.*;
import exceptions.*;

public class Car extends LandVehicle implements FuelConsumable, PassengerCarrier, Maintainable {
    //properties
    private double fuelLevel;
    private int passengerCapacity;
    private int currentPassengers;
    private boolean maintenanceNeeded;

    // constructor
    public Car(String id, String model, double maxSpeed, double currentMileage, int numWheels,
               double fuelLevel, int passengerCapacity, int currentPassengers, boolean maintenanceNeeded)
            throws InvalidOperationException {
        super(id, model, maxSpeed, currentMileage, numWheels);

        if (fuelLevel < 0 || passengerCapacity <= 0 || currentPassengers < 0 || currentPassengers > passengerCapacity) {
            throw new InvalidOperationException("Invalid initial values for Car.");
        }
        this.fuelLevel = fuelLevel;
        this.passengerCapacity = passengerCapacity;
        this.currentPassengers = currentPassengers;
        this.maintenanceNeeded = maintenanceNeeded;
    }

    @Override
    public void move(double distance) throws InvalidOperationException, InsufficientFuelException {
        if (distance <= 0) {
            throw new InvalidOperationException("Distance must be positive for movement.");
        }
        double requiredFuel = (distance / calculateFuelEfficiency());
        if (fuelLevel < requiredFuel) {
            throw new InsufficientFuelException("Insufficient fuel: cannot travel " + distance + " km.");
        }
        System.out.println("Driving on road...");
        updateMileage(distance);
        consumeFuel(requiredFuel);
        if (getCurrentMileage() % 1000 >= 500) {
            maintenanceNeeded = true;
        }
    }

    @Override
    public double calculateFuelEfficiency() {
        return 15.0; // km per liter
    }

    @Override
    public void refuel(double amount) throws InvalidOperationException {
        if (amount < 0) {
            throw new InvalidOperationException("Cannot refuel with a negative amount.");
        }
        this.fuelLevel += amount;
        System.out.println("Car with ID " + getId() + " refueled. New fuel level: " + this.fuelLevel);
    }

    @Override
    public double getFuelLevel() {
        return this.fuelLevel;
    }

    @Override
    public double consumeFuel(double amount) throws InsufficientFuelException {
        if (amount <= 0) {
            throw new InsufficientFuelException("Consumption amount must be positive.");
        }
        if (fuelLevel < amount) {
            throw new InsufficientFuelException("Fuel level too low for this consumption.");
        }
        fuelLevel -= amount;
        return fuelLevel;
    }

    @Override
    public void boardPassengers(int count) throws OverloadException {
        if ((currentPassengers + count) > passengerCapacity) {
            throw new OverloadException("Passenger capacity exceeded for Car.");
        }
        currentPassengers += count;
    }

    @Override
    public void disembarkPassengers(int count) throws InvalidOperationException {
        if (count > currentPassengers) {
            throw new InvalidOperationException("Cannot disembark more passengers than present.");
        }
        currentPassengers -= count;
    }

    @Override
    public int getPassengerCapacity() {
        return this.passengerCapacity;
    }

    @Override
    public int getCurrentPassengers() {
        return this.currentPassengers;
    }

    @Override
    public boolean needsMaintenance() {
        return maintenanceNeeded;
    }

    @Override
    public void scheduleMaintenance() {
        if (maintenanceNeeded) {
            System.out.println("Maintenance has been scheduled for the Car.");
        }
    }

    @Override
    public void performMaintenance() {
        if (maintenanceNeeded) {
            System.out.println("Performing maintenance now...");
            maintenanceNeeded = false;
        }
    }
}