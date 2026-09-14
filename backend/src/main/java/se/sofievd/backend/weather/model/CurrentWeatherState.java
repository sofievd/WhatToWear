package se.sofievd.backend.weather.model;

public record CurrentWeatherState(
        double cloTarget,
        double temperature,
        double windSpeed,
        boolean isSunny
) {
}
