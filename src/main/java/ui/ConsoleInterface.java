package ui;

import domain.booster.Booster;
import domain.capsule.Capsule;
import domain.launch.LaunchResult;
import domain.launcher.Launcher;
import domain.mission.CustomMission;
import domain.mission.Mission;
import domain.rocket.Rocket;
import java.io.IOException;
import java.lang.ProcessBuilder.Redirect;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import persistence.LaunchHistoryService;
import service.ComponentCatalog;
import service.LaunchSimulationService;
import service.RocketConfigurationService;

public class ConsoleInterface {
    private static final String CLEAR_SCREEN = "\033[H\033[2J";
    private static final String RESET = "\033[0m";
    private static final String HIGHLIGHT = "\033[7m";
    private static final int KEY_ENTER = 10;
    private static final int KEY_UP = 1001;
    private static final int KEY_DOWN = 1002;

    private final Scanner scanner;
    private final ComponentCatalog componentCatalog;
    private final RocketConfigurationService rocketConfigurationService;
    private final LaunchSimulationService launchSimulationService;
    private final LaunchHistoryService launchHistoryService;
    private Launcher selectedLauncher;
    private Capsule selectedCapsule;
    private List<Booster> selectedBoosters;
    private Mission selectedMission;
    private Rocket configuredRocket;
    private boolean running;

    public ConsoleInterface(LaunchHistoryService launchHistoryService) {
        this.scanner = new Scanner(System.in);
        this.componentCatalog = new ComponentCatalog();
        this.rocketConfigurationService = new RocketConfigurationService();
        this.launchSimulationService = new LaunchSimulationService(launchHistoryService);
        this.launchHistoryService = launchHistoryService;
        this.selectedBoosters = new ArrayList<>();
        this.running = true;
    }

    public void start() {
        while (running) {
            handleMainMenuChoice(showSelectionMenu("Space Launch Simulator", List.of(
                    "Configure rocket",
                    "Choose mission",
                    "Run launch simulation",
                    "Display history",
                    "Exit"
            )));
        }
    }

    private void handleMainMenuChoice(int choice) {
        switch (choice) {
            case 0:
                configureRocket();
                break;
            case 1:
                selectMission();
                break;
            case 2:
                runLaunchSimulation();
                break;
            case 3:
                displayHistory();
                break;
            case 4:
                running = false;
                clearScreen();
                System.out.println("Goodbye.");
                break;
            default:
                break;
        }
    }

    private void configureRocket() {
        while (true) {
            if (!selectLauncher()) {
                return;
            }
            if (!selectCapsule()) {
                continue;
            }
            if (selectBoosters()) {
                return;
            }
        }
    }

    private boolean selectLauncher() {
        List<Launcher> launchers = componentCatalog.getLaunchers();
        List<String> options = new ArrayList<>();

        for (Launcher launcher : launchers) {
            options.add(launcher.getName()
                    + " | payload " + launcher.getPayloadCapacityTons() + " t"
                    + " | fuel " + launcher.getMaxFuelTons() + " t"
                    + " | max boosters " + launcher.getMaxBoosters()
                    + " | price " + launcher.getPriceMillionEuros() + " M EUR");
        }

        options.add("Back");
        int choice = showSelectionMenu("Available launchers", options);
        if (choice == options.size() - 1) {
            return false;
        }

        selectedLauncher = launchers.get(choice);
        return true;
    }

    private boolean selectCapsule() {
        List<Capsule> capsules = componentCatalog.getCapsules();
        List<String> options = new ArrayList<>();

        for (Capsule capsule : capsules) {
            options.add(capsule.getName()
                    + " | crewed " + formatBoolean(capsule.isCrewed())
                    + " | occupants " + capsule.getMaxOccupants()
                    + " | mass " + capsule.getMassTons() + " t"
                    + " | price " + capsule.getPriceMillionEuros() + " M EUR");
        }

        options.add("Back");
        int choice = showSelectionMenu("Available capsules", options);
        if (choice == options.size() - 1) {
            return false;
        }

        selectedCapsule = capsules.get(choice);
        return true;
    }

