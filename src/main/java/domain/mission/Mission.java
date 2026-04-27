package domain.mission;

public abstract class Mission {
    private final String name;
    private final boolean crewRequired;
    private final double distanceKilometers;
    private final String duration;
    private final double fuelCoefficient;

    protected Mission(String name, boolean crewRequired, double distanceKilometers, String duration, double fuelCoefficient) {
        this.name = name;
        this.crewRequired = crewRequired;
        this.distanceKilometers = distanceKilometers;
        this.duration = duration;
        this.fuelCoefficient = fuelCoefficient;
    }

    public String getName() { return name; }
    public boolean isCrewRequired() { return crewRequired; }
    public double getDistanceKilometers() { return distanceKilometers; }
    public String getDuration() { return duration; }
    public double getFuelCoefficient() { return fuelCoefficient; }

    public abstract String getObjective();
}
