import java.util.*;
import java.io.*;
import vehicles.*;
import concreteclasses.*;
import fleetmanager.*;
import interfaces.*;
import exceptions.*;
import simulator.*;
import javax.swing.*;

public class Main {

    // input function that checks validity of input (int in this case)
    public static int readInt(Scanner sc) {
        while (!sc.hasNextInt()) {
            System.out.println("Invalid input! Please enter an integer.");
            sc.next();
        }
        int val = sc.nextInt();
        sc.nextLine();
        return val;
    }

    // input function that checks validity of input (double in this case)
    public static double readDouble(Scanner sc) {
        while (!sc.hasNextDouble()) {
            System.out.println("Invalid input! Please enter a number.");
            sc.next();
        }
        double val = sc.nextDouble();
        sc.nextLine();
        return val;
    }

    // input function that checks validity of input (boolean in this case)
    public static boolean readBoolean(Scanner sc) {
        while (!sc.hasNextBoolean()) {
            System.out.println("Invalid input! Please enter true or false.");
            sc.next();
        }
        boolean val = sc.nextBoolean();
        sc.nextLine();
        return val;
    }

    // used to read trimmed string from user
    public static String readString(Scanner sc) {
        return sc.nextLine().trim();
    }

    // main function 
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        FleetManager fleetManager = new FleetManager();

