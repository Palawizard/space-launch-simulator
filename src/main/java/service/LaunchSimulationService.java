package service;

import domain.launch.LaunchResult;
import domain.mission.Mission;
import domain.rocket.Rocket;
import exception.IncompatibleCrewedLauncherException;
import exception.IncompatibleCrewedMissionException;
import exception.InsufficientFuelException;
import exception.LaunchException;
import exception.PayloadCapacityExceededException;
import exception.TechnicalAnomalyException;
import exception.TooManyBoostersException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Random;
import persistence.LaunchHistoryService;

public class LaunchSimulationService {
    public static final double PRICE_KEROSENE_PER_TON = 1200;
    public static final double RANDOM_FAILURE_PROBABILITY = 0.05;

    private final Random random;
    private final LaunchHistoryService launchHistoryService;

    public LaunchSimulationService() {
        this(new Random(), null);
    }

    public LaunchSimulationService(Random random) {
        this(random, null);
    }

    public LaunchSimulationService(LaunchHistoryService launchHistoryService) {
        this(new Random(), launchHistoryService);
    }

    public LaunchSimulationService(Random random, LaunchHistoryService launchHistoryService) {
        this.random = random;
        this.launchHistoryService = launchHistoryService;
    }

    public double calculateFuelRequiredTons(Rocket rocket, Mission mission) {
        return mission.calculateFuelRequiredTons(rocket);
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

        if (mission.isCrewRequired() && !rocket.getLauncher().isCrewed()) {
            throw new IncompatibleCrewedLauncherException();
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

    public LaunchResult runLaunch(Rocket rocket, Mission mission) {
        double fuelRequiredTons = calculateFuelRequiredTons(rocket, mission);
        double totalCostEuros = calculateLaunchCostEuros(rocket, mission);
        LaunchResult launchResult;

        try {
            validateLaunchConditions(rocket, mission);
            validateRandomLaunchRisk();
            launchResult = createLaunchResult(rocket, mission, true, "Launch successful", fuelRequiredTons, totalCostEuros);
        } catch (LaunchException exception) {
            launchResult = createLaunchResult(rocket, mission, false, exception.getMessage(), fuelRequiredTons, totalCostEuros);
        }

        saveLaunchResult(launchResult);
        return launchResult;
    }

    private LaunchResult createLaunchResult(Rocket rocket, Mission mission, boolean success, String reason, double fuelRequiredTons, double totalCostEuros) {
        return new LaunchResult(
                LocalDateTime.now(),
                rocket.getSummary(),
                mission.getName(),
                success,
                reason,
                fuelRequiredTons,
                totalCostEuros
        );
    }

    private void saveLaunchResult(LaunchResult launchResult) {
        if (launchHistoryService == null) {
            return;
        }

        try {
            launchHistoryService.addResult(launchResult);
            launchHistoryService.saveHistory();
        } catch (IOException exception) {
            throw new IllegalStateException("Unable to save launch history", exception);
        }
    }
}
