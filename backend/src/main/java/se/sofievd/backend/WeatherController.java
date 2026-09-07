package se.sofievd.backend;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import se.sofievd.backend.outfit.service.ClothingService;
import se.sofievd.backend.weather.model.Weather;
import se.sofievd.backend.weather.openmeteo.model.HourlyWeather;
import se.sofievd.backend.weather.service.WeatherMapper;
import se.sofievd.backend.weather.WeatherResponseDto;
import se.sofievd.backend.weather.service.WeatherService;

import java.util.List;

@RestController
public class WeatherController {
    private final ClothingService clothingService;
    private final WeatherService weatherService;

    public WeatherController(ClothingService clothingService, WeatherService weatherService) {
        this.clothingService = clothingService;
        this.weatherService = weatherService;
    }

    @GetMapping("/api/weather")
    public ResponseEntity<WeatherResponseDto> getWeather(
            @RequestParam Double latitude,
            @RequestParam Double longitude
    ){
        Weather response = weatherService.getGlobalWeather(longitude, latitude);
        return ResponseEntity.ok().body(WeatherMapper.toWeatherResponseDto(response));
    }


    @GetMapping("/api/weather/meteo")
    public ResponseEntity<List<HourlyWeather>> getMeteoWeather() {
        List<HourlyWeather> response = weatherService.getOpenMeteoWeather(59.334591, 18.063240);
        return ResponseEntity.ok().body(response);
    }
}
