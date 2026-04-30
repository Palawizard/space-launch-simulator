package ui;

import java.util.Scanner;

public class ConsoleInterface {
    private final Scanner scanner;
    private boolean running;

    public ConsoleInterface() {
        this.scanner = new Scanner(System.in);
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
}
