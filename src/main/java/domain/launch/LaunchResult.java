package domain.launch;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * result produced by a launch simulation
 */
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
        return section("launch details")
                + "\nDate: " + getFormattedDate()
                + "\nMission: " + missionName
                + "\nResult: " + getVerdict()
                + "\nReason: " + reason
                + "\n\n" + section("cost and fuel")
                + "\nFuel required: " + formatDecimal(fuelRequiredTons) + " t"
                + "\nTotal cost: " + formatCurrency(totalCostEuros) + " EUR"
                + "\n\n" + rocketSummary;
    }

    private String formatDecimal(double value) {
        return String.format(Locale.US, "%,.2f", value);
    }

    private String formatCurrency(double value) {
        return String.format(Locale.US, "%,.2f", value);
    }

    private String section(String title) {
        return "---------- " + title.toUpperCase(Locale.US) + " ----------";
    }
}
