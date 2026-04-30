package app;

import java.io.IOException;
import persistence.LaunchHistoryService;
import ui.ConsoleInterface;

public class SpaceLaunchApplication {
    private final LaunchHistoryService launchHistoryService;

    public SpaceLaunchApplication() {
        this(new LaunchHistoryService());
    }

    public SpaceLaunchApplication(LaunchHistoryService launchHistoryService) {
        this.launchHistoryService = launchHistoryService;
    }

    public void start() {
        loadHistory();
        ConsoleInterface consoleInterface = new ConsoleInterface();
        consoleInterface.start();
    }

    private void loadHistory() {
        try {
            launchHistoryService.loadHistory();
        } catch (IOException exception) {
            throw new IllegalStateException("Unable to load launch history", exception);
        }
    }
}
