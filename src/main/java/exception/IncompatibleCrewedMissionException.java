package exception;

public class IncompatibleCrewedMissionException extends LaunchException {
    public IncompatibleCrewedMissionException() {
        super("Capsule incompatible with crewed mission");
    }
}
