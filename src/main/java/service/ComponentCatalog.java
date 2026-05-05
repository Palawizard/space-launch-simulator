package service;

import java.util.List;

import domain.booster.Booster;
import domain.capsule.ApolloCapsule;
import domain.capsule.Capsule;
import domain.capsule.CargoDragonCapsule;
import domain.capsule.CrewDragonCapsule;
import domain.capsule.OrionCapsule;
import domain.launcher.Ariane5Launcher;
import domain.launcher.Falcon9Launcher;
import domain.launcher.Launcher;
import domain.launcher.SaturnVLauncher;
import domain.launcher.SlsLauncher;
import domain.mission.EarthOrbitMission;
import domain.mission.EuropaProbeMission;
import domain.mission.IssMission;
import domain.mission.MarsMission;
import domain.mission.Mission;
import domain.mission.MoonMission;

/**
 * catalog of available simulator components
 */
public class ComponentCatalog {
    public List<Launcher> getLaunchers() {
        return List.of(
                new SaturnVLauncher(),
                new Ariane5Launcher(),
                new Falcon9Launcher(),
                new SlsLauncher()
        );
    }

    public List<Capsule> getCapsules() {
        return List.of(
                new OrionCapsule(),
                new CrewDragonCapsule(),
                new ApolloCapsule(),
                new CargoDragonCapsule()
        );
    }

    public List<Booster> getBoosters() {
        return List.of(
                new Booster("EAP (Ariane)", 6470, 270, 30),
                new Booster("SRB (Shuttle)", 12500, 590, 55),
                new Booster("BE-3", 490, 25, 12)
        );
    }

    public List<Mission> getMissions() {
        return List.of(
                new EarthOrbitMission(),
                new IssMission(),
                new MoonMission(),
                new MarsMission(),
                new EuropaProbeMission()
        );
    }
}
