package se.sofievd.backend.outfit.service;

import org.springframework.stereotype.Service;
import se.sofievd.backend.outfit.model.ActivityLevel;
import se.sofievd.backend.outfit.model.Clothing;
import se.sofievd.backend.outfit.model.OutfitRecommendation;
import se.sofievd.backend.outfit.model.ToleranceLevel;
import se.sofievd.backend.weather.model.Weather;
import se.sofievd.backend.weather.service.WeatherService;

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

        OutfitRecommendation recommendation = new OutfitRecommendation(targetCloValue);
        double baseTarget = Math.min(targetCloValue, 0.25);
        Clothing baseLayer = getBaseLayer(baseTarget);

        Double remainingClo = targetCloValue - (baseLayer!= null ? baseLayer.getCloValue() : baseTarget);
        Clothing bottomLayer = getBottom();

        recommendation.setBaseLayer(baseLayer);
        recommendation.setBottom(bottomLayer);
        if(remainingClo > 0.15){
            double midTarget = Math.min(remainingClo, 0.40);
            Clothing midLayer = getMidLayer(midTarget);
            recommendation.setMidLayer(midLayer);
            if(midLayer != null){
                remainingClo -= midLayer.getCloValue();
            }
        }

        if(remainingClo > 0.15){
            Clothing outerLayer = getOuterLayer(remainingClo);
            recommendation.setOuterLayer(outerLayer);
        }

        recommendation.setFootwear(clothingService.findBestItemFromDb("footwear", 0.10));

        if(temperature < 5.0){
            Clothing accessory = getAccessory();
            recommendation.setAccessory(accessory);
        }

        return recommendation;
    }

    private Clothing getBaseLayer(double targetClo){
        return clothingService.findBestItemFromDb("shirts", targetClo);
    }

    private Clothing getBottom(){
        return clothingService.findBestItemFromDb("trousers", 0.25);
    }

    private Clothing getMidLayer(double targetClo){
        return clothingService.findBestItemFromDb("sweaters", targetClo);
    }

    private Clothing getOuterLayer(double targetClo){
        return clothingService.findBestItemFromCategories(List.of("jackets", "outerwear"), targetClo);
    }

    private Clothing getAccessory(){
        return clothingService.findBestItemFromDb("accessories", 0.15);
    }

}
