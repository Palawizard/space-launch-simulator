package service;

import domain.booster.Booster;
import domain.capsule.Capsule;
import domain.launcher.Launcher;
import domain.rocket.Rocket;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * builds valid rocket configurations
 */
public class RocketConfigurationService {
    /**
     * builds a rocket without boosters
     */
    public Rocket buildRocket(Launcher launcher, Capsule capsule) {
        return buildRocket(launcher, capsule, new ArrayList<>());
    }

    /**
     * builds a rocket after checking required parts
     */
    public Rocket buildRocket(Launcher launcher, Capsule capsule, List<Booster> boosters) {
        Objects.requireNonNull(launcher, "Launcher is required");
        Objects.requireNonNull(capsule, "Capsule is required");
        Objects.requireNonNull(boosters, "Boosters are required");
        validateBoosterLimit(launcher, boosters);
        return new Rocket(launcher, capsule, boosters);
    }

    /**
     * checks booster count against launcher limits
     */
    public boolean canUseBoosters(Launcher launcher, List<Booster> boosters) {
        Objects.requireNonNull(launcher, "Launcher is required");
        Objects.requireNonNull(boosters, "Boosters are required");
        return boosters.size() <= launcher.getMaxBoosters();
    }

    /**
     * rejects configurations with too many boosters
     */
    private void validateBoosterLimit(Launcher launcher, List<Booster> boosters) {
        if (!canUseBoosters(launcher, boosters)) {
            throw new IllegalArgumentException("Too many boosters for " + launcher.getName());
        }
    }
}
