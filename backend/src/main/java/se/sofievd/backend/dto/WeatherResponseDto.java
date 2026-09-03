package se.sofievd.backend.dto;

public record WeatherResponseDto(Double temperature,
                                 Double feelsLike,
                                 Integer condition,
                                 String conditionDescription,
                                 Double windSpeed,
                                 Double precipitation,
                                 String precipitationDescription) {
}
