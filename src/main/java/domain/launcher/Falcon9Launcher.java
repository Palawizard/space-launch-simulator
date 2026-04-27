package domain.launcher;

public class Falcon9Launcher extends Launcher {
    public Falcon9Launcher() {
        super("Falcon 9", true, 0, 500, 22, 60);
    }

    @Override
    public String getLaunchProfile() {
        return "Reusable crewed orbital launcher";
    }
}
