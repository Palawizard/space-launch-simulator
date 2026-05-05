package domain.capsule;

/**
 * base model for a spacecraft capsule
 */
public abstract class Capsule {
    private final String name;
    private final boolean crewed;
    private final int maxOccupants;
    private final double massTons;
    private final double priceMillionEuros;

    /**
     * creates a capsule with its main mission data
     */
    protected Capsule(String name, boolean crewed, int maxOccupants, double massTons, double priceMillionEuros) {
        this.name = name;
        this.crewed = crewed;
        this.maxOccupants = maxOccupants;
        this.massTons = massTons;
        this.priceMillionEuros = priceMillionEuros;
    }

    public String getName() {return name;}

    public boolean isCrewed() {return crewed;}

    public int getMaxOccupants() {return maxOccupants;}

    public double getMassTons() {return massTons;}

    public double getPriceMillionEuros() {return priceMillionEuros;}

    public abstract String getMissionRole();

}
