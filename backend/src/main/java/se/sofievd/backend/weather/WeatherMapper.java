package se.sofievd.backend.weather;

public class WeatherMapper {

    public static WeatherResponseDto toWeatherResponseDto(Weather weather){
        return new WeatherResponseDto(weather.getAirTemperature(),
                weather.getFeelsLikeTemperature(),
                weather.getCondition(),
                weather.getConditionDescription().getDescription(),
                weather.getWindSpeed(),
                weather.getPrecipitation(),
                weather.getPrecipitationDescription()
                );
    }
}
