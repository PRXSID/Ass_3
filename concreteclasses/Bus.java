package concreteclasses;

import vehicles.*;
import interfaces.*;
import exceptions.*;

public class Bus extends LandVehicle implements FuelConsumable, PassengerCarrier, CargoCarrier, Maintainable {
    // properties
    private double fuelLevel;
    private int passengerCapacity;
    private int currentPassengers;
    private double cargoCapacity;
    private double currentCargo;
    private boolean maintenanceNeeded;

    //constructor
    public Bus(String id, String model, double maxSpeed, double currentMileage, int numWheels,
               double fuelLevel, int passengerCapacity, int currentPassengers,
               double cargoCapacity, double currentCargo, boolean maintenanceNeeded)
            throws InvalidOperationException {
        super(id, model, maxSpeed, currentMileage, numWheels);
        if (fuelLevel < 0 || passengerCapacity <= 0 || currentPassengers < 0 || currentPassengers > passengerCapacity ||
                cargoCapacity <= 0 || currentCargo < 0 || currentCargo > cargoCapacity) {
            throw new InvalidOperationException("Invalid initial values for Bus.");
        }
        this.fuelLevel = fuelLevel;
        this.passengerCapacity = passengerCapacity;
        this.currentPassengers = currentPassengers;
        this.cargoCapacity = cargoCapacity;
        this.currentCargo = currentCargo;
        this.maintenanceNeeded = maintenanceNeeded;
    }

    @Override
    public void move(double distance) throws InvalidOperationException, InsufficientFuelException {
        if (distance <= 0) {
            throw new InvalidOperationException("Distance cannot be negative.");
        }
        double requiredFuel = distance / calculateFuelEfficiency();
        if (fuelLevel < requiredFuel) {
            throw new InsufficientFuelException("Insufficient fuel for " + distance + " km journey.");
        }
        System.out.println("Transporting passengers and cargo...");
        updateMileage(distance);
        consumeFuel(requiredFuel);
        if (getCurrentMileage() % 5000 >= 4000) {
            maintenanceNeeded = true;
        }
    }

    @Override
    public double calculateFuelEfficiency() {
        return 5.0; // km per liter
    }

    @Override
    public void refuel(double amount) throws InvalidOperationException {
        if (amount < 0) {
            throw new InvalidOperationException("Cannot refuel with a negative amount.");
        }
        this.fuelLevel += amount;
        System.out.println("Bus with ID " + getId() + " refueled. New fuel level: " + this.fuelLevel);
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
            throw new InsufficientFuelException("Not enough fuel available.");
        }
        fuelLevel -= amount;
        return fuelLevel;
    }

    @Override
    public void boardPassengers(int count) throws OverloadException {
        if ((currentPassengers + count) > passengerCapacity) {
            throw new OverloadException("Bus passenger capacity exceeded.");
        }
        currentPassengers += count;
    }

    @Override
    public void disembarkPassengers(int count) throws InvalidOperationException {
        if (count > currentPassengers) {
            throw new InvalidOperationException("Disembarking more passengers than current ones.");
        }
        currentPassengers -= count;
    }

    @Override
    public int getPassengerCapacity() {
        return passengerCapacity;
    }

    @Override
    public int getCurrentPassengers() {
        return currentPassengers;
    }

    @Override
    public boolean needsMaintenance() {
        return maintenanceNeeded;
    }

    @Override
    public void scheduleMaintenance() {
        if (needsMaintenance()) {
            System.out.println("Bus maintenance scheduled.");
        }
    }

    @Override
    public void performMaintenance() {
        if (needsMaintenance()) {
            System.out.println("Performing bus maintenance...");
            maintenanceNeeded = false;
        }
    }

    @Override
    public void loadCargo(double weight) throws OverloadException {
        if (currentCargo + weight > cargoCapacity) {
            throw new OverloadException("Bus cargo overloaded.");
        }
        currentCargo += weight;
    }

    @Override
    public void unloadCargo(double weight) throws InvalidOperationException {
        if (currentCargo - weight < 0) {
            throw new InvalidOperationException("Unloading weight is more than the current weight of Cargo.");
        }
        currentCargo -= weight;
    }

    @Override
    public double getCargoCapacity() {
        return cargoCapacity;
    }

    @Override
    public double getCurrentCargo() {
        return currentCargo;
    }
}