package ui;

import domain.booster.Booster;
import domain.capsule.Capsule;
import domain.launch.LaunchResult;
import domain.launcher.Launcher;
import domain.mission.CustomMission;
import domain.mission.Mission;
import domain.rocket.Rocket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import persistence.LaunchHistoryService;
import service.ComponentCatalog;
import service.LaunchSimulationService;
import service.RocketConfigurationService;

public class ConsoleInterface {
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
            showMainMenu();
            handleMainMenuChoice(readMenuChoice());
        }
    }

    private void showMainMenu() {
        System.out.println();
        System.out.println("Space Launch Simulator");
        System.out.println("1. Configure rocket");
        System.out.println("2. Choose mission");
        System.out.println("3. Run launch simulation");
        System.out.println("4. Display history");
        System.out.println("5. Exit");
        System.out.print("Choose an option: ");
    }

    private void handleMainMenuChoice(int choice) {
        switch (choice) {
            case 1:
                selectLauncher();
                selectCapsule();
                selectBoosters();
                break;
            case 2:
                selectMission();
                break;
            case 3:
                runLaunchSimulation();
                break;
            case 4:
                displayHistory();
                break;
            case 5:
                running = false;
                System.out.println("Goodbye.");
                break;
            default:
                System.out.println("Invalid option.");
                break;
        }
    }

    private void selectLauncher() {
        List<Launcher> launchers = componentCatalog.getLaunchers();
        System.out.println();
        System.out.println("Available launchers");

        for (int index = 0; index < launchers.size(); index++) {
            Launcher launcher = launchers.get(index);
            System.out.println((index + 1) + ". " + launcher.getName()
                    + " | payload " + launcher.getPayloadCapacityTons() + " t"
                    + " | fuel " + launcher.getMaxFuelTons() + " t"
                    + " | max boosters " + launcher.getMaxBoosters()
                    + " | price " + launcher.getPriceMillionEuros() + " M EUR");
        }

        System.out.print("Choose a launcher: ");
        int choice = readChoice(1, launchers.size());
        selectedLauncher = launchers.get(choice - 1);
        System.out.println("Selected launcher: " + selectedLauncher.getName());
    }

    private void selectCapsule() {
        List<Capsule> capsules = componentCatalog.getCapsules();
        System.out.println();
        System.out.println("Available capsules");

        for (int index = 0; index < capsules.size(); index++) {
            Capsule capsule = capsules.get(index);
            System.out.println((index + 1) + ". " + capsule.getName()
                    + " | crewed " + formatBoolean(capsule.isCrewed())
                    + " | occupants " + capsule.getMaxOccupants()
                    + " | mass " + capsule.getMassTons() + " t"
                    + " | price " + capsule.getPriceMillionEuros() + " M EUR");
        }

        System.out.print("Choose a capsule: ");
        int choice = readChoice(1, capsules.size());
        selectedCapsule = capsules.get(choice - 1);
        System.out.println("Selected capsule: " + selectedCapsule.getName());
    }

    private void selectBoosters() {
        selectedBoosters = new ArrayList<>();
        List<Booster> boosters = componentCatalog.getBoosters();
        System.out.println();
        System.out.println("Available boosters");

        for (int index = 0; index < boosters.size(); index++) {
            Booster booster = boosters.get(index);
            System.out.println((index + 1) + ". " + booster.getName()
                    + " | thrust " + booster.getAdditionalThrustKilonewtons() + " kN"
                    + " | mass " + booster.getMassTons() + " t"
                    + " | price " + booster.getPriceMillionEuros() + " M EUR");
        }

        System.out.print("How many boosters do you want to add? ");
        int boosterCount = readNonNegativeInteger();

        for (int index = 0; index < boosterCount; index++) {
            System.out.print("Choose booster " + (index + 1) + ": ");
            int choice = readChoice(1, boosters.size());
            selectedBoosters.add(boosters.get(choice - 1));
        }

        System.out.println("Selected boosters: " + selectedBoosters.size());
        try {
            configuredRocket = rocketConfigurationService.buildRocket(selectedLauncher, selectedCapsule, selectedBoosters);
            System.out.println();
            System.out.println(configuredRocket.getSummary());
        } catch (IllegalArgumentException exception) {
            configuredRocket = null;
            System.out.println(exception.getMessage());
        }
    }

    private void selectMission() {
        List<Mission> missions = componentCatalog.getMissions();
        System.out.println();
        System.out.println("Available missions");

        for (int index = 0; index < missions.size() - 1; index++) {
            Mission mission = missions.get(index);
            System.out.println((index + 1) + ". " + mission.getName()
                    + " | crew required " + formatBoolean(mission.isCrewRequired())
                    + " | distance " + mission.getDistanceKilometers() + " km"
                    + " | duration " + mission.getDuration()
                    + " | fuel coefficient " + mission.getFuelCoefficient());
        }

        System.out.println(missions.size() + ". Custom mission");
        System.out.print("Choose a mission: ");
        int choice = readChoice(1, missions.size());
        if (choice == missions.size()) {
            selectedMission = createCustomMission();
        } else {
            selectedMission = missions.get(choice - 1);
        }
        System.out.println("Selected mission: " + selectedMission.getName());
    }

    private Mission createCustomMission() {
        System.out.print("Mission name: ");
        String name = readRequiredText();
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
            System.out.println("Configure a rocket before running a simulation.");
            return;
        }

        if (selectedMission == null) {
            System.out.println("Choose a mission before running a simulation.");
            return;
        }

        LaunchResult launchResult = launchSimulationService.runLaunch(configuredRocket, selectedMission);
        System.out.println();
        System.out.println(launchResult.getSummary());
    }

    private void displayHistory() {
        List<LaunchResult> history = launchHistoryService.getHistory();
        System.out.println();
        System.out.println("Launch history");

        if (history.isEmpty()) {
            System.out.println("No launch history yet.");
            return;
        }

        for (int index = 0; index < history.size(); index++) {
            System.out.println();
            System.out.println("Launch " + (index + 1));
            System.out.println(history.get(index).getSummary());
        }
    }

    private String formatBoolean(boolean value) {
        return value ? "Yes" : "No";
    }

    private int readMenuChoice() {
        return readChoice(1, 5);
    }

    private int readChoice(int minimum, int maximum) {
        while (true) {
            try {
                int value = Integer.parseInt(scanner.nextLine());
                if (value >= minimum && value <= maximum) {
                    return value;
                }
            } catch (NumberFormatException exception) {
                System.out.print("Enter a valid number: ");
                continue;
            }
            System.out.print("Enter a number between " + minimum + " and " + maximum + ": ");
        }
    }

    private int readNonNegativeInteger() {
        while (true) {
            try {
                int value = Integer.parseInt(scanner.nextLine());
                if (value >= 0) {
                    return value;
                }
            } catch (NumberFormatException exception) {
                System.out.print("Enter a valid number: ");
                continue;
            }
            System.out.print("Enter zero or a positive number: ");
        }
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
