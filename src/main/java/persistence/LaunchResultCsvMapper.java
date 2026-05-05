package persistence;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import domain.launch.LaunchResult;

/**
 * maps launch results to csv rows
 */
public class LaunchResultCsvMapper {
    public static final String HEADER = "date;mission;success;reason;fuelRequiredTons;totalCostEuros;rocketSummary";

    /**
     * serializes a launch result into one csv row
     */
    public String toCsvLine(LaunchResult launchResult) {
        return String.join(";",
                escape(launchResult.getDate().toString()),
                escape(launchResult.getMissionName()),
                Boolean.toString(launchResult.isSuccess()),
                escape(launchResult.getReason()),
                Double.toString(launchResult.getFuelRequiredTons()),
                Double.toString(launchResult.getTotalCostEuros()),
                escape(launchResult.getRocketSummary())
        );
    }

    /**
     * rebuilds a launch result from one csv row
     */
    public LaunchResult fromCsvLine(String csvLine) {
        List<String> values = splitCsvLine(csvLine);
        return new LaunchResult(
                LocalDateTime.parse(values.get(0)),
                unescape(values.get(6)),
                unescape(values.get(1)),
                Boolean.parseBoolean(values.get(2)),
                unescape(values.get(3)),
                Double.parseDouble(values.get(4)),
                Double.parseDouble(values.get(5))
        );
    }

    /**
     * protects separators quotes and line breaks
     */
    private String escape(String value) {
        return "\"" + value.replace("\\", "\\\\").replace("\"", "\"\"").replace("\n", "\\n") + "\"";
    }

    /**
     * restores escaped line breaks and backslashes
     */
    private String unescape(String value) {
        return value.replace("\\n", "\n").replace("\\\\", "\\");
    }

    /**
     * splits one csv row while respecting quoted fields
     */
    private List<String> splitCsvLine(String csvLine) {
        List<String> values = new ArrayList<>();
        StringBuilder currentValue = new StringBuilder();
        boolean quoted = false;

        // preserves separators inside quoted values
        for (int index = 0; index < csvLine.length(); index++) {
            char character = csvLine.charAt(index);

            if (character == '"') {
                if (quoted && index + 1 < csvLine.length() && csvLine.charAt(index + 1) == '"') {
                    currentValue.append('"');
                    index++;
                } else {
                    quoted = !quoted;
                }
            } else if (character == ';' && !quoted) {
                values.add(currentValue.toString());
                currentValue.setLength(0);
            } else {
                currentValue.append(character);
            }
        }

        values.add(currentValue.toString());
        return values;
    }
}