        // CLI
        while (true) {
            System.out.println("\n=== Fleet Management System ===");
            System.out.println("1. Add Vehicle");
            System.out.println("2. Remove Vehicle");
            System.out.println("3. Start Journey");
            System.out.println("4. Refuel All");
            System.out.println("5. Perform Maintenance");
            System.out.println("6. Generate Report");
            System.out.println("7. Save Fleet");
            System.out.println("8. Load Fleet");
            System.out.println("9. Search by Type");
            System.out.println("10. List Vehicles Needing Maintenance");
            System.out.println("11. Sort Vehicles");
            System.out.println("12. Display by Model Name");
            System.out.println("13. Launch Highway Simulator GUI");
            System.out.println("14. Exit");
            System.out.print("Enter choice: ");

            int choice = readInt(sc);

            // Exit option
            if (choice == 14) {
                System.out.println("Exiting System.");
                break;
            }

            // ADD VEHICLE
            else if (choice == 1) {
                System.out.print("Enter vehicle type (Car, Bus, Truck, CargoShip, Airplane): ");
                String vehicleString = readString(sc);

                try {
                    //Add Car
                    if (vehicleString.equalsIgnoreCase("Car")) {
                        System.out.print("Enter Vehicle ID: ");
                        String id = readString(sc);
                        System.out.print("Enter Vehicle Model: ");
                        String model = readString(sc);
                        System.out.print("Enter Max Speed: ");
                        double maxSpeed = readDouble(sc);
                        System.out.print("Enter Current Mileage: ");
                        double currentMileage = readDouble(sc);
                        System.out.print("Enter Fuel Level: ");
                        double fuelLevel = readDouble(sc);
                        System.out.print("Enter Passenger Capacity: ");
                        int passengerCapacity = readInt(sc);
                        System.out.print("Enter Current Passengers: ");
                        int currentPassengers = readInt(sc);
                        System.out.print("Maintenance Needed? (true/false): ");
                        boolean maintenanceNeeded = readBoolean(sc);
                        System.out.print("Enter Number of Wheels: ");
                        int numWheels = readInt(sc);

                        Vehicle car = new Car(id, model, maxSpeed, currentMileage, numWheels,
                                fuelLevel, passengerCapacity, currentPassengers, maintenanceNeeded);
                        fleetManager.addVehicle(car);
                    }

                    // Add Truck
                    else if (vehicleString.equalsIgnoreCase("Truck")) {
                        System.out.print("Enter ID: ");
                        String id = readString(sc);
                        System.out.print("Enter Model: ");
                        String model = readString(sc);
                        System.out.print("Enter Max Speed: ");
                        double maxSpeed = readDouble(sc);
                        System.out.print("Enter Current Mileage: ");
                        double currentMileage = readDouble(sc);
                        System.out.print("Enter Number of Wheels: ");
                        int numWheels = readInt(sc);
                        System.out.print("Enter Fuel Level: ");
                        double fuelLevel = readDouble(sc);
                        System.out.print("Enter Cargo Capacity: ");
                        double cargoCapacity = readDouble(sc);
                        System.out.print("Enter Current Cargo: ");
                        double currentCargo = readDouble(sc);
                        System.out.print("Maintenance Needed? (true/false): ");
                        boolean maintenanceNeeded = readBoolean(sc);

                        Vehicle truck = new Truck(id, model, maxSpeed, currentMileage, numWheels,
                                fuelLevel, cargoCapacity, currentCargo, maintenanceNeeded);
                        fleetManager.addVehicle(truck);
                    }

                    // Add Cargo Ship
                    else if (vehicleString.equalsIgnoreCase("CargoShip")) {
                        System.out.print("Enter ID: ");
                        String id = readString(sc);
                        System.out.print("Enter Model: ");
                        String model = readString(sc);
                        System.out.print("Enter Max Speed: ");
                        double maxSpeed = readDouble(sc);
                        System.out.print("Enter Current Mileage: ");
                        double currentMileage = readDouble(sc);
                        System.out.print("Has Sail? (true/false): ");
                        boolean hasSail = readBoolean(sc);
                        System.out.print("Enter Fuel Level: ");
                        double fuelLevel = readDouble(sc);
                        System.out.print("Enter Current Cargo: ");
                        double currentCargo = readDouble(sc);
                        System.out.print("Maintenance Needed? (true/false): ");
                        boolean maintenanceNeeded = readBoolean(sc);

                        Vehicle ship = new CargoShip(id, model, maxSpeed, currentMileage,
                                hasSail, fuelLevel, currentCargo, maintenanceNeeded);
                        fleetManager.addVehicle(ship);
                    }

                    // Add Bus
                    else if (vehicleString.equalsIgnoreCase("Bus")) {
                        System.out.print("Enter ID: ");
                        String id = readString(sc);
                        System.out.print("Enter Model: ");
                        String model = readString(sc);
                        System.out.print("Enter Max Speed: ");
                        double maxSpeed = readDouble(sc);
                        System.out.print("Enter Current Mileage: ");
                        double currentMileage = readDouble(sc);
                        System.out.print("Enter Number of Wheels: ");
                        int numWheels = readInt(sc);
                        System.out.print("Enter Fuel Level: ");
                        double fuelLevel = readDouble(sc);
                        System.out.print("Enter Passenger Capacity: ");
                        int passengerCapacity = readInt(sc);
                        System.out.print("Enter Current Passengers: ");
                        int currentPassengers = readInt(sc);
                        System.out.print("Enter Cargo Capacity: ");
                        double cargoCapacity = readDouble(sc);
                        System.out.print("Enter Current Cargo: ");
                        double currentCargo = readDouble(sc);
                        System.out.print("Maintenance Needed? (true/false): ");
                        boolean maintenanceNeeded = readBoolean(sc);

                        Vehicle bus = new Bus(id, model, maxSpeed, currentMileage, numWheels,
                                fuelLevel, passengerCapacity, currentPassengers,
                                cargoCapacity, currentCargo, maintenanceNeeded);
                        fleetManager.addVehicle(bus);
                    }

                    // Add Airplane
                    else if (vehicleString.equalsIgnoreCase("Airplane")) {
                        System.out.print("Enter ID: ");
                        String id = readString(sc);
                        System.out.print("Enter Model: ");
                        String model = readString(sc);
                        System.out.print("Enter Max Speed: ");
                        double maxSpeed = readDouble(sc);
                        System.out.print("Enter Current Mileage: ");
                        double currentMileage = readDouble(sc);
                        System.out.print("Enter Fuel Level: ");
                        double fuelLevel = readDouble(sc);
                        System.out.print("Enter Max Altitude: ");
                        double maxAltitude = readDouble(sc);
                        System.out.print("Enter Current Passengers: ");
                        int currentPassengers = readInt(sc);
                        System.out.print("Enter Current Cargo: ");
                        double currentCargo = readDouble(sc);
                        System.out.print("Maintenance Needed? (true/false): ");
                        boolean maintenanceNeeded = readBoolean(sc);

                        Vehicle airplane = new Airplane(id, model, maxSpeed, currentMileage,
                                fuelLevel, maxAltitude, currentPassengers, currentCargo, maintenanceNeeded);
                        fleetManager.addVehicle(airplane);
                    }

                    // Invalid vehicle type
                    else {
                        System.out.println("Enter valid vehicle type!");
                    }
                } catch (InvalidOperationException e) {
                    System.out.println("Error creating vehicle: " + e.getMessage());
                }
            }

            // Remove Vehicle
            else if (choice == 2) {
                System.out.println("Enter vehicle Id");
                String id = readString(sc);
                try {
                    fleetManager.removeVehicle(id);
                } catch (InvalidOperationException e) {
                    System.out.println("Error: " + e.getMessage());
                }
            }

            // START JOURNEY
            else if (choice == 3) {
                System.out.print("Enter journey distance: ");
                double dist = readDouble(sc);
                fleetManager.startAllJourneys(dist);
            }

            // REFUEL ALL 
            else if (choice == 4) {
                System.out.print("Enter fuel amount to add: ");
                double fuel = readDouble(sc);
                fleetManager.refuelAll(fuel);
                System.out.println("Refuel operation completed.");
            }

            // PERFORM MAINTENANCE 
            else if (choice == 5) {
                fleetManager.maintainAll();
            }

            // GENERATE REPORT
            else if (choice == 6) {
                System.out.println(fleetManager.generateReport());
            }

            // SAVE FLEET 
            else if (choice == 7) {
                System.out.print("Enter filename to save (e.g., fleet.csv): ");
                String filename = readString(sc);

                if (filename.isEmpty()) {
                    System.out.println("Invalid filename. Save cancelled.");
                } else {
                    CSVManager save = new CSVManager();
                    save.savetoCSV(filename, fleetManager.getFleet());
                }
            }

            // LOAD FLEET 
            else if (choice == 8) {
                System.out.print("Enter filename to load (e.g., fleet.csv): ");
                String filename = readString(sc);

                if (filename.isEmpty()) {
                    System.out.println("Invalid filename. Load cancelled.");
                } else {
                    try {
                        CSVManager load = new CSVManager();
                        load.loadCSV(filename, fleetManager.getFleet(),
                                fleetManager.getmap(), fleetManager.getset());
                        System.out.println("Fleet loaded successfully from " + filename);
                    } catch (Exception e) {
                        System.out.println("Error loading fleet: " + e.getMessage());
                    }
                }
            }

            // SEARCH BY TYPE
            else if (choice == 9) {
                System.out.print("Enter vehicle type (Car/Bus/Truck/Airplane/CargoShip): ");
                String typeInput = readString(sc);
                Class<?> type = null;

                if (typeInput.equalsIgnoreCase("Car")) type = Car.class;
                else if (typeInput.equalsIgnoreCase("Bus")) type = Bus.class;
                else if (typeInput.equalsIgnoreCase("Truck")) type = Truck.class;
                else if (typeInput.equalsIgnoreCase("Airplane")) type = Airplane.class;
                else if (typeInput.equalsIgnoreCase("CargoShip")) type = CargoShip.class;

                if (type != null) {
                    List<Vehicle> results = fleetManager.searchByType(type);
                    for (Vehicle v : results) v.displayInfo();
                } else {
                    System.out.println("Invalid vehicle type entered!");
                }
            }

            // VEHICLES NEEDING MAINTENANCE 
            else if (choice == 10) {
                List<Vehicle> results = fleetManager.vehiclesNeedingMaintenance();
                for (Vehicle v : results) v.displayInfo();
            }

            // SORT VEHICLES 
            else if (choice == 11) {
                while (true) {
                    System.out.println("Sort the Vehicles fleet by:");
                    System.out.println("1. By Fuel Efficiency");
                    System.out.println("2. By Max Speed");
                    System.out.println("3. By Current Mileage");
                    System.out.println("4. By Model");

                    int ch = readInt(sc);

                    if (ch == 1) {
                        fleetManager.sortFleetByEfficiency();
                        System.out.println("Sorted vehicles by efficiency");
                        for (Vehicle v : fleetManager.getFleet()) v.displayInfo();
                        break;
                    } else if (ch == 2) {
                        fleetManager.sortFleetBySpeed();
                        System.out.println("Sorted vehicles by speed");
                        for (Vehicle v : fleetManager.getFleet()) v.displayInfo();
                        break;
                    } else if (ch == 3) {
                        fleetManager.sortFleetByCurrentMileage();
                        System.out.println("Sorted vehicles by mileage");
                        for (Vehicle v : fleetManager.getFleet()) v.displayInfo();
                        break;
                    } else if (ch == 4) {
                        fleetManager.sortFleetByModel();
                        System.out.println("Sorted Vehicles by Model");
                        for (Vehicle v : fleetManager.getFleet()) v.displayInfo();
                        break;
                    } else {
                        System.out.println("Invalid input");
                    }
                }
            }

            // DISPLAY BY MODEL
            else if (choice == 12) {
                fleetManager.displayByModelName();
            }

            // LAUNCH HIGHWAY SIMULATOR GUI
            else if (choice == 13) {
                System.out.println("Launching Highway Simulator GUI...");
                SwingUtilities.invokeLater(() -> {
                    try {
                        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                    } catch (Exception e) {
                        // Use default look and feel
                    }
                    
                    HighwaySimulatorGUI gui = new HighwaySimulatorGUI();
                    gui.setVisible(true);
                });
                System.out.println("Highway Simulator GUI launched in a separate window.");
            }

            // INVALID CHOICE
            else {
                System.out.println("Invalid choice! Try again.");
            }
        }

