package bg.sofia.uni.fmi.mjt.vehiclerent.driver;

public enum AgeGroup {
    JUNIOR(10),
    EXPERIENCED(0),
    SENIOR(15);

    private final int ageGroup;

    AgeGroup(int ageGroup) {
        this.ageGroup = ageGroup;
    }

    public int getAgeGroup() {
        return ageGroup;
    }
}