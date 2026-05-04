package app;

import domain.booster.Booster;
import domain.capsule.Capsule;
import domain.launch.LaunchResult;
import domain.launcher.Launcher;
import domain.mission.Mission;
import domain.rocket.Rocket;
import exception.LaunchException;
import java.io.IOException;
import java.util.List;
import persistence.LaunchHistoryService;
import service.ComponentCatalog;
import service.LaunchSimulationService;
import service.RocketConfigurationService;

public class Simulator {
    private static Simulator instance;

    private final ComponentCatalog componentCatalog;
    private final RocketConfigurationService rocketConfigurationService;
    private final LaunchSimulationService launchSimulationService;
    private final LaunchHistoryService launchHistoryService;

    public static synchronized Simulator getInstance() {
        return getInstance(new LaunchHistoryService());
    }

    public static synchronized Simulator getInstance(LaunchHistoryService launchHistoryService) {
        if (instance == null) {
            instance = new Simulator(launchHistoryService);
        }
        return instance;
    }

    private Simulator(LaunchHistoryService launchHistoryService) {
        this.componentCatalog = new ComponentCatalog();
        this.rocketConfigurationService = new RocketConfigurationService();
        this.launchSimulationService = new LaunchSimulationService(launchHistoryService);
        this.launchHistoryService = launchHistoryService;
    }

    public List<Launcher> getLaunchers() {
        return componentCatalog.getLaunchers();
    }

    public List<Capsule> getCapsules() {
        return componentCatalog.getCapsules();
    }

    public List<Booster> getBoosters() {
        return componentCatalog.getBoosters();
    }

    public List<Mission> getMissions() {
        return componentCatalog.getMissions();
    }

    public Rocket buildRocket(Launcher launcher, Capsule capsule, List<Booster> boosters) {
        return rocketConfigurationService.buildRocket(launcher, capsule, boosters);
    }

    public LaunchResult runLaunch(Rocket rocket, Mission mission) {
        return launchSimulationService.runLaunch(rocket, mission);
    }

    public void validateLaunchConditions(Rocket rocket, Mission mission) throws LaunchException {
        launchSimulationService.validateLaunchConditions(rocket, mission);
    }

    public List<LaunchResult> getHistory() {
        return launchHistoryService.getHistory();
    }

    public void loadHistory() {
        try {
            launchHistoryService.loadHistory();
        } catch (IOException exception) {
            throw new IllegalStateException("Unable to load launch history", exception);
        }
    }
}
