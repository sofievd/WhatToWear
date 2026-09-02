package se.sofievd.backend;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import se.sofievd.backend.data.Weather;
import se.sofievd.backend.dto.ClothingDto;
import se.sofievd.backend.dto.WeatherResponseDto;
import se.sofievd.backend.enitiy.Clothing;
import se.sofievd.backend.service.ClothingMapper;
import se.sofievd.backend.service.ClothingService;
import se.sofievd.backend.service.WeatherMapper;
import se.sofievd.backend.service.WeatherService;

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
        Weather response = weatherService.getWeather(59.334591, 18.063240);
        return ResponseEntity.ok().body(WeatherMapper.toWeatherResponseDto(response));
    }

}
