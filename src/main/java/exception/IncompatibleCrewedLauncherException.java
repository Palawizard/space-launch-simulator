package exception;

public class IncompatibleCrewedLauncherException extends LaunchException {
    public IncompatibleCrewedLauncherException() {
        super("Launcher incompatible with crewed mission");
    }
}
