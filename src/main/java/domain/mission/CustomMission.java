package domain.mission;

public class CustomMission extends Mission {
    public CustomMission(String name, boolean crewRequired, double distanceKilometers, String duration, double fuelCoefficient) {
        super(name, crewRequired, distanceKilometers, duration, fuelCoefficient);
    }

    @Override
    public String getObjective() {
        return "Complete a custom mission";
    }
}
