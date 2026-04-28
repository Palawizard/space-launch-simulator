package service;

import domain.mission.Mission;
import domain.rocket.Rocket;
import exception.IncompatibleCrewedMissionException;
import exception.InsufficientFuelException;
import exception.LaunchException;
import exception.PayloadCapacityExceededException;
import exception.TechnicalAnomalyException;
import exception.TooManyBoostersException;
import java.util.Random;

public class LaunchSimulationService {
    public static final double PRICE_KEROSENE_PER_TON = 1200;
    public static final double RANDOM_FAILURE_PROBABILITY = 0.05;

    private final Random random;

    public LaunchSimulationService() {
        this(new Random());
    }

    public LaunchSimulationService(Random random) {
        this.random = random;
    }

    public double calculateFuelRequiredTons(Rocket rocket, Mission mission) {
        return (rocket.getTotalMassTons() * mission.getDistanceKilometers() * mission.getFuelCoefficient()) / 1000;
    }

    public double calculateLaunchCostEuros(Rocket rocket, Mission mission) {
        double rocketPriceEuros = rocket.getTotalPriceMillionEuros() * 1_000_000;
        double fuelPriceEuros = calculateFuelRequiredTons(rocket, mission) * PRICE_KEROSENE_PER_TON;
        return rocketPriceEuros + fuelPriceEuros;
    }

    public void validateLaunchConditions(Rocket rocket, Mission mission) throws LaunchException {
        if (calculateFuelRequiredTons(rocket, mission) > rocket.getLauncher().getMaxFuelTons()) {
            throw new InsufficientFuelException();
        }

        if (rocket.getTotalMassTons() > rocket.getLauncher().getPayloadCapacityTons()) {
            throw new PayloadCapacityExceededException();
        }

        if (rocket.getBoosters().size() > rocket.getLauncher().getMaxBoosters()) {
            throw new TooManyBoostersException();
        }

        if (mission.isCrewRequired() && (!rocket.getCapsule().isCrewed() || rocket.getCapsule().getMaxOccupants() <= 0)) {
            throw new IncompatibleCrewedMissionException();
        }
    }

    public void validateRandomLaunchRisk() throws TechnicalAnomalyException {
        if (random.nextDouble() < RANDOM_FAILURE_PROBABILITY) {
            throw new TechnicalAnomalyException();
        }
    }
}
