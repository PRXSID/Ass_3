package fleetmanager;
import java.util.*;
import vehicles.*;
import concreteclasses.*;
import exceptions.*;
import interfaces.*;

public class FleetManager{
    private List<Vehicle> fleet=new ArrayList<>();   // stores all vehicles
    private TreeMap<String, List<Vehicle>> map = new TreeMap<>();   // maps model name to list of vehicles of that model
    private HashSet<String> id = new HashSet<>();    // tracks unique vehicle IDs

    public List<Vehicle> getFleet() {
        return fleet;
    }

    public TreeMap<String, List<Vehicle>> getmap(){
        return map;
    }

    public HashSet<String> getset(){
        return id;
    }
    
    // to add vehicle
    public void addVehicle(Vehicle v){
        try{
            id.add(v.getId());   //add vehicle ID to ID set
            Vehicle.checkUniqueId(fleet, v.getId()); // ensure ID is unique across fleet
            fleet.add(v);   // add vehicle to fleet list
            if(map.containsKey(v.getModel())){
            map.get(v.getModel()).add(v);   // add to existing model list
            }else{
                List<Vehicle> newModelList = new ArrayList<>(); 
                newModelList.add(v);
                map.put(v.getModel(), newModelList); // creating new model entry
            }
            System.out.println("Vehicle with ID " + v.getId() + " added to fleet");
            } catch (InvalidOperationException e) {
                System.out.println("Error: " + e.getMessage()); // will handle duplicate ID
        }
    }

    // to remove vehicle
    public void removeVehicle(String idd) throws InvalidOperationException{
        int i = 0;
        boolean found = false;
        for(Vehicle v : fleet){
            if(v.getId().equals(idd)){
                System.out.println("removed ID from fleet");
                found = true;
                break;
            }
            i++;
        }
        if(!found){
            throw new InvalidOperationException("No such ID exists in Fleet: " + idd); // ID not found
        }
        else{            
            Vehicle vehicleToRemove = fleet.get(i); 
            id.remove(vehicleToRemove.getId());          // Remove ID from set
            List<Vehicle> modelList = map.get(vehicleToRemove.getModel());
            if (modelList != null) {
                modelList.remove(vehicleToRemove);       // Remove from model map
                if (modelList.isEmpty()) {
                    map.remove(vehicleToRemove.getModel());
                }
            }
            fleet.remove(i);    // finally remove from fleet list
        } 
        return;
    }

    // display the vehicles by their model name
    public void displayByModelName() {
        if (map.isEmpty()) {
            System.out.println("No vehicles in the fleet to display.");
            return;
        }

        System.out.println("--- Fleet Report by Model ---");
        for (String modelName : map.keySet()) {            
            List<Vehicle> vehicles = map.get(modelName);             
            System.out.println("\n== Model: " + modelName + " (" + vehicles.size() + " vehicles) ==");            
            for (Vehicle v : vehicles) {
                System.out.println("  - ID: " + v.getId() + 
                                   ", Max Speed: " + v.getMaxSpeed() +
                                   ", Mileage: " + v.getCurrentMileage());   // display details
            }
        }
    }

    // start journey for all vehicles in fleet
    public void startAllJourneys(double distance) {
        for (Vehicle v : fleet) {
            try {
                v.move(distance);   // Move each vehicle
            } catch (InvalidOperationException e) {
                System.out.println("Invalid move for " + v.getId() + ": " + e.getMessage());
            } catch (InsufficientFuelException e) {
                System.out.println("Fuel issue for " + v.getId() + ": " + e.getMessage());
            }
        }
    }

    // this function calcuates how much fuel will be used to travel the distance based on fuel efficiency of my vehicle.
    public double getTotalFuelConsumption(double distance) {
        double fuelNeeded = 0.0;
        for (int i = 0; i < fleet.size(); i++) {
            Vehicle v = fleet.get(i);
            try {
                if (v instanceof Car) {
                    Car temp = (Car) v;
                    fuelNeeded += temp.consumeFuel(distance); 
                }
                else if(v instanceof Truck){
                    Truck temp = (Truck) v;
                    fuelNeeded += temp.consumeFuel(distance);
                }
                else if(v instanceof Bus){
                    Bus temp = (Bus) v;
                    fuelNeeded += temp.consumeFuel(distance);
                }
                else if(v instanceof Airplane){
                    Airplane temp = (Airplane) v;
                    fuelNeeded += temp.consumeFuel(distance);
                }
                else{
                    CargoShip temp = (CargoShip) v;
                    fuelNeeded += temp.consumeFuel(distance);
                }
                System.out.println(v.getId() +" consumed " + fuelNeeded +"for distance " + distance + " km.");  // Log fuel use
            }catch (InsufficientFuelException e){
                System.out.println(v.getId() + ": " + e.getMessage());
            }
        }

        return fuelNeeded;   // return total fuel consumed
    }

    // used to refuel all vehicles
    public void refuelAll(double amount) {
        for (Vehicle v : fleet) {
            if (v instanceof FuelConsumable) {
                try {
                    ((FuelConsumable) v).refuel(amount);   // Refuel each fuel-based vehicle
                } catch (InvalidOperationException e) {
                    System.out.println("Failed to refuel " + v.getId() + ": " + e.getMessage());
                }
            }
        }
    }

