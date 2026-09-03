package se.sofievd.backend.service;

import se.sofievd.backend.data.Weather;
import se.sofievd.backend.dto.WeatherResponseDto;

public class WeatherMapper {

    public static WeatherResponseDto toWeatherResponseDto(Weather weather){
        return new WeatherResponseDto(weather.getAirTemperature(),
                weather.getFeelsLikeTemperature(),
                weather.getCondition(),
                weather.getConditionDescription().toString(),
                weather.getWindSpeed(),
                weather.getPrecipitation(),
                weather.getPrecipitationDescription()
                );
    }
}
