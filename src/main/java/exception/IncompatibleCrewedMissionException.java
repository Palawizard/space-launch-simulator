package exception;

/**
 * failure when a capsule cannot support crew
 */
public class IncompatibleCrewedMissionException extends LaunchException {
    public IncompatibleCrewedMissionException() {
        super("Capsule incompatible with crewed mission");
    }
}
