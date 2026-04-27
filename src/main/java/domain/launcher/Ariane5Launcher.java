package domain.launcher;

public class Ariane5Launcher extends Launcher {
    public Ariane5Launcher() {
        super("Ariane 5", false, 2, 700, 20, 180);
    }

    @Override
    public String getLaunchProfile() {
        return "Uncrewed orbital launcher";
    }
}
