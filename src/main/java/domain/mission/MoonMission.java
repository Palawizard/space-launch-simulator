package domain.mission;

public class MoonMission extends Mission {
    public MoonMission() {
        super("Moon", true, 400000, "8 to 10 days", 0.005);
    }

    @Override
    public String getObjective() {
        return "Reach lunar orbit";
    }
}
