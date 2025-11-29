# Fleet Highway Simulator

## Assignment 3 - Multithreading and GUI with Race Condition Handling

### Overview

This project implements a Fleet Highway Simulator in Java that demonstrates multithreaded execution through a simulated highway scenario involving multiple vehicles. The simulator provides a graphical user interface (GUI) built using Swing for controlling and observing the simulation.

The application illustrates a race condition arising from unsynchronized access to a shared resource (Highway Distance Counter) and resolves it using appropriate synchronization techniques (ReentrantLock).

---

## Table of Contents

1. [Compilation and Running](#compilation-and-running)
2. [Design Overview](#design-overview)
3. [GUI Layout](#gui-layout)
4. [Thread Control via GUI](#thread-control-via-gui)
5. [Race Condition Demonstration and Fix](#race-condition-demonstration-and-fix)
6. [GUI Thread-Safety Considerations](#gui-thread-safety-considerations)

---

## Compilation and Running

### Prerequisites
- Java JDK 8 or higher
- Terminal/Command Prompt

### Compile the Application

```bash
# Navigate to the project directory
cd /path/to/Ass_3

# Compile all source files
javac -d out simulator/*.java

# Alternatively, compile everything including the base vehicle classes
javac -d out Main.java vehicles/*.java concreteclasses/*.java interfaces/*.java exceptions/*.java fleetmanager/*.java simulator/*.java
```

### Run the Simulator

```bash
# Run the Highway Simulator GUI
java -cp out simulator.HighwaySimulatorGUI
```

### Testing the Race Condition

1. **Without Synchronization (Default):**
   - Launch the application
   - Ensure "Enable Synchronization" checkbox is **unchecked**
   - Click "Start" to begin the simulation
   - Observe the discrepancy between "Expected" and actual Highway Distance Counter
   - The race condition will cause data loss (actual < expected)

2. **With Synchronization:**
   - Launch the application
   - Check the "Enable Synchronization" checkbox
   - Click "Start" to begin the simulation
   - The discrepancy should remain 0 (no data loss)

---

## Design Overview

### Architecture

```
┌─────────────────────────────────────────────────────────────────┐
│                    HighwaySimulatorGUI                          │
│                (Swing-based User Interface)                     │
│         Controls: Start, Pause, Resume, Stop, Reset             │
└─────────────────────────┬───────────────────────────────────────┘
                          │
          ┌───────────────┼───────────────┐
          │               │               │
          ▼               ▼               ▼
    ┌──────────┐    ┌──────────┐    ┌──────────┐
    │ Vehicle  │    │ Vehicle  │    │ Vehicle  │
    │ Thread 1 │    │ Thread 2 │    │ Thread 3 │
    └────┬─────┘    └────┬─────┘    └────┬─────┘
         │               │               │
         └───────────────┼───────────────┘
                         │
                         ▼
            ┌─────────────────────────┐
            │  HighwayDistanceCounter │
            │    (Shared Resource)    │
            └─────────────────────────┘
```

### Key Classes

1. **HighwaySimulatorGUI** (`simulator/HighwaySimulatorGUI.java`)
   - Main entry point for the application
   - Swing-based GUI providing all user controls
   - Implements `VehicleUpdateCallback` for vehicle state updates
   - Uses `SwingUtilities.invokeLater()` for thread-safe UI updates

2. **SimulationVehicle** (`simulator/SimulationVehicle.java`)
   - Extends `Thread` class for multithreaded execution
   - Maintains vehicle properties: ID, mileage, fuel level, status
   - Runs in its own thread, updating state every second
   - Updates the shared Highway Distance Counter

3. **HighwayDistanceCounter** (`simulator/HighwayDistanceCounter.java`)
   - Shared static counter for total highway distance
   - Provides both unsynchronized and synchronized increment methods
   - Uses `ReentrantLock` for synchronized access
   - Demonstrates race condition when unsynchronized

### Vehicle Properties

Each vehicle maintains:
- **Unique Identifier**: Vehicle-1, Vehicle-2, Vehicle-3
- **Mileage Traveled**: Incremented by 1 km per second
- **Fuel Remaining**: Decreases by 0.5 units per km
- **Operational Status**: RUNNING, PAUSED, OUT_OF_FUEL, STOPPED

---

## GUI Layout

```
┌─────────────────────────────────────────────────────────────────────────┐
│                        Simulation Controls                               │
│  [Start] [Pause All] [Resume All] [Stop] [Reset]  ☐ Enable Sync         │
├─────────────────────────────────────────────────────────────────────────┤
│                                                   │ Highway Statistics   │
│  ┌────────────┐ ┌────────────┐ ┌────────────┐    │                      │
│  │ Vehicle-1  │ │ Vehicle-2  │ │ Vehicle-3  │    │  Highway Distance    │
│  │            │ │            │ │            │    │     Counter          │
│  │ Mileage:   │ │ Mileage:   │ │ Mileage:   │    │      85 km           │
│  │ 28 km      │ │ 29 km      │ │ 28 km      │    │                      │
│  │            │ │            │ │            │    │  Race Condition      │
│  │ Fuel:      │ │ Fuel:      │ │ Fuel:      │    │  Analysis            │
│  │ 6.0/20.0   │ │ 5.5/20.0   │ │ 6.0/20.0   │    │                      │
│  │            │ │            │ │            │    │  Expected: 85 km     │
│  │ Status:    │ │ Status:    │ │ Status:    │    │  Discrepancy: 0 km   │
│  │ RUNNING    │ │ RUNNING    │ │ RUNNING    │    │                      │
│  │            │ │            │ │            │    │                      │
│  │ [Refuel]   │ │ [Refuel]   │ │ [Refuel]   │    │                      │
│  └────────────┘ └────────────┘ └────────────┘    │                      │
├─────────────────────────────────────────────────────────────────────────┤
│                          Simulation Log                                  │
│ [12:34:56] Starting simulation with 3 vehicles...                       │
│ [12:34:56] Synchronization mode: DISABLED                               │
│ [12:34:56] Simulation started!                                          │
└─────────────────────────────────────────────────────────────────────────┘
```

---

## Thread Control via GUI

### Control Buttons

| Button | Description |
|--------|-------------|
| **Start** | Starts all vehicle threads and begins the simulation |
| **Pause All** | Pauses all running vehicles (threads continue but don't update state) |
| **Resume All** | Resumes all paused vehicles that have fuel |
| **Stop** | Stops all threads permanently and displays final results |
| **Reset** | Resets the entire simulation to initial state |

### Synchronization Toggle

- **Enable Synchronization Checkbox**: Toggles between synchronized and unsynchronized access to the shared counter
- Must be set **before** starting the simulation
- When enabled, uses `ReentrantLock` for thread-safe updates

### Vehicle Refueling

- Each vehicle has a **Refuel** button
- When clicked, refills the vehicle's fuel to maximum capacity
- If the vehicle was OUT_OF_FUEL, it automatically resumes running

---

## Race Condition Demonstration and Fix

### What is the Race Condition?

A race condition occurs when multiple threads access and modify shared data concurrently without proper synchronization. In our simulator:

```java
// Unsynchronized increment - RACE CONDITION
public static void incrementUnsynchronized(int km) {
    int current = highwayDistance;  // Thread A reads: 10
    // Context switch to Thread B which also reads 10
    Thread.sleep(1);                // Simulates processing delay
    highwayDistance = current + km; // Thread A writes: 11
    // Thread B also writes: 11 (should be 12!)
}
```

### Step 1: Implementing Unsynchronized Access

The `HighwayDistanceCounter` class has:
```java
public static int highwayDistance = 0;  // Shared counter

public static void incrementUnsynchronized(int km) {
    int current = highwayDistance;
    try { Thread.sleep(1); } catch (InterruptedException e) {}
    highwayDistance = current + km;  // Race condition!
}
```

### Step 2: Recording Incorrect Behavior

When running without synchronization:
- **Expected Total**: Sum of all vehicle mileages (e.g., 85 km)
- **Actual Counter**: Lower value due to lost updates (e.g., 72 km)
- **Discrepancy**: Shows the data loss (e.g., 13 km)

The GUI displays this discrepancy in real-time with red highlighting.

### Step 3: Applying Synchronization

Using `ReentrantLock` for synchronized access:

```java
private static final ReentrantLock lock = new ReentrantLock();

public static void incrementWithLock(int km) {
    lock.lock();
    try {
        synchronizedHighwayDistance += km;
    } finally {
        lock.unlock();
    }
}
```

Alternative using `synchronized` keyword:
```java
public static synchronized void incrementSynchronized(int km) {
    synchronizedHighwayDistance += km;
}
```

### Step 4: Validating Correct Behavior

When running with synchronization enabled:
- **Expected Total** = **Actual Counter**
- **Discrepancy**: 0 km
- No data loss occurs

---

## GUI Thread-Safety Considerations

### Event Dispatch Thread (EDT)

In Swing, all UI updates must occur on the Event Dispatch Thread. Failure to do so can cause:
- Visual glitches
- Race conditions in UI state
- Potential deadlocks

### Our Implementation

1. **Application Launch**:
   ```java
   SwingUtilities.invokeLater(() -> {
       HighwaySimulatorGUI gui = new HighwaySimulatorGUI();
       gui.setVisible(true);
   });
   ```

2. **UI Updates from Vehicle Threads**:
   ```java
   private void updateUI() {
       SwingUtilities.invokeLater(() -> {
           // All UI updates happen here on EDT
           highwayCounterLabel.setText(actual + " km");
           vehicleMileageLabels[i].setText("Mileage: " + mileage + " km");
       });
   }
   ```

3. **Timer-Based Updates**:
   - A Swing `Timer` (runs on EDT) periodically polls vehicle states
   - Avoids flooding EDT with updates from multiple threads
   - Provides smooth UI refresh at 100ms intervals

4. **Volatile Variables**:
   - Vehicle status flags (`running`, `paused`) are marked `volatile`
   - Ensures visibility across threads without full synchronization

### Best Practices Used

- ✅ GUI created on EDT using `SwingUtilities.invokeLater()`
- ✅ All UI updates wrapped in `SwingUtilities.invokeLater()`
- ✅ Timer-based polling instead of direct thread callbacks
- ✅ Volatile flags for thread communication
- ✅ Proper thread interruption handling

---

## Screenshots

Screenshots demonstrating the race condition and its fix should be placed in the `screenshots/` directory:

1. `race_condition_before.png` - Showing discrepancy without synchronization
2. `race_condition_after.png` - Showing no discrepancy with synchronization
3. `vehicle_out_of_fuel.png` - Showing OUT_OF_FUEL status and refueling
4. `final_results.png` - Showing final simulation results

---

## Integration with Previous Assignments

This assignment builds upon the vehicle model from Assignments 1 and 2:

- **Package Structure**: Maintains the existing package organization
  - `vehicles/` - Base vehicle classes (Vehicle, LandVehicle, etc.)
  - `concreteclasses/` - Concrete implementations (Car, Bus, Truck, etc.)
  - `interfaces/` - Vehicle interfaces (FuelConsumable, Maintainable, etc.)
  - `exceptions/` - Custom exceptions
  - `fleetmanager/` - Fleet management functionality
  - `simulator/` - **NEW** Highway simulator with multithreading and GUI

- **Simplified Model**: The `SimulationVehicle` class provides a simplified vehicle model specifically designed for the highway simulation, focusing on:
  - Multithreaded execution
  - Mileage and fuel tracking
  - Status management (RUNNING, PAUSED, OUT_OF_FUEL, STOPPED)

---

## Author

Assignment 3 - Advanced Programming M2025
