package domain.mission;

import domain.rocket.Rocket;

/**
 * mission defined from user input
 */
public class CustomMission extends Mission {
    public CustomMission(String name, boolean crewRequired, double distanceKilometers, String duration, double fuelCoefficient) {
        super(name, crewRequired, distanceKilometers, duration, fuelCoefficient);
    }

    @Override
    public String getObjective() {
        return "Complete a custom mission";
    }

    @Override
    public double calculateFuelRequiredTons(Rocket rocket) {
        return calculateStandardFuelRequiredTons(rocket);
    }
}