    private boolean selectBoosters() {
        selectedBoosters = new ArrayList<>();
        int boosterCount = selectBoosterCount();

        if (boosterCount < 0) {
            return false;
        }

        for (int index = 0; index < boosterCount; index++) {
            Booster booster = selectBooster(index + 1);
            if (booster == null) {
                return false;
            }
            selectedBoosters.add(booster);
        }

        try {
            configuredRocket = rocketConfigurationService.buildRocket(selectedLauncher, selectedCapsule, selectedBoosters);
            showMessage("Rocket configured", configuredRocket.getSummary());
            return true;
        } catch (IllegalArgumentException exception) {
            configuredRocket = null;
            showMessage("Configuration failed", exception.getMessage());
            return false;
        }
    }

    private int selectBoosterCount() {
        int maxBoosters = selectedLauncher.getMaxBoosters();
        List<String> options = new ArrayList<>();

        for (int index = 0; index <= maxBoosters; index++) {
            options.add(index + " booster" + (index == 1 ? "" : "s"));
        }

        options.add("Back");
        int choice = showSelectionMenu("Booster count", options);
        if (choice == options.size() - 1) {
            return -1;
        }

        return choice;
    }

    private Booster selectBooster(int boosterNumber) {
        List<Booster> boosters = componentCatalog.getBoosters();
        List<String> options = new ArrayList<>();

        for (Booster booster : boosters) {
            options.add(booster.getName()
                    + " | thrust " + booster.getAdditionalThrustKilonewtons() + " kN"
                    + " | mass " + booster.getMassTons() + " t"
                    + " | price " + booster.getPriceMillionEuros() + " M EUR");
        }

        options.add("Back");
        int choice = showSelectionMenu("Choose booster " + boosterNumber, options);
        if (choice == options.size() - 1) {
            return null;
        }

        return boosters.get(choice);
    }

    private void selectMission() {
        while (true) {
            List<Mission> missions = componentCatalog.getMissions();
            List<String> options = new ArrayList<>();

            for (int index = 0; index < missions.size() - 1; index++) {
                Mission mission = missions.get(index);
                options.add(mission.getName()
                        + " | crew required " + formatBoolean(mission.isCrewRequired())
                        + " | distance " + mission.getDistanceKilometers() + " km"
                        + " | duration " + mission.getDuration()
                        + " | fuel coefficient " + mission.getFuelCoefficient());
            }

            options.add("Custom mission");
            options.add("Back");
            int choice = showSelectionMenu("Available missions", options);

            if (choice == options.size() - 1) {
                return;
            }

            if (choice == options.size() - 2) {
                Mission customMission = createCustomMission();
                if (customMission != null) {
                    selectedMission = customMission;
                    showMessage("Mission selected", selectedMission.getName());
                    return;
                }
            } else {
                selectedMission = missions.get(choice);
                showMessage("Mission selected", selectedMission.getName());
                return;
            }
        }
    }

    private Mission createCustomMission() {
        clearScreen();
        System.out.println("Custom mission");
        System.out.println();
        System.out.println("Leave the mission name empty to go back.");
        System.out.println();
        System.out.print("Mission name: ");
        String name = scanner.nextLine();

        if (name.isBlank()) {
            return null;
        }

        System.out.print("Crew required? yes/no: ");
        boolean crewRequired = readYesNo();
        System.out.print("Distance in kilometers: ");
        double distanceKilometers = readPositiveDouble();
        System.out.print("Duration: ");
        String duration = readRequiredText();
        System.out.print("Fuel coefficient: ");
        double fuelCoefficient = readPositiveDouble();
        return new CustomMission(name, crewRequired, distanceKilometers, duration, fuelCoefficient);
    }

    private void runLaunchSimulation() {
        if (configuredRocket == null) {
            showMessage("Simulation unavailable", "Configure a rocket before running a simulation.");
            return;
        }

        if (selectedMission == null) {
            showMessage("Simulation unavailable", "Choose a mission before running a simulation.");
            return;
        }

        LaunchResult launchResult = launchSimulationService.runLaunch(configuredRocket, selectedMission);
        showMessage("Launch result", launchResult.getSummary());
    }

