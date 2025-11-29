package concreteclasses;

import vehicles.*;
import interfaces.*;
import exceptions.*;

public class Truck extends LandVehicle implements FuelConsumable, CargoCarrier, Maintainable {
    //properties
    private double fuelLevel;
    private double cargoCapacity;
    private double currentCargo;
    private boolean maintenanceNeeded;

    //constructor
    public Truck(String id, String model, double maxSpeed, double currentMileage, int numWheels,
                 double fuelLevel, double cargoCapacity, double currentCargo, boolean maintenanceNeeded)
            throws InvalidOperationException {
        super(id, model, maxSpeed, currentMileage, numWheels);

        if (fuelLevel < 0 || cargoCapacity <= 0 || currentCargo < 0 || currentCargo > cargoCapacity) {
            throw new InvalidOperationException("Invalid initial values for fuel or cargo.");
        }
        this.fuelLevel = fuelLevel;
        this.cargoCapacity = cargoCapacity;
        this.currentCargo = currentCargo;
        this.maintenanceNeeded = maintenanceNeeded;
    }

    @Override
    public void move(double distance) throws InvalidOperationException, InsufficientFuelException {
        if (distance <= 0) {
            throw new InvalidOperationException("Distance must be greater than zero.");
        }
        double fuelNeeded = distance / calculateFuelEfficiency();
        if (fuelLevel < fuelNeeded) {
            throw new InsufficientFuelException("Truck does not have enough fuel for " + distance + " km.");
        }
        System.out.println("Hauling cargo by truck...");
        updateMileage(distance);
        consumeFuel(fuelNeeded);
        if (getCurrentMileage() % 10000 >= 9000) {
            maintenanceNeeded = true;
        }
    }

    @Override
    public double calculateFuelEfficiency() {
        return 2.0; // km per liter
    }

    @Override
    public void refuel(double amount) throws InvalidOperationException {
        if (amount < 0) {
            throw new InvalidOperationException("Cannot refuel with a negative amount.");
        }
        this.fuelLevel += amount;
        System.out.println("Truck with ID " + getId() + " refueled. New fuel level: " + this.fuelLevel);
    }

    @Override
    public double getFuelLevel() {
        return this.fuelLevel;
    }

    @Override
    public double consumeFuel(double amount) throws InsufficientFuelException {
        if (amount <= 0) {
            throw new InsufficientFuelException("Consumption value must be greater than zero.");
        }
        if (fuelLevel < amount) {
            throw new InsufficientFuelException("Not enough fuel available for consumption.");
        }
        fuelLevel -= amount;
        return fuelLevel;
    }

    @Override
    public boolean needsMaintenance() {
        return maintenanceNeeded;
    }

    @Override
    public void scheduleMaintenance() {
        if (needsMaintenance()) {
            System.out.println("Truck maintenance scheduled.");
        }
    }

    @Override
    public void performMaintenance() {
        if (needsMaintenance()) {
            System.out.println("Performing truck maintenance...");
            maintenanceNeeded = false;
        }
    }

    @Override
    public void loadCargo(double weight) throws OverloadException {
        if (currentCargo + weight > cargoCapacity) {
            throw new OverloadException("Truck is getting overloaded.");
        }
        currentCargo += weight;
    }

    @Override
    public void unloadCargo(double weight) throws InvalidOperationException {
        if (weight > currentCargo) {
            throw new InvalidOperationException("Cannot unload weight more than the current cargo weight.");
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