package exception;

/**
 * failure when booster count exceeds launcher limits
 */
public class TooManyBoostersException extends LaunchException {
    public TooManyBoostersException() {
        super("Too many boosters");
    }
}
