package concreteclasses;

import vehicles.*;
import interfaces.*;
import exceptions.*;

public class Airplane extends AirVehicle implements FuelConsumable, PassengerCarrier, CargoCarrier, Maintainable {
    //properties
    private double fuelLevel;
    private int passengerCapacity;
    private int currentPassengers;
    private double cargoCapacity;
    private double currentCargo;
    private boolean maintenanceNeeded;

    // constructor
    public Airplane(String id, String model, double maxSpeed, double currentMileage, double maxAltitude,
                    double fuelLevel, int currentPassengers, double currentCargo, boolean maintenanceNeeded)
            throws InvalidOperationException {
        super(id, model, maxSpeed, currentMileage, maxAltitude);
        if (fuelLevel < 0 || currentPassengers < 0 || currentCargo < 0) {
            throw new InvalidOperationException("Initial values for fuel, passengers, or cargo cannot be negative.");
        }
        this.fuelLevel = fuelLevel;
        this.passengerCapacity = 200;
        this.currentPassengers = currentPassengers;
        this.cargoCapacity = 10000.0;
        this.currentCargo = currentCargo;
        this.maintenanceNeeded = maintenanceNeeded;
    }

    @Override
    public void move(double distance) throws InvalidOperationException, InsufficientFuelException {
        if (distance < 0) {
            throw new InvalidOperationException("Distance cannot be negative.");
        }
        double fuelRequired = distance / calculateFuelEfficiency();
        if (fuelLevel < fuelRequired) {
            throw new InsufficientFuelException("Not enough fuel to cover " + distance + " km.");
        }
        System.out.println("Flying at " + getMaxAltitude() + " meters.");
        updateMileage(distance);
        consumeFuel(fuelRequired);
        if (getCurrentMileage() % 50000 >= 45000) {
            maintenanceNeeded = true;
        }
    }

    @Override
    public double calculateFuelEfficiency() {
        return 0.5; // km per liter
    }

    @Override
    public void refuel(double amount) throws InvalidOperationException {
        if (amount < 0) {
            throw new InvalidOperationException("Cannot refuel with a negative amount.");
        }
        this.fuelLevel += amount;
        System.out.println("Airplane with ID " + getId() + " refueled. New fuel level: " + this.fuelLevel);
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
            throw new OverloadException("Airplane passenger capacity exceeded.");
        }
        currentPassengers += count;
    }

    @Override
    public void disembarkPassengers(int count) throws InvalidOperationException {
        if (count > currentPassengers) {
            throw new InvalidOperationException("Disembarking more passengers than onboard.");
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
            System.out.println("Scheduling maintenance for Airplane...");
        }
    }

    @Override
    public void performMaintenance() {
        if (needsMaintenance()) {
            System.out.println("Performing maintenance on Airplane...");
            maintenanceNeeded = false;
        }
    }

    @Override
    public void loadCargo(double weight) throws OverloadException {
        if (currentCargo + weight > cargoCapacity) {
            throw new OverloadException("Airplane cargo overload detected.");
        }
        currentCargo += weight;
    }

    @Override
    public void unloadCargo(double weight) throws InvalidOperationException {
        if (currentCargo - weight < 0) {
            throw new InvalidOperationException("Cannot unload more cargo than currently loaded.");
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