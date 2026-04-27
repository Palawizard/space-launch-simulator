package domain.rocket;

import domain.booster.Booster;
import domain.capsule.Capsule;
import domain.launcher.Launcher;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
        return "Launcher: " + launcher.getName()
                + "\nCapsule: " + capsule.getName()
                + "\nBoosters: " + boosters.size()
                + "\nTotal mass: " + getTotalMassTons() + " t"
                + "\nTotal price: " + getTotalPriceMillionEuros() + " M EUR";
    }
}
