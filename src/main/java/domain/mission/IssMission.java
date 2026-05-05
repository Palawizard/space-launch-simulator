package domain.mission;

import domain.rocket.Rocket;

/**
 * mission preset for the international space station
 */
public class IssMission extends Mission {
    public IssMission() {
        super("ISS", true, 400, "12 h to a few days", 1.2);
    }

    @Override
    public String getObjective() {
        return "Dock with the International Space Station";
    }

    @Override
    public double calculateFuelRequiredTons(Rocket rocket) {
        return calculateStandardFuelRequiredTons(rocket);
    }
}
