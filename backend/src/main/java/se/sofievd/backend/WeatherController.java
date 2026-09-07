package se.sofievd.backend;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import se.sofievd.backend.outfit.service.ClothingService;
import se.sofievd.backend.weather.Weather;
import se.sofievd.backend.weather.WeatherMapper;
import se.sofievd.backend.weather.WeatherResponseDto;
import se.sofievd.backend.weather.WeatherService;

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
    public ResponseEntity<String> getMeteoWeather() {
        String response = weatherService.getOpenMeteoWeather(59.334591, 18.063240);
        return ResponseEntity.ok().body(response);
    }
}
