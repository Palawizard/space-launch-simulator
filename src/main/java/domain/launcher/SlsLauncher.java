package domain.launcher;

public class SlsLauncher extends Launcher {
    public SlsLauncher() {
        super("SLS", true, 2, 2600, 130, 2000);
    }

    @Override
    public String getLaunchProfile() {
        return "Heavy crewed deep space launcher";
    }
}
