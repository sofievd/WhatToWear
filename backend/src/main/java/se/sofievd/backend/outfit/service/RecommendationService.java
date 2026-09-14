package se.sofievd.backend.outfit.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import se.sofievd.backend.outfit.model.*;
import se.sofievd.backend.weather.model.CurrentWeatherState;
import se.sofievd.backend.weather.model.DailyWeatherMetrics;
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
        List<WeatherTimeSlot> activeTimeSlots = filterActiveTimeSlots(weatherTimeSlotList, time);
        WeatherTimeSlot currentTimeSlot = findCurrentTimeSlot(weatherTimeSlotList, time);

        CurrentWeatherState currentWeatherState = analyzeCurrentWeather(currentTimeSlot, toleranceLevel, activityLevel);
        DailyWeatherMetrics dailyMetrics = calculateDailyMetrics(activeTimeSlots, toleranceLevel, activityLevel);

        OutfitLayers layers = determineOutfitLayers(dailyMetrics);

        return assembleRecommendation(currentWeatherState, dailyMetrics, layers);
    }

    private WeatherTimeSlot findCurrentTimeSlot(List<WeatherTimeSlot> slots, LocalTime time) {
        return slots.stream()
                .filter(slot -> slot.getTimeSlot().contains(time))
                .findFirst()
                .orElse(null);

    }

    private List<WeatherTimeSlot> filterActiveTimeSlots(List<WeatherTimeSlot> timeSlots, LocalTime time) {
        return timeSlots.stream()
                .filter(slot -> slot.getTimeSlot().getEndTime().isAfter(time))
                .toList();
    }

    private CurrentWeatherState analyzeCurrentWeather(WeatherTimeSlot currentSlot, ToleranceLevel tolerance, ActivityLevel activity) {
        if (currentSlot == null) {
            return new CurrentWeatherState(0, 0, 0, false);
        }
        double temperature = calculator.calculateEffectiveTemperature(
                currentSlot.getMinFeelsLikeTemperature(),
                tolerance.getTemperatureOffset());

        double currentCloTargetValue = calculator.calculateTargetClo(
                temperature,
                activity.getMetValue(),
                currentSlot.getWindSpeed());
        return new CurrentWeatherState(
                currentCloTargetValue,
                currentSlot.getMinFeelsLikeTemperature(),
                currentSlot.getWindSpeed(),
                currentSlot.isSunny()
        );
    }

    private DailyWeatherMetrics calculateDailyMetrics(List<WeatherTimeSlot> activeTimeSlots, ToleranceLevel tolerance, ActivityLevel activity) {

        double minTemp = Double.MAX_VALUE;
        double maxTemp = -Double.MAX_VALUE;
        double precipitation = 0;
        double minClo = Double.MAX_VALUE;
        double maxClo = 0;

        boolean needSunglasses = false;
        boolean needBeanie = false;
        boolean needGloves = false;
        boolean needScarf = false;

        for (WeatherTimeSlot timeSlot : activeTimeSlots) {
            double temperature = calculator.calculateEffectiveTemperature(timeSlot.getMinFeelsLikeTemperature(), tolerance.getTemperatureOffset());
            double targetCloValue = calculator.calculateTargetClo(temperature, activity.getMetValue(), timeSlot.getWindSpeed());

            minTemp = Math.min(timeSlot.getMinFeelsLikeTemperature(), minTemp);
            maxTemp = Math.max(maxTemp, timeSlot.getMaxFeelsLikeTemperature());
            precipitation += timeSlot.getPrecipitation();
            minClo = Math.min(minClo, targetCloValue);
            maxClo = Math.max(maxClo, targetCloValue);

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
        return new DailyWeatherMetrics(
                minTemp,
                maxTemp,
                precipitation,
                minClo,
                maxClo,
                needSunglasses,
                needBeanie,
                needGloves,
                needScarf);

    }

    private OutfitLayers determineOutfitLayers(DailyWeatherMetrics metrics) {
        Clothing footwear = getFootwear(metrics.maxTemp(), metrics.minTemp(), metrics.precipitation());
        Clothing socks = getSocks(footwear);
        Clothing bottom = getBottom(metrics.maxTemp());

        double socksClo = (socks != null) ? socks.getCloValue() : 0.0;
        double remainingClo = Math.max(0.0, metrics.minClo() - bottom.getCloValue() - socksClo);
        Clothing base = getBaseLayer(remainingClo);

        double baseOutfitClo = bottom.getCloValue() + base.getCloValue();
        double neededExtraClo = Math.max(0.0, metrics.maxClo() - baseOutfitClo);

        Clothing midLayer = null;
        Clothing outerLayer = null;

        if (neededExtraClo > 0.04) {
            midLayer = getMidLayer(neededExtraClo);
            neededExtraClo -= (midLayer != null ? midLayer.getCloValue() : 0.0);
        }
        if (neededExtraClo > 0.2 || metrics.precipitation() > 0) {
            outerLayer = getOuterLayer(neededExtraClo, metrics.precipitation());
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

        AccessoriesResult accessories = determineAccessories(current, daily);
        outfit.setAccessories(accessories.worn());
        packInBag.addAll(accessories.packed());

        Recommendation recommendation = new Recommendation();
        recommendation.setWearNow(outfit);
        recommendation.setPackInBag(packInBag);

        return recommendation;
    }

    private AccessoriesResult determineAccessories(CurrentWeatherState current, DailyWeatherMetrics daily) {
        List<Clothing> worn = new ArrayList<>();
        List<Clothing> packed = new ArrayList<>();

        if (daily.needSunglasses()) {
            evaluateAccessory(getAccessory("sunglasses"), current.isSunny(), worn, packed);
        }
        if (daily.needScarf()) {
            boolean wearNow = current.temperature() < 8.0 || (current.temperature() < 12.0 && current.windSpeed() > 20.0);
            evaluateAccessory(getAccessory("scarf"), wearNow, worn, packed);
        }
        if (daily.needBeanie()) {
            evaluateAccessory(getAccessory("beanie"), current.temperature() < 10.0, worn, packed);
        }
        if (daily.needGloves()) {
            evaluateAccessory(getAccessory("gloves"), current.temperature() < 8.0, worn, packed);
        }

        return new AccessoriesResult(worn, packed);
    }

    private Clothing getSocks(Clothing footwear) {
        if ("sandals".equalsIgnoreCase(footwear.getName())) {
            return null;
        }
        return clothingService.findByName("socks");
    }

    private Clothing getFootwear(double maxTemp, double minTemp, double precipitation) {
        if (precipitation > 0) {
            if (minTemp < 6) {
                return clothingService.findByName("boots");
            }
            return clothingService.findByName("rainboots");
        }
        if (maxTemp > 22 && minTemp >= 16) {
            return clothingService.findByName("sandals");
        }
        return clothingService.findByName("sneakers");
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

    private Clothing getOuterLayer(double targetClo, double precipitation) {
        if (precipitation > 0) {
            Clothing rainCoat = clothingService.findByName("rain jacket");
            if (rainCoat != null) {
                return rainCoat;
            }
        }
        return clothingService.findBestItemFromCategories(List.of("jackets", "outerwear"), targetClo);
    }

    private Clothing getAccessory(String name) {
        return clothingService.findByName(name);
    }

    private void evaluateAccessory(Clothing item, boolean conditionWearNow, List<Clothing> worn, List<Clothing> bag) {
        if (conditionWearNow) {
            worn.add(item);
        } else {
            bag.add(item);
        }
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
