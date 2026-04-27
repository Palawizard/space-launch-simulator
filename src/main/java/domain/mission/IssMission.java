package domain.mission;

public class IssMission extends Mission {
    public IssMission() {
        super("ISS", true, 400, "12 h to a few days", 1.2);
    }

    @Override
    public String getObjective() {
        return "Dock with the International Space Station";
    }
}
