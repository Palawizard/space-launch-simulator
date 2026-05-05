package domain.booster;

/**
 * reusable rocket thrust booster
 */
public class Booster {
    private final String name;
    private final double additionalThrustKilonewtons;
    private final double massTons;
    private final double priceMillionEuros;

    public Booster(String name, double additionalThrustKilonewtons, double massTons, double priceMillionEuros) {
        this.name = name;
        this.additionalThrustKilonewtons = additionalThrustKilonewtons;
        this.massTons = massTons;
        this.priceMillionEuros = priceMillionEuros;
    }

    public String getName() {return name;}
    public double getAdditionalThrustKilonewtons() {return additionalThrustKilonewtons;}
    public double getMassTons() {return massTons;}
    public double getPriceMillionEuros() {return priceMillionEuros;}
}
