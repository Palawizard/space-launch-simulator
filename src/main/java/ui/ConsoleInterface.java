package ui;

import java.io.IOException;
import java.lang.ProcessBuilder.Redirect;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

import app.Simulator;
import domain.booster.Booster;
import domain.capsule.Capsule;
import domain.launch.LaunchResult;
import domain.launcher.Launcher;
import domain.mission.CustomMission;
import domain.mission.Mission;
import domain.rocket.Rocket;
import exception.LaunchException;

/**
 * interactive console user interface
 */
public class ConsoleInterface {
    private static final String APP_TITLE = "Space Launch Simulator";
    private static final String RESET = "\033[0m";
    private static final String CLEAR_SCREEN = RESET + "\033[2J\033[3J\033[H";
    private static final String HIGHLIGHT = "\033[7m";
    private static final int KEY_ENTER = 10;
    private static final int KEY_UP = 1001;
    private static final int KEY_DOWN = 1002;
    private static final int KEY_END = -1;

    private final Scanner scanner;
    private final Simulator simulator;
    private Launcher selectedLauncher;
    private Capsule selectedCapsule;
    private List<Booster> selectedBoosters;
    private Mission selectedMission;
    private Rocket configuredRocket;
    private boolean running;

    public ConsoleInterface(Simulator simulator) {
        this.scanner = new Scanner(System.in);
        this.simulator = simulator;
        this.selectedBoosters = new ArrayList<>();
        this.running = true;
    }

    /**
     * starts the main menu loop
     */
    public void start() {
        while (running) {
            handleMainMenuChoice(showSelectionMenu("Main menu", List.of(
                    "Configure rocket",
                    "Choose mission",
                    "Run launch simulation",
                    "Display history",
                    "Exit"
            ), getCurrentSelectionSummary()));
        }
    }

