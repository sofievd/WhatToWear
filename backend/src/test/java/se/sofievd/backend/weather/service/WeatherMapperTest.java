package se.sofievd.backend.weather.service;

import org.junit.jupiter.api.Test;
import se.sofievd.backend.weather.model.Weather;
import se.sofievd.backend.weather.model.WeatherResponseDto;
import se.sofievd.backend.weather.model.enums.WeatherSymbol;

import static org.junit.jupiter.api.Assertions.*;

class WeatherMapperTest {

    @Test
    public void toWeatherResponseDto_ValidWeatherObject_MapsAllFieldsCorrectly() {
        Weather weather = createWeather(
                22.5,          // airTemperature
                20.0,          // feelsLikeTemperature
                0,       // condition
                15.0,          // windSpeed
                0.0,           // precipitation
                "No rain"      // precipitationDescription
        );

        WeatherResponseDto result = WeatherMapper.toWeatherResponseDto(weather);

        assertEquals(22.5, result.temperature(), 0.0001);
        assertEquals(20.0, result.feelsLike(), 0.0001);
        assertEquals(0, result.condition());
        assertEquals("Clear sky", result.conditionDescription());
        assertEquals(15.0, result.windSpeed(), 0.0001);
        assertEquals(0.0, result.precipitation(), 0.0001);
        assertEquals("No rain", result.precipitationDescription());
    }

    @Test
    public void toWeatherResponseDto_NullWeatherInput_ThrowsNullPointerException() {
        assertThrows(NullPointerException.class, () -> {
            WeatherMapper.toWeatherResponseDto(null);
        });
    }


    @Test
    public void toWeatherResponseDto_SubZeroTemperatures_MapsNegativeValuesCorrectly() {
        Weather weather = createWeather(-10.5, -15.0, 2, 25.0, 5.0, "Light rain");

        WeatherResponseDto result = WeatherMapper.toWeatherResponseDto(weather);

        assertEquals(-10.5, result.temperature(), 0.0001);
        assertEquals(-15.0, result.feelsLike(), 0.0001);
    }

    private Weather createWeather(double temp, double feelsLike, Integer condition, double wind, double precip, String precipDesc) {
        Weather weather = new Weather();
        weather.setAirTemperature(temp);
        weather.setFeelsLikeTemperature(feelsLike);
        weather.setCondition(condition);

      weather.setConditionDescription(WeatherSymbol.fromWmoCode(condition));

        weather.setWindSpeed(wind);
        weather.setPrecipitation(precip);
        weather.setPrecipitationDescription(precipDesc);
        return weather;
    }
}