        sc.close();
    }
}

// CSVManager class handles saving and loading fleet data in CSV files.
class CSVManager {

    // this function saves fleet data into a CSV file
    public void savetoCSV(String fileName, List<Vehicle> fleet) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName, true))) {
            for (Vehicle v : fleet) {
                StringBuilder line = new StringBuilder();
                line.append("Class:").append(v.getClass().getSimpleName()).append(",");
                line.append("ID:").append(v.getId()).append(",");
                line.append("Model:").append(v.getModel()).append(",");
                line.append("MaxSpeed:").append(v.getMaxSpeed()).append(",");
                line.append("Mileage:").append(v.getCurrentMileage()).append(",");

                if (v instanceof Car) {
                    Car c = (Car) v;
                    line.append("FuelLevel:").append(c.getFuelLevel()).append(",");
                    line.append("PassengerCapacity:").append(c.getPassengerCapacity()).append(",");
                    line.append("CurrentPassengers:").append(c.getCurrentPassengers()).append(",");
                    line.append("MaintenanceNeeded:").append(c.needsMaintenance()).append(",");
                    line.append("NumWheels:").append(c.getNumWheels());
                } else if (v instanceof Truck) {
                    Truck t = (Truck) v;
                    line.append("NumWheels:").append(t.getNumWheels()).append(",");
                    line.append("FuelLevel:").append(t.getFuelLevel()).append(",");
                    line.append("CargoCapacity:").append(t.getCargoCapacity()).append(",");
                    line.append("CurrentCargo:").append(t.getCurrentCargo()).append(",");
                    line.append("MaintenanceNeeded:").append(t.needsMaintenance());
                } else if (v instanceof Bus) {
                    Bus b = (Bus) v;
                    line.append("NumWheels:").append(b.getNumWheels()).append(",");
                    line.append("FuelLevel:").append(b.getFuelLevel()).append(",");
                    line.append("PassengerCapacity:").append(b.getPassengerCapacity()).append(",");
                    line.append("CurrentPassengers:").append(b.getCurrentPassengers()).append(",");
                    line.append("CargoCapacity:").append(b.getCargoCapacity()).append(",");
                    line.append("CurrentCargo:").append(b.getCurrentCargo()).append(",");
                    line.append("MaintenanceNeeded:").append(b.needsMaintenance());
                } else if (v instanceof CargoShip) {
                    CargoShip s = (CargoShip) v;
                    line.append("HasSail:").append(s.getSail()).append(",");
                    line.append("FuelLevel:").append(s.getFuelLevel()).append(",");
                    line.append("CurrentCargo:").append(s.getCurrentCargo()).append(",");
                    line.append("MaintenanceNeeded:").append(s.needsMaintenance());
                } else if (v instanceof Airplane) {
                    Airplane p = (Airplane) v;
                    line.append("MaxAltitude:").append(p.getMaxAltitude()).append(",");
                    line.append("FuelLevel:").append(p.getFuelLevel()).append(",");
                    line.append("PassengerCapacity:").append(p.getPassengerCapacity()).append(",");
                    line.append("CurrentPassengers:").append(p.getCurrentPassengers()).append(",");
                    line.append("CurrentCargo:").append(p.getCurrentCargo()).append(",");
                    line.append("MaintenanceNeeded:").append(p.needsMaintenance());
                }

                writer.println(line);
            }
        } catch (IOException e) {
            System.out.println("Error saving fleet to file: " + e.getMessage());
        }
        System.out.println(fileName + " has been updated");
    }

    // this function loads fleet data from CSV file 
    public void loadCSV(String fileName, List<Vehicle> fleet, TreeMap<String, List<Vehicle>> map, HashSet<String> idd) {
        try (Scanner sc = new Scanner(new File(fileName))) {
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                String[] elements = line.split(",");
                String id = "", model = "", className = "";
                double maxSpeed = 0, mileage = 0, fuelLevel = 0, maxAltitude = 0, cargoCapacity = 0, currentCargo = 0;
                int passengerCapacity = 0, currentPassengers = 0, numWheels = 0;
                boolean maintenanceNeeded = false, hasSail = false;

                for (String i : elements) {
                    String[] parts = i.split(":");
                    String val = parts[1].trim();
                    String type = parts[0].trim();

                    if (type.equals("Class")) {
                        className = val;
                    } else if (type.equals("ID")) {
                        id = val;
                    } else if (type.equals("Model")) {
                        model = val;
                    } else if (type.equals("MaxSpeed")) {
                        maxSpeed = Double.parseDouble(val);
                    } else if (type.equals("Mileage")) {
                        mileage = Double.parseDouble(val);
                    } else if (type.equals("FuelLevel")) {
                        fuelLevel = Double.parseDouble(val);
                    } else if (type.equals("MaxAltitude")) {
                        maxAltitude = Double.parseDouble(val);
                    } else if (type.equals("PassengerCapacity")) {
                        passengerCapacity = Integer.parseInt(val);
                    } else if (type.equals("CurrentPassengers")) {
                        currentPassengers = Integer.parseInt(val);
                    } else if (type.equals("NumWheels")) {
                        numWheels = Integer.parseInt(val);
                    } else if (type.equals("CargoCapacity")) {
                        cargoCapacity = Double.parseDouble(val);
                    } else if (type.equals("CurrentCargo")) {
                        currentCargo = Double.parseDouble(val);
                    } else if (type.equals("MaintenanceNeeded")) {
                        maintenanceNeeded = Boolean.parseBoolean(val);
                    } else if (type.equals("HasSail")) {
                        hasSail = Boolean.parseBoolean(val);
                    }
                }

                Vehicle v = null;
                if (className.equals("Car")) {
                    v = new Car(id, model, maxSpeed, mileage, numWheels, fuelLevel, passengerCapacity, currentPassengers, maintenanceNeeded);
                } else if (className.equals("Bus")) {
                    v = new Bus(id, model, maxSpeed, mileage, numWheels, fuelLevel,passengerCapacity, currentPassengers, cargoCapacity, currentCargo,maintenanceNeeded);
                } else if (className.equals("Truck")) {
                    v = new Truck(id, model, maxSpeed, mileage, numWheels, fuelLevel,
                            cargoCapacity, currentCargo, maintenanceNeeded);
                } else if (className.equals("Airplane")) {
                    v = new Airplane(id, model, maxSpeed, mileage, fuelLevel, maxAltitude,
                            currentPassengers, currentCargo, maintenanceNeeded);
                } else if (className.equals("CargoShip")) {
                    v = new CargoShip(id, model, maxSpeed, mileage, hasSail,
                            fuelLevel, currentCargo, maintenanceNeeded);
                }

                if (v != null) {
                    try{
                    idd.add(v.getId());
                    Vehicle.checkUniqueId(fleet, v.getId());
                    fleet.add(v);
                    if(map.containsKey(v.getModel())){
                    map.get(v.getModel()).add(v);
                    }else{
                        List<Vehicle> newModelList = new ArrayList<>(); 
                        newModelList.add(v);
                        map.put(v.getModel(), newModelList);
                    }
                    System.out.println("Vehicle with ID " + v.getId() + " added to fleet");
                    } catch (InvalidOperationException e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                }
                }
            }catch (FileNotFoundException | InvalidOperationException e) {
            System.out.println("Error loading fleet from file: " + e.getMessage());
        }
    }
}

