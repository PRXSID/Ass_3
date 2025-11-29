package concreteclasses;

import vehicles.*;
import interfaces.*;
import exceptions.*;

public class CargoShip extends WaterVehicle implements CargoCarrier, Maintainable, FuelConsumable {
    //properites
    private double fuelLevel;
    private double cargoCapacity;
    private double currentCargo;
    private boolean maintenanceNeeded;

    //constructor
    public CargoShip(String id, String model, double maxSpeed, double currentMileage, boolean hasSail,
                     double fuelLevel, double currentCargo, boolean maintenanceNeeded)
            throws InvalidOperationException {
        super(id, model, maxSpeed, currentMileage, hasSail);
        if (fuelLevel < 0 || currentCargo < 0) {
            throw new InvalidOperationException("Initial values for fuel or cargo cannot be negative.");
        }
        this.fuelLevel = fuelLevel;
        this.cargoCapacity = 50000.0;
        this.currentCargo = currentCargo;
        this.maintenanceNeeded = maintenanceNeeded;
    }

    @Override
    public void move(double distance) throws InvalidOperationException, InsufficientFuelException {
        if (distance <= 0) {
            throw new InvalidOperationException("Distance cannot be negative.");
        }
        if (getSail()) {
            System.out.println("Sailing using wind...");
            updateMileage(distance);
            return;
        }
        double fuelNeeded = distance / calculateFuelEfficiency();
        if (fuelLevel < fuelNeeded) {
            throw new InsufficientFuelException("Not enough fuel for " + distance + " km journey.");
        }
        System.out.println("Motoring through water...");
        updateMileage(distance);
        consumeFuel(fuelNeeded);
        if (getCurrentMileage() % 20000 >= 18000) {
            maintenanceNeeded = true;
        }
    }

    @Override
    public double calculateFuelEfficiency() {
        return 1.0; // km per liter
    }

    @Override
    public void refuel(double amount) throws InvalidOperationException {
        if (amount < 0) {
            throw new InvalidOperationException("Cannot refuel with a negative amount.");
        }
        this.fuelLevel += amount;
        System.out.println("CargoShip with ID " + getId() + " refueled. New fuel level: " + this.fuelLevel);
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
            throw new InsufficientFuelException("Not enough fuel to consume.");
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
            System.out.println("CargoShip maintenance scheduled.");
        }
    }

    @Override
    public void performMaintenance() {
        if (needsMaintenance()) {
            System.out.println("Carrying out maintenance on CargoShip...");
            maintenanceNeeded = false;
        }
    }

    @Override
    public void loadCargo(double weight) throws OverloadException {
        if (currentCargo + weight > cargoCapacity) {
            throw new OverloadException("Exceeding CargoShip's load capacity!");
        }
        currentCargo += weight;
    }

    @Override
    public void unloadCargo(double weight) throws InvalidOperationException {
        if (weight > currentCargo) {
            throw new InvalidOperationException("Attempting to unload more than current cargo.");
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