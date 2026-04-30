package ui;

import domain.capsule.Capsule;
import domain.launcher.Launcher;
import java.util.List;
import java.util.Scanner;
import service.ComponentCatalog;

public class ConsoleInterface {
    private final Scanner scanner;
    private final ComponentCatalog componentCatalog;
    private Launcher selectedLauncher;
    private Capsule selectedCapsule;
    private boolean running;

    public ConsoleInterface() {
        this.scanner = new Scanner(System.in);
        this.componentCatalog = new ComponentCatalog();
        this.running = true;
    }

    public void start() {
        while (running) {
            showMainMenu();
            handleMainMenuChoice(scanner.nextLine());
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

    private void handleMainMenuChoice(String choice) {
        switch (choice) {
            case "1":
                selectLauncher();
                selectCapsule();
                break;
            case "2":
            case "3":
            case "4":
                System.out.println("This feature is not available yet.");
                break;
            case "5":
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
        int choice = Integer.parseInt(scanner.nextLine());
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
        int choice = Integer.parseInt(scanner.nextLine());
        selectedCapsule = capsules.get(choice - 1);
        System.out.println("Selected capsule: " + selectedCapsule.getName());
    }

    private String formatBoolean(boolean value) {
        return value ? "Yes" : "No";
    }
}
