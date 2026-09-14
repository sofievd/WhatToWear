package se.sofievd.backend.outfit.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import se.sofievd.backend.outfit.model.*;
import se.sofievd.backend.weather.model.WeatherTimeSlot;
import se.sofievd.backend.weather.service.WeatherService;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

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


    public Recommendation generateRecommendation(Double lon, Double lat, ToleranceLevel toleranceLevel, ActivityLevel activityLevel) {

        LocalTime time = LocalTime.now();

        List<WeatherTimeSlot> weatherTimeSlotList = weatherService.getAllDayWeather(lon, lat);
        List<WeatherTimeSlot> activeTimeSlots = new ArrayList<>();
        WeatherTimeSlot currentTimeSlot = null;
        // filter timeslots
        for (WeatherTimeSlot weatherTimeSlot : weatherTimeSlotList) {
            if (weatherTimeSlot.getTimeSlot().contains(time)) {
                currentTimeSlot = weatherTimeSlot;
            }
            if (weatherTimeSlot.getTimeSlot().getEndTime().isAfter(LocalTime.now())) {
                activeTimeSlots.add(weatherTimeSlot);
            }
        }

        double currentCloTargetValue = 0;
        double currentTemp = 0;
        double currentWind = 0;
        boolean currentSunny = false;
        if (currentTimeSlot != null) {
            double temperature = calculator.calculateEffectiveTemperature(currentTimeSlot.getMinFeelsLikeTemperature(), toleranceLevel.getTemperatureOffset());
            currentCloTargetValue = calculator.calculateTargetClo(temperature, activityLevel.getMetValue(), currentTimeSlot.getWindSpeed());
            currentTemp = currentTimeSlot.getMinFeelsLikeTemperature();
            currentWind = currentTimeSlot.getWindSpeed();
            currentSunny = currentTimeSlot.isSunny();
        }

        double minTemp = Double.MAX_VALUE;
        double maxTemp = -Double.MAX_VALUE;
        double precipitation = 0;
        double maxCLo = 0;
        double minCLo = Double.MAX_VALUE;

        boolean needSunglasses = false;
        boolean needBeanie = false;
        boolean needGloves = false;
        boolean needScarf = false;

        //calculate clo for each timeslot
        for (WeatherTimeSlot timeSlot : activeTimeSlots) {
            double temperature = calculator.calculateEffectiveTemperature(timeSlot.getMinFeelsLikeTemperature(), toleranceLevel.getTemperatureOffset());
            double targetCloValue = calculator.calculateTargetClo(temperature, activityLevel.getMetValue(), timeSlot.getWindSpeed());

            minTemp = Math.min(timeSlot.getMinFeelsLikeTemperature(), minTemp);
            maxTemp = Math.max(maxTemp, timeSlot.getMaxFeelsLikeTemperature());
            precipitation += timeSlot.getPrecipitation();
            minCLo = Math.min(minCLo, targetCloValue);
            maxCLo = Math.max(maxCLo, targetCloValue);

            if (timeSlot.isSunny() || timeSlot.getMaxUvIndex() >= 3) {
                needSunglasses = true;
            }
            if (timeSlot.getMinFeelsLikeTemperature() < 5.0 ||
                    (timeSlot.getMinFeelsLikeTemperature() < 12 && timeSlot.getWindSpeed() > 20.0)) {
                needScarf = true;
            }
            if (timeSlot.getMinFeelsLikeTemperature() < 8) {
                needBeanie = true;
            }
            if (timeSlot.getMinFeelsLikeTemperature() < 8) {
                needGloves = true;
            }
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
        log.info("remaining clo: {}", remainingMinClo);

        Clothing base = getBaseLayer(remainingMinClo);
        outfit.setBase(base);
        double baseOutfitClo = bottoms.getCloValue() + base.getCloValue();

        // extra needed clo for coldest spot
        double neededExtraClo = Math.max(0.0, maxCLo - baseOutfitClo);
        log.info("Extra needed clo: {}", neededExtraClo);

        // set mid, outer layer;
        Clothing midLayer = null;
        Clothing outerLayer = null;

        List<Clothing> packInBag = new ArrayList<>();

        if (neededExtraClo > 0.05) {
            midLayer = getMidLayer(neededExtraClo);
            neededExtraClo -= (midLayer != null ? midLayer.getCloValue() : 0.0);
            log.info("midlayer: {}", midLayer);
        }
        if (neededExtraClo > 0.2) {
            outerLayer = getOuterLayer(neededExtraClo);
            log.info("outerlayer: {}", outerLayer);
        }

        double currentWornClo = baseOutfitClo;
        Recommendation recommendation = new Recommendation();
        log.info("current CloTarget: {}", currentCloTargetValue);
        log.info("Currently wearing clo: {}", currentWornClo);

        if (midLayer != null) {
            if (currentWornClo < currentCloTargetValue) {
                outfit.setMid(midLayer);
                currentWornClo += midLayer.getCloValue();
            } else {
                packInBag.add(midLayer);
            }
        }
        if (outerLayer != null) {
            if (currentWornClo < currentCloTargetValue) {
                outfit.setOuter(outerLayer);
                currentWornClo += outerLayer.getCloValue();
            } else {
                packInBag.add(outerLayer);
            }
        }

        List<Clothing> wornAccessories = new ArrayList<>();
        if (needSunglasses) {
            Clothing sunglasses = getAccessory("sunglasses");
            if (currentSunny) {
                wornAccessories.add(sunglasses);
            } else packInBag.add(sunglasses);
        }
        if (needScarf) {
            Clothing scarf = getAccessory("scarf");
            if (currentTemp < 8.0 || (currentTemp < 12.0 && currentWind > 20.0)) {
                wornAccessories.add(scarf);
            } else packInBag.add(scarf);
        }
        if (needBeanie) {
            Clothing beanie = getAccessory("beanie");
            if (currentTemp < 10) {
                wornAccessories.add(beanie);
            } else packInBag.add(beanie);
        }
        if (needGloves) {
            Clothing gloves = getAccessory("gloves");
            if (currentTemp < 8.0) {
                wornAccessories.add(gloves);
            } else packInBag.add(gloves);
        }

        outfit.setAcessories(wornAccessories);
        recommendation.setWearNow(outfit);
        recommendation.setPackInBag(packInBag);

        return recommendation;
    }


    private Clothing getFootwear(double maxTemp, double minTemp, double precipitation) {
        if (maxTemp > 22 && minTemp >= 16 && precipitation == 0) {
            return clothingService.findByName("Sandals");
        } else if (minTemp < 6 && precipitation > 0) {
            return clothingService.findByName("Boots");
        }
        return clothingService.findByName("Sneakers");
    }

    private Clothing getBaseLayer(double targetClo) {
        return clothingService.findBestItemFromDb("shirts", targetClo);
    }

    private Clothing getBottom(double temp) {
        if (temp > 22) {
            return clothingService.findByName("shorts");
        } else if (temp < 22 & temp > 10) {
            return clothingService.findByName("trousers");
        } else return clothingService.findByName("flannel trousers");
    }

    private Clothing getMidLayer(double targetClo) {
        return clothingService.findBestItemFromDb("sweaters", targetClo);
    }

    private Clothing getOuterLayer(double targetClo) {
        return clothingService.findBestItemFromCategories(List.of("jackets", "outerwear"), targetClo);
    }

    private Clothing getAccessory(String name) {

        return clothingService.findByName(name);
    }

}
