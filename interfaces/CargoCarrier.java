package interfaces;
import exceptions.*;

public interface CargoCarrier{
    public void loadCargo(double weight) throws OverloadException;
    public void unloadCargo(double weight) throws InvalidOperationException;
    public double getCargoCapacity();
    public double getCurrentCargo();
}