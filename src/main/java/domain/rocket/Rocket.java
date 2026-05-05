package domain.rocket;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import domain.booster.Booster;
import domain.capsule.Capsule;
import domain.launcher.Launcher;

/**
 * configured rocket ready for mission checks
 */
public class Rocket {
    private final Launcher launcher;
    private final Capsule capsule;
    private final List<Booster> boosters;

    public Rocket(Launcher launcher, Capsule capsule, List<Booster> boosters) {
        this.launcher = launcher;
        this.capsule = capsule;
        this.boosters = new ArrayList<>(boosters);
    }

    public Launcher getLauncher() {
        return launcher;
    }

    public Capsule getCapsule() {
        return capsule;
    }

    public List<Booster> getBoosters() {
        return Collections.unmodifiableList(boosters);
    }

    public void addBooster(Booster booster) {
        boosters.add(booster);
    }

    public void addBoosters(List<Booster> boosters) {
        this.boosters.addAll(boosters);
    }

    public void addBoosters(Booster firstBooster, Booster secondBooster) {
        boosters.add(firstBooster);
        boosters.add(secondBooster);
    }

    public double getTotalMassTons() {
        double total = capsule.getMassTons();
        for (Booster booster : boosters) {
            total += booster.getMassTons();
        }
        return total;
    }

    public double getTotalPriceMillionEuros() {
        double total = launcher.getPriceMillionEuros() + capsule.getPriceMillionEuros();
        for (Booster booster : boosters) {
            total += booster.getPriceMillionEuros();
        }
        return total;
    }

    public String getSummary() {
        return section("launcher")
                + "\nLauncher: " + launcher.getName()
                + "\nLauncher profile: " + launcher.getLaunchProfile()
                + "\nLauncher max boosters: " + launcher.getMaxBoosters()
                + "\nLauncher max fuel: " + formatDecimal(launcher.getMaxFuelTons()) + " t"
                + "\nLauncher payload capacity: " + formatDecimal(launcher.getPayloadCapacityTons()) + " t"
                + "\n\n" + section("capsule")
                + "\nCapsule: " + capsule.getName()
                + "\nCapsule role: " + capsule.getMissionRole()
                + "\nCapsule crewed: " + formatBoolean(capsule.isCrewed())
                + "\nCapsule max occupants: " + capsule.getMaxOccupants()
                + "\n\n" + section("boosters")
                + "\nBoosters: " + getBoosterSummary()
                + "\n\n" + section("totals")
                + "\nTotal mass: " + formatDecimal(getTotalMassTons()) + " t"
                + "\nTotal price: " + formatDecimal(getTotalPriceMillionEuros()) + " M EUR";
    }

    private String getBoosterSummary() {
        if (boosters.isEmpty()) {
            return "None";
        }

        List<String> boosterNames = new ArrayList<>();
        for (Booster booster : boosters) {
            boosterNames.add(booster.getName());
        }
        return boosters.size() + " (" + String.join(", ", boosterNames) + ")";
    }

    private String formatBoolean(boolean value) {
        return value ? "Yes" : "No";
    }

    private String formatDecimal(double value) {
        return String.format(Locale.US, "%,.2f", value);
    }

    private String section(String title) {
        return "---------- " + title.toUpperCase(Locale.US) + " ----------";
    }
}