    public void maintainAll(){
        for(Vehicle v : fleet){
            if(v instanceof Maintainable){
                Maintainable m = (Maintainable) v;
                if (m.needsMaintenance()) {
                    System.out.println("Vehicle " + v.getId() + " needs maintenance.");
                    m.scheduleMaintenance();
                    m.performMaintenance();
                } else {
                    System.out.println("Vehicle " + v.getId() + " doesn't need maintenance.");
                }
            }
        }
        return;
    }
    // used to search for vehicles of a particular type
    public List<Vehicle> searchByType(Class<?> type){
        List<Vehicle> result = new ArrayList<>();
        for(Vehicle v : fleet){
            if(type.isInstance(v)){
                result.add(v);   // Add vehicles matching class type
            }
        }
        return result;
    }

    public void sortFleetByEfficiency(){
        Collections.sort(fleet, (v1, v2) -> Double.compare(v1.calculateFuelEfficiency(), v2.calculateFuelEfficiency()));
        System.out.println("Fleet sorted by fuel efficiency");
    }

    public void sortFleetBySpeed(){
        // Sort by max speed
        Collections.sort(fleet, Comparator.comparingDouble(Vehicle::getMaxSpeed));   
        System.out.println("Fleet sorted by max speed");
    }

    public void sortFleetByCurrentMileage() {
        // Sort by mileage
        Collections.sort(fleet, Comparator.comparingDouble(Vehicle::getCurrentMileage));  
        System.out.println("Fleet sorted by current mileage (ascending)");
    }

    public void sortFleetByModel() {
        // Sort alphabetically by model
        Collections.sort(fleet, Comparator.comparing(Vehicle::getModel));  
        System.out.println("Fleet sorted by current mileage (ascending)");
    }

    // will return max and min speed
    public Vehicle max(){
        double max = -1.0;
        Vehicle a = null;

        if (fleet.isEmpty()) {
            return null;   // Handle empty fleet
        }
 
        for(Vehicle v : fleet){
            if(max < v.getMaxSpeed()){
                max = v.getMaxSpeed();
                a = v;   // Track vehicle with highest speed
            } 
        }
        return a;
    }

    public Vehicle min(){
        double min = Double.MAX_VALUE;
        Vehicle a = null;

        if (fleet.isEmpty()) {
            return null;   // Handle empty fleet
        }

        for(Vehicle v : fleet){
            if(min > v.getMaxSpeed()){
                min = v.getMaxSpeed();
                a = v;   // Track vehicle with lowest speed
            }
        }
        return a;
    }

    // this finction gives a list of all vehicles that need maintenance
    public List<Vehicle> vehiclesNeedingMaintenance() {
        List<Vehicle> needyVehicles = new ArrayList<>();
        for (Vehicle v : fleet) {
            if (v instanceof Maintainable && ((Maintainable) v).needsMaintenance()) {
                needyVehicles.add(v);   // Add vehicles needing maintenance
            }
        }
        return needyVehicles;
    }

    // it wiil generate a report for all the vehicles
    public String generateReport(){
        int vehicleNum = fleet.size();
        double totalEfficiency = 0.0;
        double totalMileage = 0.0;
        int maintenanceCount = vehiclesNeedingMaintenance().size();

        List<Vehicle> car = searchByType(Car.class);
        List<Vehicle> bus = searchByType(Bus.class);
        List<Vehicle> truck = searchByType(Truck.class);
        List<Vehicle> airPlane = searchByType(Airplane.class);
        List<Vehicle> ship = searchByType(CargoShip.class);

        for(Vehicle v : fleet){
            totalEfficiency += v.calculateFuelEfficiency();
            totalMileage += v.getCurrentMileage();
        }

        double averageEfficiency;
        if (vehicleNum > 0) {
            averageEfficiency = totalEfficiency / vehicleNum;
        } else {
            averageEfficiency = 0.0;
        }
        StringBuilder report = new StringBuilder();
        report.append("Fleet Report\n");
        report.append("Total Vehicles: ").append(vehicleNum).append("\n");
        report.append("No of Cars: ").append(car.size()).append("\n");
        report.append("No of Buses: ").append(bus.size()).append("\n");
        report.append("No of Trucks: ").append(truck.size()).append("\n");
        report.append("No of Airplanes: ").append(airPlane.size()).append("\n");
        report.append("no of Ships: ").append(ship.size()).append("\n");
        report.append("Average Fuel Efficiency: ").append(String.format("%.2f", averageEfficiency)).append("\n");
        report.append("Total Mileage: ").append(String.format("%.2f", totalMileage)).append("\n");
        report.append("No of Vehicles Needing Maintenance: ").append(maintenanceCount).append("\n");
        report.append("Maximum Speed: ").append(max().getId(  )).append(": ").append(max().getMaxSpeed()).append("\n");
        report.append("Minimum Speed: ").append(min().getId()).append(": ").append(min().getMaxSpeed()).append("\n");

        return report.toString();   
    }
 
}
