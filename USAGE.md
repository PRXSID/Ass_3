# How to Run the Integrated Fleet Management System

## Compilation

To compile the entire system (including the GUI), simply run:

```bash
javac Main.java
```

This single command will compile:
- The main Fleet Management CLI
- All vehicle classes (Car, Bus, Truck, Airplane, CargoShip)
- The FleetManager
- The Highway Simulator GUI components
- All supporting classes and interfaces

## Running the System

To start the Fleet Management System:

```bash
java Main
```

## Using the Highway Simulator GUI

Once the CLI is running, you can launch the Highway Simulator GUI by:

1. Selecting option **13** from the main menu
2. The GUI will launch in a separate window
3. The CLI will remain active and responsive
4. You can continue using the CLI while the GUI is open

## Menu Options

The integrated system now includes:

1. Add Vehicle
2. Remove Vehicle
3. Start Journey
4. Refuel All
5. Perform Maintenance
6. Generate Report
7. Save Fleet
8. Load Fleet
9. Search by Type
10. List Vehicles Needing Maintenance
11. Sort Vehicles
12. Display by Model Name
13. **Launch Highway Simulator GUI** ← NEW!
14. Exit

## Features of Highway Simulator GUI

The Highway Simulator GUI provides:
- Multi-threaded vehicle simulation
- Real-time vehicle status monitoring
- Race condition demonstration and fix
- Visual controls for simulation (Start, Pause, Resume, Stop, Reset)
- Synchronized vs unsynchronized mode toggle
- Highway distance counter with race condition analysis

## Notes

- The GUI runs in a separate window and can be closed independently
- You can launch multiple GUI instances if needed (not recommended)
- The CLI remains responsive while the GUI is running
- Both the CLI and GUI can be used simultaneously
