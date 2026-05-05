package domain.capsule;

/**
 * crew dragon capsule preset
 */
public class CrewDragonCapsule extends Capsule {
    public CrewDragonCapsule() {
        super("Crew Dragon", true, 7, 12.0, 150);
    }

    @Override
    public String getMissionRole() {
        return "Crew orbital capsule";
    }
}
