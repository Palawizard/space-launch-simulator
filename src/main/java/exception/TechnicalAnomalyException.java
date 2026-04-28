package exception;

public class TechnicalAnomalyException extends LaunchException {
    public TechnicalAnomalyException() {
        super("Unexpected technical anomaly");
    }
}
