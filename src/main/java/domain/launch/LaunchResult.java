package domain.launch;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LaunchResult {
    private static final DateTimeFormatter DISPLAY_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final LocalDateTime date;
    private final String rocketSummary;
    private final String missionName;
    private final boolean success;
    private final String reason;
    private final double fuelRequiredTons;
    private final double totalCostEuros;

    public LaunchResult(LocalDateTime date, String rocketSummary, String missionName, boolean success, String reason, double fuelRequiredTons, double totalCostEuros) {
        this.date = date;
        this.rocketSummary = rocketSummary;
        this.missionName = missionName;
        this.success = success;
        this.reason = reason;
        this.fuelRequiredTons = fuelRequiredTons;
        this.totalCostEuros = totalCostEuros;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public String getRocketSummary() {
        return rocketSummary;
    }

    public String getMissionName() {
        return missionName;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getReason() {
        return reason;
    }

    public double getFuelRequiredTons() {
        return fuelRequiredTons;
    }

    public double getTotalCostEuros() {
        return totalCostEuros;
    }

    public String getVerdict() {
        return success ? "Success" : "Failure";
    }

    public String getFormattedDate() {
        return date.format(DISPLAY_DATE_FORMATTER);
    }

    public String getSummary() {
        return "Date: " + getFormattedDate()
                + "\nMission: " + missionName
                + "\nResult: " + getVerdict()
                + "\nReason: " + reason
                + "\nFuel required: " + fuelRequiredTons + " t"
                + "\nTotal cost: " + totalCostEuros + " EUR"
                + "\nRocket:\n" + rocketSummary;
    }
}
