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
    private final SmhiClient client;

    public ClothingService(ClothingRepository clothingRepository, SmhiClient client) {
        this.clothingRepository = clothingRepository;
        this.client = client;
    }

    public List<Clothing> getAllClothes(){
        return clothingRepository.findAll();
    }


    public Clothing findBestItemFromDb(String category, double targetClo) {
        double minClo = Math.max(0.0, targetClo - 0.20);
        double maxClo = targetClo + 0.20;
        log.info("minClo: {}, maxClo: {}", minClo, maxClo);

        List<Clothing> matches = clothingRepository.findByCategoryAndCloValueBetween(category, minClo, maxClo);

        log.info("the matches: {}", matches);
        if (matches.isEmpty()) {
            // Fallback: haal items op van deze categorie en pak de eerste beschikbare
            List<Clothing> allCategoryItems = clothingRepository.findByCategoryAndCloValueBetween(category, 0.0, 5.0);
            return allCategoryItems.isEmpty() ? null : allCategoryItems.get(0);
        }

        return matches.get(0);
    }

}
