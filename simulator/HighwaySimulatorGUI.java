package simulator;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * HighwaySimulatorGUI - Main GUI for the Fleet Highway Simulator
 * Provides controls for starting, pausing, resuming, and stopping the simulation.
 * Displays vehicle states and the shared Highway Distance Counter.
 * Demonstrates race condition and its fix.
 */
public class HighwaySimulatorGUI extends JFrame implements SimulationVehicle.VehicleUpdateCallback {
    
    // Number of vehicles to simulate
    private static final int NUM_VEHICLES = 3;
    private static final double INITIAL_FUEL = 20.0;
    
    // Simulation vehicles
    private List<SimulationVehicle> vehicles;
    
    // GUI Components
    private JPanel vehiclePanels[];
    private JLabel[] vehicleIdLabels;
    private JLabel[] vehicleMileageLabels;
    private JLabel[] vehicleFuelLabels;
    private JLabel[] vehicleStatusLabels;
    private JButton[] refuelButtons;
    
    private JLabel highwayCounterLabel;
    private JLabel expectedCounterLabel;
    private JLabel discrepancyLabel;
    
    private JButton startButton;
    private JButton pauseButton;
    private JButton resumeButton;
    private JButton stopButton;
    private JButton resetButton;
    
    private JCheckBox syncModeCheckBox;
    private JTextArea logArea;
    private JLabel modeLabel;
    
    // Tracking expected total
    private int expectedTotal = 0;
    
    // Timer for UI updates
    private Timer updateTimer;
    
    /**
     * Constructor - sets up the GUI
     */
    public HighwaySimulatorGUI() {
        setTitle("Fleet Highway Simulator - Multithreading Demo");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 700);
        setLocationRelativeTo(null);
        
