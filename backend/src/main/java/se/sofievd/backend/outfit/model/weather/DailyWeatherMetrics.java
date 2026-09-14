package se.sofievd.backend.outfit.model.weather;

public record DailyWeatherMetrics(
        double minTemp,
        double maxTemp,
        double precipitation,
        double minClo,
        double maxClo,
        boolean needSunglasses,
        boolean needBeanie,
        boolean needGloves,
        boolean needScarf
) {
}
