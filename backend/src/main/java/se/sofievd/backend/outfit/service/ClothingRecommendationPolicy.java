package se.sofievd.backend.outfit.service;

import org.springframework.stereotype.Service;
import se.sofievd.backend.outfit.model.AccessoriesResult;
import se.sofievd.backend.outfit.model.Clothing;
import se.sofievd.backend.outfit.model.weather.CurrentWeatherState;
import se.sofievd.backend.outfit.model.weather.DailyWeatherMetrics;

import java.util.ArrayList;
import java.util.List;

@Service
public class ClothingRecommendationPolicy {

    private final ClothingService clothingService;

    public ClothingRecommendationPolicy(ClothingService clothingService) {
        this.clothingService = clothingService;
    }

    public AccessoriesResult determineAccessories(CurrentWeatherState current, DailyWeatherMetrics daily) {
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

    public Clothing getSocks(Clothing footwear) {
        if ("sandals".equalsIgnoreCase(footwear.getName())) {
            return null;
        }
        return clothingService.findByName("socks");
    }

    public Clothing getFootwear(double maxTemp, double minTemp, double precipitation) {
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

    public Clothing getBaseLayer(double targetClo) {
        return clothingService.findBestItemFromDb("shirts", targetClo);
    }

    public Clothing getBottom(double temp) {
        if (temp > 22) {
            return clothingService.findByName("shorts");
        } else if (temp < 22 & temp > 10) {
            return clothingService.findByName("trousers");
        } else return clothingService.findByName("flannel trousers");
    }

    public Clothing getMidLayer(double targetClo) {
        return clothingService.findBestItemFromDb("sweaters", targetClo);
    }

    public Clothing getOuterLayer(double targetClo, double precipitation) {
        if (precipitation > 0) {
            Clothing rainCoat = clothingService.findByName("rain jacket");
            if (rainCoat != null) {
                return rainCoat;
            }
        }
        return clothingService.findBestItemFromCategories(List.of("jackets", "outerwear"), targetClo);
    }

    public Clothing getAccessory(String name) {
        return clothingService.findByName(name);
    }

    private void evaluateAccessory(Clothing item, boolean conditionWearNow, List<Clothing> worn, List<Clothing> bag) {
        if (conditionWearNow) {
            worn.add(item);
        } else {
            bag.add(item);
        }
    }


}
