package domain.launcher;

public abstract class Launcher {
    private final String name;
    private final boolean crewed;
    private final int maxBoosters;
    private final double maxFuelTons;
    private final double payloadCapacityTons;
    private final double priceMillionEuros;

    protected Launcher(String name, boolean crewed, int maxBoosters, double maxFuelTons, double payloadCapacityTons, double priceMillionEuros) {
        this.name = name;
        this.crewed = crewed;
        this.maxBoosters = maxBoosters;
        this.maxFuelTons = maxFuelTons;
        this.payloadCapacityTons = payloadCapacityTons;
        this.priceMillionEuros = priceMillionEuros;
    }

    public String getName() { return name; }
    public boolean isCrewed() { return crewed; }
    public int getMaxBoosters() { return maxBoosters; }
    public double getMaxFuelTons() { return maxFuelTons; }
    public double getPayloadCapacityTons() { return payloadCapacityTons; }
    public double getPriceMillionEuros() { return priceMillionEuros; }

    public abstract String getLaunchProfile();
}
