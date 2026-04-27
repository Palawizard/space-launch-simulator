package service;

import domain.booster.Booster;
import domain.capsule.Capsule;
import domain.launcher.Launcher;
import domain.rocket.Rocket;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RocketConfigurationService {
    public Rocket buildRocket(Launcher launcher, Capsule capsule) {
        return buildRocket(launcher, capsule, new ArrayList<>());
    }

    public Rocket buildRocket(Launcher launcher, Capsule capsule, List<Booster> boosters) {
        Objects.requireNonNull(launcher, "Launcher is required");
        Objects.requireNonNull(capsule, "Capsule is required");
        Objects.requireNonNull(boosters, "Boosters are required");
        validateBoosterLimit(launcher, boosters);
        return new Rocket(launcher, capsule, boosters);
    }

    public boolean canUseBoosters(Launcher launcher, List<Booster> boosters) {
        Objects.requireNonNull(launcher, "Launcher is required");
        Objects.requireNonNull(boosters, "Boosters are required");
        return boosters.size() <= launcher.getMaxBoosters();
    }

    private void validateBoosterLimit(Launcher launcher, List<Booster> boosters) {
        if (!canUseBoosters(launcher, boosters)) {
            throw new IllegalArgumentException("Too many boosters for " + launcher.getName());
        }
    }
}