        initializeVehicles();
        initializeGUI();
        setupUpdateTimer();
    }
    
    /**
     * Initializes the simulation vehicles
     */
    private void initializeVehicles() {
        vehicles = new ArrayList<>();
        for (int i = 1; i <= NUM_VEHICLES; i++) {
            SimulationVehicle vehicle = new SimulationVehicle("Vehicle-" + i, INITIAL_FUEL);
            vehicle.setUpdateCallback(this);
            vehicles.add(vehicle);
        }
    }
    
    /**
     * Initializes the GUI components
     */
    private void initializeGUI() {
        setLayout(new BorderLayout(10, 10));
        
        // Top panel - Control buttons and sync mode
        JPanel controlPanel = createControlPanel();
        add(controlPanel, BorderLayout.NORTH);
        
        // Center panel - Vehicle displays
        JPanel vehiclePanel = createVehiclePanel();
        add(vehiclePanel, BorderLayout.CENTER);
        
        // Right panel - Highway counter and stats
        JPanel statsPanel = createStatsPanel();
        add(statsPanel, BorderLayout.EAST);
        
        // Bottom panel - Log area
        JPanel logPanel = createLogPanel();
        add(logPanel, BorderLayout.SOUTH);
    }
    
    /**
     * Creates the control panel with buttons
     */
    private JPanel createControlPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Simulation Controls"));
        
        startButton = new JButton("Start");
        pauseButton = new JButton("Pause All");
        resumeButton = new JButton("Resume All");
        stopButton = new JButton("Stop");
        resetButton = new JButton("Reset");
        
        syncModeCheckBox = new JCheckBox("Enable Synchronization");
        syncModeCheckBox.setToolTipText("Toggle between synchronized and unsynchronized mode");
        
        // Button styling
        Dimension buttonSize = new Dimension(100, 30);
        startButton.setPreferredSize(buttonSize);
        pauseButton.setPreferredSize(new Dimension(110, 30));
        resumeButton.setPreferredSize(new Dimension(110, 30));
        stopButton.setPreferredSize(buttonSize);
        resetButton.setPreferredSize(buttonSize);
        
        // Initial button states
        pauseButton.setEnabled(false);
        resumeButton.setEnabled(false);
        stopButton.setEnabled(false);
        
        // Add action listeners
        startButton.addActionListener(e -> startSimulation());
        pauseButton.addActionListener(e -> pauseSimulation());
        resumeButton.addActionListener(e -> resumeSimulation());
        stopButton.addActionListener(e -> stopSimulation());
        resetButton.addActionListener(e -> resetSimulation());
        syncModeCheckBox.addActionListener(e -> toggleSyncMode());
        
        panel.add(startButton);
        panel.add(pauseButton);
        panel.add(resumeButton);
        panel.add(stopButton);
        panel.add(resetButton);
        panel.add(Box.createHorizontalStrut(30));
        panel.add(syncModeCheckBox);
        
        return panel;
    }
    
    /**
     * Creates the vehicle display panel
     */
    private JPanel createVehiclePanel() {
        JPanel mainPanel = new JPanel(new GridLayout(1, NUM_VEHICLES, 10, 10));
        mainPanel.setBorder(BorderFactory.createTitledBorder("Vehicles"));
        
        vehiclePanels = new JPanel[NUM_VEHICLES];
        vehicleIdLabels = new JLabel[NUM_VEHICLES];
        vehicleMileageLabels = new JLabel[NUM_VEHICLES];
        vehicleFuelLabels = new JLabel[NUM_VEHICLES];
        vehicleStatusLabels = new JLabel[NUM_VEHICLES];
        refuelButtons = new JButton[NUM_VEHICLES];
        
        for (int i = 0; i < NUM_VEHICLES; i++) {
            final int index = i;
            JPanel vPanel = new JPanel();
            vPanel.setLayout(new BoxLayout(vPanel, BoxLayout.Y_AXIS));
            vPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY, 2),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
            ));
            
            SimulationVehicle vehicle = vehicles.get(i);
            
            vehicleIdLabels[i] = new JLabel("ID: " + vehicle.getVehicleId());
            vehicleIdLabels[i].setFont(new Font("Arial", Font.BOLD, 14));
            vehicleIdLabels[i].setAlignmentX(Component.CENTER_ALIGNMENT);
            
            vehicleMileageLabels[i] = new JLabel("Mileage: 0 km");
            vehicleMileageLabels[i].setAlignmentX(Component.CENTER_ALIGNMENT);
            
            vehicleFuelLabels[i] = new JLabel(String.format("Fuel: %.1f / %.1f", vehicle.getFuelLevel(), vehicle.getMaxFuel()));
            vehicleFuelLabels[i].setAlignmentX(Component.CENTER_ALIGNMENT);
            
            vehicleStatusLabels[i] = new JLabel("Status: PAUSED");
            vehicleStatusLabels[i].setAlignmentX(Component.CENTER_ALIGNMENT);
            vehicleStatusLabels[i].setForeground(Color.ORANGE);
            
            refuelButtons[i] = new JButton("Refuel");
            refuelButtons[i].setAlignmentX(Component.CENTER_ALIGNMENT);
            refuelButtons[i].addActionListener(e -> refuelVehicle(index));
            
            vPanel.add(Box.createVerticalGlue());
            vPanel.add(vehicleIdLabels[i]);
            vPanel.add(Box.createVerticalStrut(10));
            vPanel.add(vehicleMileageLabels[i]);
            vPanel.add(Box.createVerticalStrut(5));
            vPanel.add(vehicleFuelLabels[i]);
            vPanel.add(Box.createVerticalStrut(5));
            vPanel.add(vehicleStatusLabels[i]);
            vPanel.add(Box.createVerticalStrut(15));
            vPanel.add(refuelButtons[i]);
            vPanel.add(Box.createVerticalGlue());
            
            vehiclePanels[i] = vPanel;
            mainPanel.add(vPanel);
        }
        
        return mainPanel;
    }
    
    /**
     * Creates the stats panel showing the highway counter
     */
    private JPanel createStatsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createTitledBorder("Highway Statistics"));
        panel.setPreferredSize(new Dimension(250, 0));
        
        // Highway Counter
        JPanel counterPanel = new JPanel();
        counterPanel.setLayout(new BoxLayout(counterPanel, BoxLayout.Y_AXIS));
        counterPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("Highway Distance Counter"),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        highwayCounterLabel = new JLabel("0 km");
        highwayCounterLabel.setFont(new Font("Arial", Font.BOLD, 28));
        highwayCounterLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel counterDescLabel = new JLabel("Total Distance Traveled");
        counterDescLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        counterPanel.add(highwayCounterLabel);
        counterPanel.add(Box.createVerticalStrut(5));
        counterPanel.add(counterDescLabel);
        
        // Expected vs Actual
        JPanel comparisonPanel = new JPanel();
        comparisonPanel.setLayout(new BoxLayout(comparisonPanel, BoxLayout.Y_AXIS));
        comparisonPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("Race Condition Analysis"),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        expectedCounterLabel = new JLabel("Expected: 0 km");
        expectedCounterLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        discrepancyLabel = new JLabel("Discrepancy: 0 km");
        discrepancyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        discrepancyLabel.setForeground(Color.GREEN);
        
        modeLabel = new JLabel("Mode: Unsynchronized");
        modeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        modeLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        
        comparisonPanel.add(expectedCounterLabel);
        comparisonPanel.add(Box.createVerticalStrut(10));
        comparisonPanel.add(discrepancyLabel);
        comparisonPanel.add(Box.createVerticalStrut(10));
        comparisonPanel.add(modeLabel);
        
        panel.add(Box.createVerticalStrut(20));
        panel.add(counterPanel);
        panel.add(Box.createVerticalStrut(20));
        panel.add(comparisonPanel);
        panel.add(Box.createVerticalGlue());
        
        return panel;
    }
    
    /**
     * Creates the log panel
     */
    private JPanel createLogPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Simulation Log"));
        panel.setPreferredSize(new Dimension(0, 150));
        
        logArea = new JTextArea();
        logArea.setEditable(false);
        logArea.setFont(new Font("Monospaced", Font.PLAIN, 11));
        
        JScrollPane scrollPane = new JScrollPane(logArea);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * Sets up a timer to periodically update the UI
     */
    private void setupUpdateTimer() {
        updateTimer = new Timer(100, e -> updateUI());
    }
    
    /**
     * Starts the simulation
     */
    private void startSimulation() {
        log("Starting simulation with " + NUM_VEHICLES + " vehicles...");
        log("Synchronization mode: " + (HighwayDistanceCounter.isSynchronizationEnabled() ? "ENABLED" : "DISABLED"));
        
        for (SimulationVehicle vehicle : vehicles) {
            if (!vehicle.isAlive()) {
                vehicle.start();
            }
            vehicle.resumeVehicle();
        }
        
        updateTimer.start();
        
        startButton.setEnabled(false);
        pauseButton.setEnabled(true);
        resumeButton.setEnabled(false);
        stopButton.setEnabled(true);
        syncModeCheckBox.setEnabled(false);
        
        log("Simulation started!");
    }
    
    /**
     * Pauses all vehicles
     */
    private void pauseSimulation() {
        for (SimulationVehicle vehicle : vehicles) {
            vehicle.pauseVehicle();
        }
        
        pauseButton.setEnabled(false);
        resumeButton.setEnabled(true);
        
        log("Simulation paused.");
    }
    
    /**
     * Resumes all vehicles
     */
    private void resumeSimulation() {
        for (SimulationVehicle vehicle : vehicles) {
            if (vehicle.getFuelLevel() > 0) {
                vehicle.resumeVehicle();
            }
        }
        
        pauseButton.setEnabled(true);
        resumeButton.setEnabled(false);
        
        log("Simulation resumed.");
    }
    
    /**
     * Stops the simulation
     */
    private void stopSimulation() {
        for (SimulationVehicle vehicle : vehicles) {
            vehicle.stopVehicle();
        }
        
        updateTimer.stop();
        
        startButton.setEnabled(false);
        pauseButton.setEnabled(false);
        resumeButton.setEnabled(false);
        stopButton.setEnabled(false);
        resetButton.setEnabled(true);
        
        // Calculate and show final results
        int actual = HighwayDistanceCounter.getDistance();
        int expected = calculateExpectedTotal();
        int discrepancy = expected - actual;
        
        log("=== SIMULATION STOPPED ===");
        log("Final Highway Counter: " + actual + " km");
        log("Expected Total: " + expected + " km");
        log("Discrepancy: " + discrepancy + " km");
        
        if (!HighwayDistanceCounter.isSynchronizationEnabled() && discrepancy != 0) {
            log("*** RACE CONDITION DETECTED! ***");
            log("The unsynchronized access to the shared counter caused data loss.");
        } else if (HighwayDistanceCounter.isSynchronizationEnabled() && discrepancy == 0) {
            log("*** SYNCHRONIZATION WORKING CORRECTLY ***");
            log("The synchronized access prevented race conditions.");
        }
    }
    
    /**
     * Resets the simulation
     */
    private void resetSimulation() {
        // Stop existing vehicles
        for (SimulationVehicle vehicle : vehicles) {
            if (vehicle.isAlive()) {
                vehicle.stopVehicle();
            }
        }
        
        // Wait for threads to finish properly using join
        for (SimulationVehicle vehicle : vehicles) {
            try {
                vehicle.join(1000); // Wait up to 1 second for each thread
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        
        // Reset counter
        HighwayDistanceCounter.reset();
        expectedTotal = 0;
        
        // Create new vehicles
        vehicles.clear();
        initializeVehicles();
        
        // Reset UI
        for (int i = 0; i < NUM_VEHICLES; i++) {
            SimulationVehicle vehicle = vehicles.get(i);
            vehicleIdLabels[i].setText("ID: " + vehicle.getVehicleId());
            vehicleMileageLabels[i].setText("Mileage: 0 km");
            vehicleFuelLabels[i].setText(String.format("Fuel: %.1f / %.1f", vehicle.getFuelLevel(), vehicle.getMaxFuel()));
            vehicleStatusLabels[i].setText("Status: PAUSED");
            vehicleStatusLabels[i].setForeground(Color.ORANGE);
        }
        
        highwayCounterLabel.setText("0 km");
        expectedCounterLabel.setText("Expected: 0 km");
        discrepancyLabel.setText("Discrepancy: 0 km");
        discrepancyLabel.setForeground(Color.GREEN);
        
        startButton.setEnabled(true);
        pauseButton.setEnabled(false);
        resumeButton.setEnabled(false);
        stopButton.setEnabled(false);
        syncModeCheckBox.setEnabled(true);
        
        log("Simulation reset. Ready to start again.");
    }
    
    /**
     * Toggles synchronization mode
     */
    private void toggleSyncMode() {
        boolean enabled = syncModeCheckBox.isSelected();
        HighwayDistanceCounter.setSynchronizationMode(enabled);
        modeLabel.setText("Mode: " + (enabled ? "Synchronized" : "Unsynchronized"));
        log("Synchronization mode: " + (enabled ? "ENABLED (using ReentrantLock)" : "DISABLED (race condition possible)"));
    }
    
    /**
     * Refuels a specific vehicle
     */
    private void refuelVehicle(int index) {
        SimulationVehicle vehicle = vehicles.get(index);
        vehicle.refuelFull();
        log(vehicle.getVehicleId() + " refueled to full capacity.");
        updateVehicleDisplay(index);
    }
    
    /**
     * Updates the UI periodically
     */
    private void updateUI() {
        // Run UI updates on EDT
        SwingUtilities.invokeLater(() -> {
            // Update vehicle displays
            for (int i = 0; i < NUM_VEHICLES; i++) {
                updateVehicleDisplay(i);
            }
            
            // Update highway counter
            int actual = HighwayDistanceCounter.getDistance();
            highwayCounterLabel.setText(actual + " km");
            
            // Calculate expected total
            int expected = calculateExpectedTotal();
            expectedCounterLabel.setText("Expected: " + expected + " km");
            
            // Calculate and show discrepancy
            int discrepancy = expected - actual;
            discrepancyLabel.setText("Discrepancy: " + discrepancy + " km");
            
            if (discrepancy != 0) {
                discrepancyLabel.setForeground(Color.RED);
            } else {
                discrepancyLabel.setForeground(Color.GREEN);
            }
        });
    }
    
    /**
     * Updates a specific vehicle's display
     */
    private void updateVehicleDisplay(int index) {
        SimulationVehicle vehicle = vehicles.get(index);
        
        vehicleMileageLabels[index].setText("Mileage: " + (int) vehicle.getMileage() + " km");
        vehicleFuelLabels[index].setText(String.format("Fuel: %.1f / %.1f", vehicle.getFuelLevel(), vehicle.getMaxFuel()));
        
        SimulationVehicle.Status status = vehicle.getVehicleStatus();
        vehicleStatusLabels[index].setText("Status: " + status.name());
        
        // Color code status
        switch (status) {
            case RUNNING:
                vehicleStatusLabels[index].setForeground(Color.GREEN);
                vehiclePanels[index].setBackground(new Color(220, 255, 220));
                break;
            case PAUSED:
                vehicleStatusLabels[index].setForeground(Color.ORANGE);
                vehiclePanels[index].setBackground(new Color(255, 255, 220));
                break;
            case OUT_OF_FUEL:
                vehicleStatusLabels[index].setForeground(Color.RED);
                vehiclePanels[index].setBackground(new Color(255, 220, 220));
                break;
            case STOPPED:
                vehicleStatusLabels[index].setForeground(Color.GRAY);
                vehiclePanels[index].setBackground(new Color(220, 220, 220));
                break;
        }
    }
    
    /**
     * Calculates the expected total based on vehicle mileages
     */
    private int calculateExpectedTotal() {
        int total = 0;
        for (SimulationVehicle vehicle : vehicles) {
            total += (int) vehicle.getMileage();
        }
        return total;
    }
    
    /**
     * Logs a message to the log area
     */
    private void log(String message) {
        SwingUtilities.invokeLater(() -> {
            logArea.append("[" + java.time.LocalTime.now().toString().substring(0, 8) + "] " + message + "\n");
            logArea.setCaretPosition(logArea.getDocument().getLength());
        });
    }
    
    /**
     * Callback from vehicle threads
     */
    @Override
    public void onVehicleUpdate(SimulationVehicle vehicle) {
        // Updates are handled by the timer to prevent excessive EDT invocations
    }
    
    /**
     * Main method to launch the application
     */
    public static void main(String[] args) {
        // Ensure GUI is created on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                // Use default look and feel
            }
            
            HighwaySimulatorGUI gui = new HighwaySimulatorGUI();
            gui.setVisible(true);
        });
    }
}
