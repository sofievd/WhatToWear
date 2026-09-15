package se.sofievd.backend.outfit.model.weather;

public record CurrentWeatherState(
        double cloTarget,
        double minFeelsLikeTemperature,
        double temperature,
        double windSpeed,
        boolean isSunny
) {
}
