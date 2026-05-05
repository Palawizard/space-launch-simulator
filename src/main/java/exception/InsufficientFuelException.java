package exception;

/**
 * failure when required fuel exceeds capacity
 */
public class InsufficientFuelException extends LaunchException {
    public InsufficientFuelException() {
        super("Insufficient fuel");
    }
}
