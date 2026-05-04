package domain.mission;

import domain.rocket.Rocket;

public class MoonMission extends Mission {
    public MoonMission() {
        super("Moon", true, 400000, "8 to 10 days", 0.005);
    }

    @Override
    public String getObjective() {
        return "Reach lunar orbit";
    }

    @Override
    public double calculateFuelRequiredTons(Rocket rocket) {
        return calculateStandardFuelRequiredTons(rocket);
    }
}
