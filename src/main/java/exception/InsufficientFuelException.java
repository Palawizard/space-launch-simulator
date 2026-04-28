package exception;

public class InsufficientFuelException extends LaunchException {
    public InsufficientFuelException() {
        super("Insufficient fuel");
    }
}
