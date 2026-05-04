package domain.mission;

import domain.rocket.Rocket;

public class MarsMission extends Mission {
    public MarsMission() {
        super("Mars", true, 225000000, "12 to 18 months", 0.000015);
    }

    @Override
    public String getObjective() {
        return "Reach Mars transfer trajectory";
    }

    @Override
    public double calculateFuelRequiredTons(Rocket rocket) {
        return calculateStandardFuelRequiredTons(rocket);
    }
}
