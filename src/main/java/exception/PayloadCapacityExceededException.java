package exception;

public class PayloadCapacityExceededException extends LaunchException {
    public PayloadCapacityExceededException() {
        super("Payload capacity exceeded");
    }
}
