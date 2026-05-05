package exception;

/**
 * base exception for launch failures
 */
public class LaunchException extends Exception {
    public LaunchException(String message) {
        super(message);
    }
}
