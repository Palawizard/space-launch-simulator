package domain.mission;

public class EarthOrbitMission extends Mission {
    public EarthOrbitMission() {
        super("Earth orbit", false, 400, "A few hours", 1.0);
    }

    @Override
    public String getObjective() {
        return "Reach low Earth orbit";
    }
}
