package exception;

/**
 * failure when a launcher cannot carry crew
 */
public class IncompatibleCrewedLauncherException extends LaunchException {
    public IncompatibleCrewedLauncherException() {
        super("Launcher incompatible with crewed mission");
    }
}
