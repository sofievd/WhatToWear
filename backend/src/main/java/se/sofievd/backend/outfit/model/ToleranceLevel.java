package se.sofievd.backend.outfit.model;

public enum ToleranceLevel {

    RUNS_COLD(-2.5),  // Feels 2.5°C colder than average
    NEUTRAL(0.0),     // Standard perception
    RUNS_HOT(2.5);    // Feels 2.5°C warmer than average

    private final double temperatureOffset;

    ToleranceLevel(double temperatureOffset) {
        this.temperatureOffset = temperatureOffset;
    }

    public double getTemperatureOffset() {
        return temperatureOffset;
    }

}
