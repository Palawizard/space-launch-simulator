package exception;

/**
 * failure when rocket mass exceeds payload capacity
 */
public class PayloadCapacityExceededException extends LaunchException {
    public PayloadCapacityExceededException() {
        super("Payload capacity exceeded");
    }
}
