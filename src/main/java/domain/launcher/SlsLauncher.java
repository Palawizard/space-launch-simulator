package domain.launcher;

/**
 * heavy deep space launcher preset
 */
public class SlsLauncher extends Launcher {
    public SlsLauncher() {
        super("SLS", true, 2, 2600, 130, 2000);
    }

    @Override
    public String getLaunchProfile() {
        return "Heavy crewed deep space launcher";
    }
}
