package domain.launcher;

/**
 * saturn launcher preset
 */
public class SaturnVLauncher extends Launcher {
    public SaturnVLauncher() {
        super("Saturn V", true, 0, 2700, 140, 1500);
    }

    @Override
    public String getLaunchProfile() {
        return "Heavy crewed lunar launcher";
    }
}
