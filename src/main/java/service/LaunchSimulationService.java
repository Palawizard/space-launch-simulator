package service;

import domain.mission.Mission;
import domain.rocket.Rocket;

public class LaunchSimulationService {
    public static final double PRICE_KEROSENE_PER_TON = 1200;

    public double calculateFuelRequiredTons(Rocket rocket, Mission mission) {
        return (rocket.getTotalMassTons() * mission.getDistanceKilometers() * mission.getFuelCoefficient()) / 1000;
    }

    public double calculateLaunchCostEuros(Rocket rocket, Mission mission) {
        double rocketPriceEuros = rocket.getTotalPriceMillionEuros() * 1_000_000;
        double fuelPriceEuros = calculateFuelRequiredTons(rocket, mission) * PRICE_KEROSENE_PER_TON;
        return rocketPriceEuros + fuelPriceEuros;
    }
}
