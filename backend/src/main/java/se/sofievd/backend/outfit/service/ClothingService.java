package se.sofievd.backend.outfit.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import se.sofievd.backend.outfit.model.Clothing;
import se.sofievd.backend.outfit.ClothingRepository;
import se.sofievd.backend.weather.smhi.SmhiClient;
import se.sofievd.backend.weather.smhi.model.SmhiWeather;

import java.util.List;

@Service
public class ClothingService {

    private static final Logger log = LoggerFactory.getLogger(ClothingService.class);
    private final ClothingRepository clothingRepository;

    public ClothingService(ClothingRepository clothingRepository) {
        this.clothingRepository = clothingRepository;
    }

    public List<Clothing> getAllClothes(){
        return clothingRepository.findAll();
    }


    public Clothing findBestItemFromDb(String category, double targetClo) {
        double minClo = Math.max(0.0, targetClo - 0.20);
        double maxClo = targetClo + 0.20;

        List<Clothing> matches = clothingRepository.findByCategoryAndCloValueBetween(category, minClo, maxClo);

        if (matches.isEmpty()) {
            List<Clothing> allCategoryItems = clothingRepository.findByCategoryAndCloValueBetween(category, 0.0, 5.0);
            return allCategoryItems.isEmpty() ? null : allCategoryItems.get(0);
        }
        return matches.get(0);
    }


    public Clothing findBestItemFromCategories(List<String> categories, double targetClo) {
        double minClo = Math.max(0.0, targetClo - 0.20);
        double maxClo = targetClo + 0.20;

        for (String category : categories) {
            List<Clothing> matches = clothingRepository.findByCategoryAndCloValueBetween(category, minClo, maxClo);
            if (!matches.isEmpty()) {
                return matches.get(0);
            }
        }

        for (String category : categories) {
            List<Clothing> anyItems = clothingRepository.findByCategoryAndCloValueBetween(category, 0.0, 5.0);
            if (!anyItems.isEmpty()) {
                return anyItems.get(0);
            }
        }

        return null;
    }

    public Clothing findByName(String name){
        return  clothingRepository.findByName(name);
    }

}
