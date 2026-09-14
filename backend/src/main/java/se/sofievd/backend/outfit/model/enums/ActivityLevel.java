package se.sofievd.backend.outfit.model.enums;

public enum ActivityLevel {

    SITTING(1.0, "Sitting quietly, general"),
    WALKING(4.0, "Walking, self-selected speed"),
    BIKING(7.0, "Bicycling, general"),
    JOGGING(7.5, "Jogging, general, self-selected pace"),
    RUNNING(10.5, "Running, self-selected pace");

    private final double metValue;
    private final String description;

    ActivityLevel(double metValue, String description) {
        this.metValue = metValue;
        this.description = description;
    }

    public double getMetValue() {
        return metValue;
    }

    public String getDescription() {
        return description;
    }

    public double getMetabolicRate() {
        return metValue * 58.2;
    }

}
