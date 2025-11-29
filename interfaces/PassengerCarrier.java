package interfaces;
import exceptions.*;

public interface PassengerCarrier{
    public void boardPassengers(int count) throws OverloadException;
    public void disembarkPassengers(int count) throws InvalidOperationException;
    public int getPassengerCapacity();
    public int getCurrentPassengers();
}