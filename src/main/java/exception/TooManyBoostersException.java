package exception;

public class TooManyBoostersException extends LaunchException {
    public TooManyBoostersException() {
        super("Too many boosters");
    }
}
