package interfaces;
import exceptions.*;

public interface FuelConsumable {
    public void refuel(double amount) throws InvalidOperationException;
    public double getFuelLevel();
    public double consumeFuel(double amount) throws InsufficientFuelException;
}