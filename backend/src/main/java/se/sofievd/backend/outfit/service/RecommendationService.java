package se.sofievd.backend.outfit.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import se.sofievd.backend.outfit.model.*;
import se.sofievd.backend.outfit.model.enums.ActivityLevel;
import se.sofievd.backend.outfit.model.Recommendation;
import se.sofievd.backend.outfit.model.enums.ToleranceLevel;
import se.sofievd.backend.outfit.model.weather.CurrentWeatherState;
import se.sofievd.backend.outfit.model.weather.DailyWeatherMetrics;
import se.sofievd.backend.weather.model.WeatherTimeSlot;
import se.sofievd.backend.weather.service.WeatherService;
import se.sofievd.backend.weather.service.WeatherUtils;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class RecommendationService {

    private static final Logger log = LoggerFactory.getLogger(RecommendationService.class);
    private final WeatherService weatherService;
    private final InsulationCalculator calculator;
    private final ClothingRecommendationPolicy policy;

    public RecommendationService(WeatherService weatherService, InsulationCalculator calculator, ClothingRecommendationPolicy policy) {
        this.weatherService = weatherService;
        this.calculator = calculator;
        this.policy = policy;
    }

    public Recommendation generateRecommendation(Double lon, Double lat, ToleranceLevel toleranceLevel, ActivityLevel activityLevel) {
        LocalTime time = LocalTime.now();

        List<WeatherTimeSlot> weatherTimeSlotList = weatherService.getAllDayWeather(lon, lat);
        List<WeatherTimeSlot> activeTimeSlots = WeatherUtils.filterActiveTimeSlots(weatherTimeSlotList, time);
        WeatherTimeSlot currentTimeSlot = WeatherUtils.findCurrentTimeSlot(weatherTimeSlotList, time);

        CurrentWeatherState currentWeatherState = calculator.analyzeCurrentWeather(currentTimeSlot, toleranceLevel, activityLevel);
        DailyWeatherMetrics dailyMetrics = calculator.calculateDailyMetrics(activeTimeSlots, toleranceLevel, activityLevel);

        OutfitLayers layers = determineOutfitLayers(dailyMetrics);

        return assembleRecommendation(currentWeatherState, dailyMetrics, layers);
    }

//    public Recommendation updateRecommendation(String userId){
//        // get cuurentWeatherState of user
//
//
//    }

    private OutfitLayers determineOutfitLayers(DailyWeatherMetrics metrics) {
        Clothing footwear = policy.getFootwear(metrics.maxTemp(), metrics.minTemp(), metrics.precipitation());
        Clothing socks = policy.getSocks(footwear);
        Clothing bottom = policy.getBottom(metrics.maxTemp());

        double remainingClo = Math.max(0.0, metrics.minClo() - bottom.getCloValue());
        Clothing base = policy.getBaseLayer(remainingClo);

        double baseOutfitClo = bottom.getCloValue() + base.getCloValue();
        double neededExtraClo = Math.max(0.0, metrics.maxClo() - baseOutfitClo);

        Clothing midLayer = null;
        Clothing outerLayer = null;

        if (neededExtraClo > 0.05) {
            midLayer = policy.getMidLayer(neededExtraClo);
            neededExtraClo -= (midLayer != null ? midLayer.getCloValue() : 0.0);
        }
        if (neededExtraClo > 0.2 || metrics.precipitation() > 0) {
            outerLayer = policy.getOuterLayer(neededExtraClo, metrics.precipitation());
        }

        return new OutfitLayers(footwear, socks, bottom, base, midLayer, outerLayer);
    }

    private Recommendation assembleRecommendation(CurrentWeatherState current, DailyWeatherMetrics daily, OutfitLayers layers) {
        Outfit outfit = new Outfit();
        outfit.setFootwear(layers.footwear());
        if (layers.socks() != null) {
            outfit.setSocks(layers.socks());
        }
        outfit.setBottom(layers.bottom());
        outfit.setBase(layers.base());

        double currentWornClo = layers.bottom().getCloValue() + layers.base().getCloValue();
        List<Clothing> packInBag = new ArrayList<>();

        if (layers.midLayer() != null) {
            if (currentWornClo < current.cloTarget()) {
                outfit.setMid(layers.midLayer());
                currentWornClo += layers.midLayer().getCloValue();
            } else {
                packInBag.add(layers.midLayer());
            }
        }

        if (layers.outerLayer() != null) {
            if (currentWornClo < current.cloTarget()) {
                outfit.setOuter(layers.outerLayer());
                currentWornClo += layers.outerLayer().getCloValue();
            } else {
                packInBag.add(layers.outerLayer());
            }
        }

        AccessoriesResult accessories = policy.determineAccessories(current, daily);
        outfit.setAccessories(accessories.worn());
        packInBag.addAll(accessories.packed());

        Recommendation recommendation = new Recommendation();
        recommendation.setWearNow(outfit);
        recommendation.setPackInBag(packInBag);

        return recommendation;
    }


//    private Recommendation createRecommendaiotn(){
    //        double currentCloTargetValue = 0;
//        double currentTemp = 0;
//        double currentWind = 0;
//        boolean currentSunny = false;
//        if (currentTimeSlot != null) {
//            double temperature = calculator.calculateEffectiveTemperature(currentTimeSlot.getMinFeelsLikeTemperature(), toleranceLevel.getTemperatureOffset());
//            currentCloTargetValue = calculator.calculateTargetClo(temperature, activityLevel.getMetValue(), currentTimeSlot.getWindSpeed());
//            currentTemp = currentTimeSlot.getMinFeelsLikeTemperature();
//            currentWind = currentTimeSlot.getWindSpeed();
//            currentSunny = currentTimeSlot.isSunny();
//        }
//
//        double minTemp = Double.MAX_VALUE;
//        double maxTemp = -Double.MAX_VALUE;
//        double precipitation = 0;
//        double maxCLo = 0;
//        double minCLo = Double.MAX_VALUE;
//
//        boolean needSunglasses = false;
//        boolean needBeanie = false;
//        boolean needGloves = false;
//        boolean needScarf = false;
//
//        //calculate clo for each timeslot
//        for (WeatherTimeSlot timeSlot : activeTimeSlots) {
//            double temperature = calculator.calculateEffectiveTemperature(timeSlot.getMinFeelsLikeTemperature(), toleranceLevel.getTemperatureOffset());
//            double targetCloValue = calculator.calculateTargetClo(temperature, activityLevel.getMetValue(), timeSlot.getWindSpeed());
//
//            minTemp = Math.min(timeSlot.getMinFeelsLikeTemperature(), minTemp);
//            maxTemp = Math.max(maxTemp, timeSlot.getMaxFeelsLikeTemperature());
//            precipitation += timeSlot.getPrecipitation();
//            minCLo = Math.min(minCLo, targetCloValue);
//            maxCLo = Math.max(maxCLo, targetCloValue);
//
//            if (timeSlot.isSunny() || timeSlot.getMaxUvIndex() >= 3) {
//                needSunglasses = true;
//            }
//            if (timeSlot.getMinFeelsLikeTemperature() < 5.0 ||
//                    (timeSlot.getMinFeelsLikeTemperature() < 12 && timeSlot.getWindSpeed() > 20.0)) {
//                needScarf = true;
//            }
//            if (timeSlot.getMinFeelsLikeTemperature() < 8) {
//                needBeanie = true;
//            }
//            if (timeSlot.getMinFeelsLikeTemperature() < 8) {
//                needGloves = true;
//            }
//        }
//
//        // recommend base-outfit on warmest weather (min cloValue);
//        Outfit outfit = new Outfit();
//
//        // get footwear
//        Clothing shoes = getFootwear(maxTemp, minTemp, precipitation);
//        outfit.setFootwear(shoes);
//
//        // get pants first on MAx feelslikeTemp
//        Clothing bottoms = getBottom(maxTemp);
//        outfit.setBottom(bottoms);
//
//        // get top from remainding
//        double remainingMinClo = Math.max(0.0, minCLo - bottoms.getCloValue());
//        log.info("remaining clo: {}", remainingMinClo);
//
//        Clothing base = getBaseLayer(remainingMinClo);
//        outfit.setBase(base);
//        double baseOutfitClo = bottoms.getCloValue() + base.getCloValue();
//
//        // extra needed clo for coldest spot
//        double neededExtraClo = Math.max(0.0, maxCLo - baseOutfitClo);
//        log.info("Extra needed clo: {}", neededExtraClo);
//
//        // set mid, outer layer;
//        Clothing midLayer = null;
//        Clothing outerLayer = null;
//
//        List<Clothing> packInBag = new ArrayList<>();
//
//        if (neededExtraClo > 0.05) {
//            midLayer = getMidLayer(neededExtraClo);
//            neededExtraClo -= (midLayer != null ? midLayer.getCloValue() : 0.0);
//        }
//        if (neededExtraClo > 0.2) {
//            outerLayer = getOuterLayer(neededExtraClo);
//        }
//
//        double currentWornClo = baseOutfitClo;
//        Recommendation recommendation = new Recommendation();
//        log.info("current CloTarget: {}", currentCloTargetValue);
//        log.info("Currently wearing clo: {}", currentWornClo);
//
//        if (midLayer != null) {
//            if (currentWornClo < currentCloTargetValue) {
//                outfit.setMid(midLayer);
//                currentWornClo += midLayer.getCloValue();
//            } else {
//                packInBag.add(midLayer);
//            }
//        }
//        if (outerLayer != null) {
//            if (currentWornClo < currentCloTargetValue) {
//                outfit.setOuter(outerLayer);
//                currentWornClo += outerLayer.getCloValue();
//            } else {
//                packInBag.add(outerLayer);
//            }
//        }
//
//        List<Clothing> wornAccessories = new ArrayList<>();
//        if (needSunglasses) {
//            Clothing sunglasses = getAccessory("sunglasses");
//            if (currentSunny) {
//                wornAccessories.add(sunglasses);
//            } else packInBag.add(sunglasses);
//        }
//        if (needScarf) {
//            Clothing scarf = getAccessory("scarf");
//            if (currentTemp < 8.0 || (currentTemp < 12.0 && currentWind > 20.0)) {
//                wornAccessories.add(scarf);
//            } else packInBag.add(scarf);
//        }
//        if (needBeanie) {
//            Clothing beanie = getAccessory("beanie");
//            if (currentTemp < 10) {
//                wornAccessories.add(beanie);
//            } else packInBag.add(beanie);
//        }
//        if (needGloves) {
//            Clothing gloves = getAccessory("gloves");
//            if (currentTemp < 8.0) {
//                wornAccessories.add(gloves);
//            } else packInBag.add(gloves);
//        }
//
//        outfit.setAcessories(wornAccessories);
//        recommendation.setWearNow(outfit);
//        recommendation.setPackInBag(packInBag);
//
//        return recommendation;
//    }

}