    private void displayHistory() {
        List<LaunchResult> history = launchHistoryService.getHistory();
        StringBuilder content = new StringBuilder();

        if (history.isEmpty()) {
            content.append("No launch history yet.");
        } else {
            for (int index = 0; index < history.size(); index++) {
                content.append("Launch ").append(index + 1).append("\n");
                content.append(history.get(index).getSummary());

                if (index < history.size() - 1) {
                    content.append("\n\n");
                }
            }
        }

        showMessage("Launch history", content.toString());
    }

    private int showSelectionMenu(String title, List<String> options) {
        int selectedIndex = 0;
        enableRawMode();

        try {
            while (true) {
                drawSelectionMenu(title, options, selectedIndex);
                int key = readKey();

                if (key == KEY_UP) {
                    selectedIndex = selectedIndex == 0 ? options.size() - 1 : selectedIndex - 1;
                } else if (key == KEY_DOWN) {
                    selectedIndex = selectedIndex == options.size() - 1 ? 0 : selectedIndex + 1;
                } else if (key == KEY_ENTER) {
                    return selectedIndex;
                }
            }
        } finally {
            disableRawMode();
        }
    }

    private void drawSelectionMenu(String title, List<String> options, int selectedIndex) {
        clearScreen();
        System.out.println(title);
        System.out.println();

        for (int index = 0; index < options.size(); index++) {
            String prefix = index == selectedIndex ? "-> " : "   ";
            String value = prefix + options.get(index);

            if (index == selectedIndex) {
                System.out.println(HIGHLIGHT + value + RESET);
            } else {
                System.out.println(value);
            }
        }

        System.out.println();
        System.out.println("Use up/down arrows and Enter.");
    }

    private void showMessage(String title, String message) {
        clearScreen();
        System.out.println(title);
        System.out.println();
        System.out.println(message);
        System.out.println();
        System.out.println("Press Enter to go back.");
        scanner.nextLine();
    }

    private int readKey() {
        try {
            int first = System.in.read();

            if (first == 13 || first == 10) {
                return KEY_ENTER;
            }

            if (first == 27 && System.in.read() == 91) {
                int third = System.in.read();
                if (third == 65) {
                    return KEY_UP;
                }
                if (third == 66) {
                    return KEY_DOWN;
                }
            }

            return first;
        } catch (IOException exception) {
            throw new IllegalStateException("Unable to read keyboard input", exception);
        }
    }

    private void clearScreen() {
        System.out.print(CLEAR_SCREEN);
        System.out.flush();
    }

    private void enableRawMode() {
        runTerminalCommand("stty", "-echo", "raw");
    }

    private void disableRawMode() {
        runTerminalCommand("stty", "echo", "-raw");
    }

    private void runTerminalCommand(String... command) {
        try {
            new ProcessBuilder(command)
                    .redirectInput(Redirect.INHERIT)
                    .redirectOutput(Redirect.DISCARD)
                    .redirectError(Redirect.DISCARD)
                    .start()
                    .waitFor();
        } catch (IOException | InterruptedException exception) {
            Thread.currentThread().interrupt();
        }
    }

    private String formatBoolean(boolean value) {
        return value ? "Yes" : "No";
    }

    private double readPositiveDouble() {
        while (true) {
            try {
                double value = Double.parseDouble(scanner.nextLine());
                if (value > 0) {
                    return value;
                }
            } catch (NumberFormatException exception) {
                System.out.print("Enter a valid number: ");
                continue;
            }
            System.out.print("Enter a positive number: ");
        }
    }

    private boolean readYesNo() {
        while (true) {
            String value = scanner.nextLine();
            if (value.equalsIgnoreCase("yes")) {
                return true;
            }
            if (value.equalsIgnoreCase("no")) {
                return false;
            }
            System.out.print("Enter yes or no: ");
        }
    }

    private String readRequiredText() {
        while (true) {
            String value = scanner.nextLine();
            if (!value.isBlank()) {
                return value;
            }
            System.out.print("Enter a value: ");
        }
    }
}
