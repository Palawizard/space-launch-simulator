package service;

import domain.mission.Mission;
import domain.rocket.Rocket;

public class LaunchSimulationService {
    public double calculateFuelRequiredTons(Rocket rocket, Mission mission) {
        return (rocket.getTotalMassTons() * mission.getDistanceKilometers() * mission.getFuelCoefficient()) / 1000;
    }
}
