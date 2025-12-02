# Fleet Management System with Highway Simulator

A Java-based fleet management system featuring a CLI interface for vehicle operations and a GUI-based highway simulator demonstrating multithreading and race conditions.

## Features

- **Fleet Management CLI**: Add, remove, track vehicles and manage fleet operations
- **Highway Simulator GUI**: Visual demonstration of multithreading and race conditions
- **Vehicle Types**: Car, Bus, Truck, CargoShip, Airplane
- **Race Condition Demo**: Toggle between synchronized and unsynchronized modes

## Quick Start

### Prerequisites
- Java JDK 8 or higher

### Compile

```bash
javac Main.java
```

This single command compiles all 25+ class files including the GUI components.

### Run

```bash
java Main
```

Select option 13 from the menu to launch the Highway Simulator GUI.

## CLI Menu Options

1. Add Vehicle
2. Remove Vehicle
3. Start Journey
4. Refuel All
5. Perform Maintenance
6. Generate Report
7. Save Fleet (CSV)
8. Load Fleet (CSV)
9. Search by Type
10. List Vehicles Needing Maintenance
11. Sort Vehicles
12. Display by Model Name
13. **Launch Highway Simulator GUI**
14. Exit

## Highway Simulator GUI

The GUI demonstrates multithreading concepts with three vehicles running simultaneously:

**Controls:**
- Start, Pause All, Resume All, Stop, Reset buttons
- Enable Synchronization checkbox
- Individual vehicle refuel buttons

**Features:**
- Real-time vehicle status tracking
- Shared highway distance counter
- Race condition visualization
- Discrepancy detection between expected and actual values

### Testing Race Conditions

**Without Synchronization:**
1. Uncheck "Enable Synchronization"
2. Click "Start"
3. Observe discrepancy between expected and actual distance (data loss occurs)

**With Synchronization:**
1. Check "Enable Synchronization"
2. Click "Start"
3. Observe zero discrepancy (no data loss)

## Project Structure

```
Ass_3/
├── Main.java                    # Entry point with CLI and GUI launcher
├── vehicles/                    # Base vehicle classes
├── concreteclasses/             # Car, Bus, Truck, Airplane, CargoShip
├── interfaces/                  # FuelConsumable, CargoCarrier, etc.
├── exceptions/                  # Custom exceptions
├── fleetmanager/                # Fleet management logic
└── simulator/                   # GUI and multithreading components
    ├── HighwaySimulatorGUI.java
    ├── SimulationVehicle.java
    └── HighwayDistanceCounter.java
```

## Technical Highlights

- **Multithreading**: Each vehicle runs in its own thread
- **Synchronization**: Uses ReentrantLock to prevent race conditions
- **Thread-Safe GUI**: All UI updates via SwingUtilities.invokeLater()
- **CSV Support**: Save and load fleet data
- **Exception Handling**: Custom exceptions for operations

## Vehicle Properties

Each simulation vehicle tracks:
- Mileage (increments 1 km/second)
- Fuel level (decreases 0.5 units/km)
- Status: RUNNING, PAUSED, OUT_OF_FUEL, STOPPED

## Notes

- GUI launches in a separate window while CLI remains responsive
- Both interfaces can operate simultaneously
- Race condition is intentionally created with Thread.sleep() for demonstration
- Synchronization eliminates the race condition completely
