package se.sofievd.backend;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import se.sofievd.backend.weather.Weather;
import se.sofievd.backend.outfit.ClothingDto;
import se.sofievd.backend.weather.WeatherResponseDto;
import se.sofievd.backend.outfit.Clothing;
import se.sofievd.backend.outfit.ClothingMapper;
import se.sofievd.backend.outfit.ClothingService;
import se.sofievd.backend.weather.WeatherMapper;
import se.sofievd.backend.weather.WeatherService;

import java.util.List;

@RestController
public class Controller {
    private final ClothingService clothingService;
    private final WeatherService weatherService;

    public Controller(ClothingService clothingService, WeatherService weatherService) {
        this.clothingService = clothingService;
        this.weatherService = weatherService;
    }

    @GetMapping("/api/weather/clothes")
    public ResponseEntity<List<ClothingDto>> getClothes(){
        List<Clothing> clothes = clothingService.getAllClothes();
        return ResponseEntity.ok().body(clothes.stream().map(ClothingMapper::toDto).toList());
    }

    @GetMapping("/api/weather")
    public ResponseEntity<WeatherResponseDto> getWeather(){
        Weather response = weatherService.getWeather(18.063240, 59.334591);
        return ResponseEntity.ok().body(WeatherMapper.toWeatherResponseDto(response));
    }



    @GetMapping("/api/weather/meteo")
    public ResponseEntity<String> getMeteoWeather(){
        String response = weatherService.getOpenMeteoWeather(59.334591, 18.063240);
        return ResponseEntity.ok().body(response);
    }
}
