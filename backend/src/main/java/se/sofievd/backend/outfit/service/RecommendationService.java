package se.sofievd.backend.outfit.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.stereotype.Service;
import se.sofievd.backend.outfit.model.*;
import se.sofievd.backend.weather.model.Weather;
import se.sofievd.backend.weather.model.WeatherTimeSlot;
import se.sofievd.backend.weather.service.WeatherService;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RecommendationService {

    private static final Logger log = LoggerFactory.getLogger(RecommendationService.class);
    private final ClothingService clothingService;
    private final WeatherService weatherService;
    private final InsulationCalculator calculator;

    public RecommendationService(ClothingService clothingService, WeatherService weatherService, InsulationCalculator calculator) {
        this.clothingService = clothingService;
        this.weatherService = weatherService;
        this.calculator = calculator;
    }


    public Recommendation generateRecommendation(Double lon, Double lat, ToleranceLevel toleranceLevel, ActivityLevel activityLevel){

        LocalTime time = LocalTime.now();

        List<WeatherTimeSlot> weatherTimeSlotList = weatherService.getAllDayWeather(lon, lat);
        List<WeatherTimeSlot> activeTimeSlots = new ArrayList<>();
        WeatherTimeSlot currentTimeSlot = null;
        // filter timeslots
        for(WeatherTimeSlot weatherTimeSlot: weatherTimeSlotList){
            if(weatherTimeSlot.getTimeSlot().contains(time)){
                currentTimeSlot =  weatherTimeSlot;
            }
            if(weatherTimeSlot.getTimeSlot().getEndTime().isAfter(LocalTime.now())){
                activeTimeSlots.add(weatherTimeSlot);
            }
        }

        double currentCloTargetValue = 0;
        if(currentTimeSlot != null){
            double temperature = calculator.calculateEffectiveTemperature(currentTimeSlot.getMinFeelsLikeTemperature(), toleranceLevel.getTemperatureOffset());
            currentCloTargetValue = calculator.calculateTargetClo(temperature, activityLevel.getMetValue(), currentTimeSlot.getWindSpeed());
        }

        double minTemp = Double.MAX_VALUE;
        double maxTemp = -Double.MAX_VALUE;
        double precipitation = 0;
        double maxCLo = 0;
        double minCLo = Double.MAX_VALUE;

        //calculate clo for each timeslot
        for(WeatherTimeSlot timeSlot: activeTimeSlots){
            double temperature = calculator.calculateEffectiveTemperature(timeSlot.getMinFeelsLikeTemperature(), toleranceLevel.getTemperatureOffset());
            double targetCloValue = calculator.calculateTargetClo(temperature, activityLevel.getMetValue(), timeSlot.getWindSpeed());
            minTemp = Math.min(timeSlot.getMinFeelsLikeTemperature(), minTemp);
            maxTemp = Math.max(maxTemp, timeSlot.getMaxFeelsLikeTemperature());
            precipitation += timeSlot.getPrecipitation();
            minCLo = Math.min(minCLo, targetCloValue);
            maxCLo = Math.max(maxCLo, targetCloValue);
        }

        // recommend base-outfit on warmest weather (min cloValue);

        Outfit outfit = new Outfit();

        // get footwear
        Clothing shoes = getFootwear(maxTemp, minTemp, precipitation);
        outfit.setFootwear(shoes);

        // get pants first on MAx feelslikeTemp
        Clothing bottoms = getBottom(maxTemp);
        outfit.setBottom(bottoms);

        // get top from remainding
        double remainingMinClo = Math.max(0.0, minCLo - bottoms.getCloValue());
        Clothing base = getBaseLayer(remainingMinClo);
        outfit.setBase(base);
        double baseOutfitClo = bottoms.getCloValue() + base.getCloValue();

        log.info("base clo: {}", baseOutfitClo);

        log.info("max CLo: {}", maxCLo);

        // extra needed clo for coldest spot
        double neededExtraClo = Math.max(0.0, maxCLo-baseOutfitClo);
        log.info("Extra needed clo: {}", neededExtraClo);

        // set mid, outer layer;
        Clothing midLayer = null;
        Clothing outerLayer = null;

        if(neededExtraClo > 0.3){
            midLayer = getMidLayer(neededExtraClo);
            neededExtraClo -= (midLayer != null ? midLayer.getCloValue() : 0.0);
            log.info("midlayer: {}", midLayer);
        }
        if (neededExtraClo > 0.3){
            outerLayer = getOuterLayer(neededExtraClo);
            log.info("outerlayer: {}", outerLayer);
        }

        double currentWornClo = baseOutfitClo;
        Recommendation recommendation = new Recommendation();

        if(midLayer != null){
            if(currentWornClo < currentCloTargetValue){
                outfit.setMid(midLayer);
                currentWornClo -= midLayer.getCloValue();
            }else {
                //TODO: fix adding item to bag
                log.info("");
                recommendation.setPackInBag(List.of(midLayer));
            }
        }
        if(outerLayer != null){
            if(currentWornClo < currentCloTargetValue){
                outfit.setOuter(outerLayer);
                currentWornClo -= outerLayer.getCloValue();
            }
            else {
                recommendation.getPackInBag().add(outerLayer);
            }
        }
        recommendation.setWearNow(outfit);
//        double baseTarget = Math.min(targetCloValue, 0.25);
//        Clothing baseLayer = getBaseLayer(baseTarget);
//
//        Double remainingClo = targetCloValue - (baseLayer!= null ? baseLayer.getCloValue() : baseTarget);
//        Clothing bottomLayer = getBottom();
//
//        recommendation.setBaseLayer(baseLayer);
//        recommendation.setBottom(bottomLayer);
//        if(remainingClo > 0.15){
//            double midTarget = Math.min(remainingClo, 0.40);
//            Clothing midLayer = getMidLayer(midTarget);
//            recommendation.setMidLayer(midLayer);
//            if(midLayer != null){
//                remainingClo -= midLayer.getCloValue();
//            }
//        }
//
//        if(remainingClo > 0.15){
//            Clothing outerLayer = getOuterLayer(remainingClo);
//            recommendation.setOuterLayer(outerLayer);
//        }

       // recommendation.setFootwear(clothingService.findBestItemFromDb("footwear", 0.10));
//
//        if(temperature < 5.0){
//            Clothing accessory = getAccessory();
//            recommendation.setAccessory(accessory);
//        }

        return recommendation;
    }

    private Clothing getFootwear(double maxTemp, double minTemp, double precipitation) {
        if(maxTemp > 22 && minTemp >= 16 && precipitation == 0){
            return clothingService.findByName("Sandals");
        } else if (minTemp < 6 && precipitation > 0) {
            return clothingService.findByName("Boots");
        }
        return clothingService.findByName("Sneakers");
    }

    private OutfitRecommendation createOutfitRecommendation(Double targetCloValue){
        OutfitRecommendation recommendation = new OutfitRecommendation(targetCloValue);
        return recommendation;
    }

    private Clothing getBaseLayer(double targetClo){
        return clothingService.findBestItemFromDb("shirts", targetClo);
    }

    private Clothing getBottom(double temp){
        if(temp > 22 ){
            return clothingService.findByName("shorts");
        } else if (temp < 22 & temp > 10) {
            return clothingService.findByName("trousers");
        }
        else return clothingService.findByName("flannel trousers");
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
