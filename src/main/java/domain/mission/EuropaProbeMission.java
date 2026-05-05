package domain.mission;

import domain.rocket.Rocket;

/**
 * mission preset for an uncrewed europa probe
 */
public class EuropaProbeMission extends Mission {
    public EuropaProbeMission() {
        super("Europa probe", false, 628300000, "5 to 7 years", 0.000006);
    }

    @Override
    public String getObjective() {
        return "Send an uncrewed probe to Europa";
    }

    @Override
    public double calculateFuelRequiredTons(Rocket rocket) {
        return calculateStandardFuelRequiredTons(rocket);
    }
}
