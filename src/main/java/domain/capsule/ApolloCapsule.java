package domain.capsule;

/**
 * apollo capsule preset
 */
public class ApolloCapsule extends Capsule {
    public ApolloCapsule() {
        super("Apollo", true, 3, 5.6, 200);
    }

    @Override
    public String getMissionRole() {
        return "Crew lunar capsule";
    }
}
