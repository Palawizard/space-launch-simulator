package domain.capsule;

/**
 * orion capsule preset
 */
public class OrionCapsule extends Capsule {
    public OrionCapsule() {
        super ("Orion", true, 4, 10.4, 300);
    }

    @Override
    public String getMissionRole() {
        return "Crew deep space capsule";
    }
}
