package app;

import persistence.LaunchHistoryService;
import ui.ConsoleInterface;

/**
 * starts the console application
 */
public class SpaceLaunchApplication {
    private final Simulator simulator;

    public SpaceLaunchApplication() {
        this(Simulator.getInstance());
    }

    public SpaceLaunchApplication(LaunchHistoryService launchHistoryService) {
        this(Simulator.getInstance(launchHistoryService));
    }

    public SpaceLaunchApplication(Simulator simulator) {
        this.simulator = simulator;
    }

    /**
     * prepares history then starts the console interface
     */
    public void start() {
        simulator.loadHistory();
        ConsoleInterface consoleInterface = new ConsoleInterface(simulator);
        consoleInterface.start();
    }
}
