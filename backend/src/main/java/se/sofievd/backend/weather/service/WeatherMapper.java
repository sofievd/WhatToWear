package se.sofievd.backend.weather.service;

import se.sofievd.backend.weather.WeatherResponseDto;
import se.sofievd.backend.weather.model.Weather;

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