    /**
     * routes the selected main menu action
     */
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
                break;
            default:
                break;
        }
    }

    /**
     * guides the user through rocket setup
     */
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

    /**
     * lets the user choose a launcher preset
     */
    private boolean selectLauncher() {
        List<Launcher> launchers = simulator.getLaunchers();
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

    /**
     * lets the user choose a capsule preset
     */
    private boolean selectCapsule() {
        List<Capsule> capsules = simulator.getCapsules();
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

    /**
     * lets the user choose and validate boosters
     */
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
            configuredRocket = simulator.buildRocket(selectedLauncher, selectedCapsule, selectedBoosters);
            showMessage("Rocket configured", configuredRocket.getSummary());
            return true;
        } catch (IllegalArgumentException exception) {
            configuredRocket = null;
            showMessage("Configuration failed", exception.getMessage());
            return false;
        }
    }

    /**
     * asks how many boosters should be added
     */
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

    /**
     * lets the user choose one booster preset
     */
    private Booster selectBooster(int boosterNumber) {
        List<Booster> boosters = simulator.getBoosters();
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

    /**
     * lets the user choose a preset or custom mission
     */
    private void selectMission() {
        while (true) {
            List<Mission> missions = simulator.getMissions();
            List<String> options = new ArrayList<>();

            for (Mission mission : missions) {
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
                    showMissionSelectionMessage();
                    return;
                }
            } else {
                selectedMission = missions.get(choice);
                showMissionSelectionMessage();
                return;
            }
        }
    }

    /**
     * builds a custom mission from console input
     */
    private Mission createCustomMission() {
        clearScreen();
        printHeader();
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
        System.out.print("Duration (for example: 6 months, 2 years): ");
        String duration = readRequiredText();
        System.out.print("Fuel coefficient: ");
        double fuelCoefficient = readPositiveDouble();
        return new CustomMission(name, crewRequired, distanceKilometers, duration, fuelCoefficient);
    }

    /**
     * runs the selected rocket and mission
     */
    private void runLaunchSimulation() {
        if (configuredRocket == null) {
            showMessage("Simulation unavailable", "Configure a rocket before running a simulation.");
            return;
        }

        if (selectedMission == null) {
            showMessage("Simulation unavailable", "Choose a mission before running a simulation.");
            return;
        }

        LaunchResult launchResult = simulator.runLaunch(configuredRocket, selectedMission);
        showMessage("Launch result", launchResult.getSummary());
        resetSelection();
    }

    /**
     * shows saved launch results
     */
    private void displayHistory() {
        List<LaunchResult> history = simulator.getHistory();
        if (history.isEmpty()) {
            showMessage("Launch history", "No launch history yet.");
            return;
        }

        while (true) {
            List<String> options = new ArrayList<>();

            for (int index = 0; index < history.size(); index++) {
                LaunchResult launchResult = history.get(index);
                options.add(formatHistoryIndex(index + 1)
                        + launchResult.getFormattedDate()
                        + " | " + getHistoryRocketName(launchResult)
                        + " | " + launchResult.getMissionName()
                        + " | " + launchResult.getVerdict()
                        + " | " + formatCost(launchResult.getTotalCostEuros()));
            }

            options.add("Back");
            int choice = showSelectionMenu("Launch history", options);

            if (choice == options.size() - 1) {
                return;
            }

            showMessage("Launch " + (choice + 1), history.get(choice).getSummary());
        }
    }

    /**
     * clears the current simulation choices
     */
    private void resetSelection() {
        selectedLauncher = null;
        selectedCapsule = null;
        selectedBoosters = new ArrayList<>();
        selectedMission = null;
        configuredRocket = null;
    }

    /**
     * shows the chosen mission with compatibility status
     */
    private void showMissionSelectionMessage() {
        String message = section("mission")
                + "\nName: " + selectedMission.getName()
                + "\nObjective: " + selectedMission.getObjective()
                + "\n\n" + section("compatibility")
                + "\nCompatibility: " + getMissionCompatibilityStatus();
        showMessage("Mission selected", message);
    }

    /**
     * checks the selected mission against the configured rocket
     */
    private String getMissionCompatibilityStatus() {
        if (configuredRocket == null) {
            return "Configure a rocket to check this mission.";
        }

        try {
            simulator.validateLaunchConditions(configuredRocket, selectedMission);
            return "Compatible with the configured rocket.";
        } catch (LaunchException exception) {
            return "Launch would fail: " + exception.getMessage();
        }
    }

    /**
     * shows a menu without footer text
     */
    private int showSelectionMenu(String title, List<String> options) {
        return showSelectionMenu(title, options, "");
    }

    /**
     * shows a keyboard driven selection menu
     */
    private int showSelectionMenu(String title, List<String> options, String footer) {
        int selectedIndex = 0;
        enableRawMode();

        try {
            while (true) {
                drawSelectionMenu(title, options, selectedIndex, footer);
                int key = readKey();

                if (key == KEY_UP) {
                    selectedIndex = selectedIndex == 0 ? options.size() - 1 : selectedIndex - 1;
                } else if (key == KEY_DOWN) {
                    selectedIndex = selectedIndex == options.size() - 1 ? 0 : selectedIndex + 1;
                } else if (key == KEY_ENTER) {
                    return selectedIndex;
                } else if (key == KEY_END) {
                    return options.size() - 1;
                }
            }
        } finally {
            disableRawMode();
        }
    }

    /**
     * redraws the menu with the current selection highlighted
     */
    private void drawSelectionMenu(String title, List<String> options, int selectedIndex, String footer) {
        clearScreen();
        printHeader();
        printScreenTitle(title);

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

        if (!footer.isBlank()) {
            System.out.println();
            System.out.println(footer);
        }
    }

    /**
     * builds the footer summary for the main menu
     */
    private String getCurrentSelectionSummary() {
        StringBuilder summary = new StringBuilder();
        summary.append(section("current selection")).append("\n");
        summary.append("Launcher: ").append(selectedLauncher == null ? "None" : selectedLauncher.getName()).append("\n");
        summary.append("Capsule: ").append(selectedCapsule == null ? "None" : selectedCapsule.getName()).append("\n");
        summary.append("Boosters: ").append(getSelectedBoostersSummary()).append("\n");
        summary.append("Mission: ").append(selectedMission == null ? "None" : selectedMission.getName());

        if (configuredRocket != null) {
            summary.append("\n\n");
            summary.append(section("rocket totals")).append("\n");
            summary.append("Rocket mass: ").append(formatDecimal(configuredRocket.getTotalMassTons())).append(" t").append("\n");
            summary.append("Rocket price: ").append(formatDecimal(configuredRocket.getTotalPriceMillionEuros())).append(" M EUR");
        }

        return summary.toString();
    }

    /**
     * formats the selected boosters for display
     */
    private String getSelectedBoostersSummary() {
        if (selectedBoosters.isEmpty()) {
            return "None";
        }

        List<String> boosterNames = new ArrayList<>();
        for (Booster booster : selectedBoosters) {
            boosterNames.add(booster.getName());
        }
        return selectedBoosters.size() + " (" + String.join(", ", boosterNames) + ")";
    }

    /**
     * extracts a short rocket name from a saved summary
     */
    private String getHistoryRocketName(LaunchResult launchResult) {
        String launcher = getSummaryValue(launchResult.getRocketSummary(), "Launcher: ");
        String capsule = getSummaryValue(launchResult.getRocketSummary(), "Capsule: ");

        if (launcher.isBlank() && capsule.isBlank()) {
            return "Rocket";
        }

        if (capsule.isBlank()) {
            return launcher;
        }

        if (launcher.isBlank()) {
            return capsule;
        }

        return launcher + " + " + capsule;
    }

    /**
     * extracts one labeled value from a multiline summary
     */
    private String getSummaryValue(String summary, String label) {
        for (String line : summary.split("\\n")) {
            if (line.startsWith(label)) {
                return line.substring(label.length());
            }
        }
        return "";
    }

    /**
     * formats costs with a stable decimal separator
     */
    private String formatCost(double costEuros) {
        return String.format(Locale.US, "%,.2f EUR", costEuros);
    }

    private String formatDecimal(double value) {
        return String.format(Locale.US, "%,.2f", value);
    }

    private String formatHistoryIndex(int index) {
        return String.format(Locale.US, "%02d | ", index);
    }

    private String section(String title) {
        return "---------- " + title.toUpperCase(Locale.US) + " ----------";
    }

    private void printScreenTitle(String title) {
        String formattedTitle = title.toUpperCase(Locale.US);
        int width = Math.max(32, formattedTitle.length() + 8);
        int leftPadding = (width - formattedTitle.length()) / 2;
        int rightPadding = width - formattedTitle.length() - leftPadding;
        String border = "=".repeat(width);

        System.out.println(border);
        System.out.println(" ".repeat(leftPadding) + formattedTitle + " ".repeat(rightPadding));
        System.out.println(border);
        System.out.println();
    }

    /**
     * displays a blocking message screen
     */
    private void showMessage(String title, String message) {
        clearScreen();
        printHeader();
        printScreenTitle(title);
        System.out.println(message);
        System.out.println();
        System.out.println("Press Enter to go back.");
        if (scanner.hasNextLine()) {
            scanner.nextLine();
        }
    }

    /**
     * reads one key press from the terminal
     */
    private int readKey() {
        try {
            int first = System.in.read();

            if (first == -1) {
                return KEY_END;
            }

            if (first == 13 || first == 10) {
                return KEY_ENTER;
            }

            if (first == 27 && System.in.read() == 91) {
                // reads terminal escape sequences for arrow keys
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

    /**
     * clears the terminal viewport
     */
    private void clearScreen() {
        System.out.print(CLEAR_SCREEN);
        System.out.flush();
    }

    /**
     * prints the application title
     */
    private void printHeader() {
        System.out.println(APP_TITLE);
        System.out.println();
    }

    /**
     * enables immediate keyboard input
     */
    private void enableRawMode() {
        // lets the menu react without waiting for enter
        runTerminalCommand("stty", "-echo", "-icanon", "min", "1", "time", "0");
    }

    /**
     * restores normal terminal input
     */
    private void disableRawMode() {
        runTerminalCommand("stty", "echo", "icanon");
    }

    /**
     * runs a terminal control command
     */
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

    /**
     * formats booleans for the console
     */
    private String formatBoolean(boolean value) {
        return value ? "Yes" : "No";
    }

    /**
     * reads a strictly positive number
     */
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

    /**
     * reads a yes or no answer
     */
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

    /**
     * reads a non blank text value
     */
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
