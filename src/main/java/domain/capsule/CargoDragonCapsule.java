package domain.capsule;

public class CargoDragonCapsule extends Capsule {
    public CargoDragonCapsule() {
        super("Cargo Dragon", false, 0, 9.5, 100);
    }

    @Override
    public String getMissionRole() {
        return "Cargo orbital capsule";
    }
}
