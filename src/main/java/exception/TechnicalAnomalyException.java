package exception;

/**
 * failure caused by a random technical anomaly
 */
public class TechnicalAnomalyException extends LaunchException {
    public TechnicalAnomalyException() {
        super("Unexpected technical anomaly");
    }
}
