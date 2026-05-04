package domain.mission;

import domain.rocket.Rocket;

public class EarthOrbitMission extends Mission {
    public EarthOrbitMission() {
        super("Earth orbit", false, 400, "A few hours", 1.0);
    }

    @Override
    public String getObjective() {
        return "Reach low Earth orbit";
    }

    @Override
    public double calculateFuelRequiredTons(Rocket rocket) {
        return calculateStandardFuelRequiredTons(rocket);
    }
}
