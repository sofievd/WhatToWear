package se.sofievd.backend.outfit.service;

import org.springframework.stereotype.Service;
import se.sofievd.backend.outfit.model.ActivityLevel;
import se.sofievd.backend.outfit.model.Clothing;
import se.sofievd.backend.outfit.model.OutfitRecommendation;
import se.sofievd.backend.outfit.model.ToleranceLevel;
import se.sofievd.backend.weather.Weather;
import se.sofievd.backend.weather.WeatherService;

import java.util.List;

@Service
public class RecommendationService {

    private final ClothingService clothingService;
    private final WeatherService weatherService;
    private final InsulationCalculator calculator;

    public RecommendationService(ClothingService clothingService, WeatherService weatherService, InsulationCalculator calculator) {
        this.clothingService = clothingService;
        this.weatherService = weatherService;
        this.calculator = calculator;
    }

    public OutfitRecommendation generateRecommendation(){

        Weather weather = weatherService.getWeather(18.063240, 59.334591);

        double temperature = calculator.calculateEffectiveTemperature(weather.getFeelsLikeTemperature(), ToleranceLevel.NEUTRAL.getTemperatureOffset());
        double targetCloValue = calculator.calculateTargetClo(temperature, ActivityLevel.WALKING.getMetValue(), weather.getWindSpeed());

        Clothing outfit = clothingService.findBestItemFromDb("shirts", targetCloValue);
        OutfitRecommendation recommendation = new OutfitRecommendation(targetCloValue);

        recommendation.setBaseLayer(outfit);

        return recommendation;
    }


}
