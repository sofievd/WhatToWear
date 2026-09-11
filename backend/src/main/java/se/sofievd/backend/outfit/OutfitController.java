package se.sofievd.backend.outfit;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import se.sofievd.backend.outfit.model.ActivityLevel;
import se.sofievd.backend.outfit.model.Clothing;
import se.sofievd.backend.outfit.model.Recommendation;
import se.sofievd.backend.outfit.model.ToleranceLevel;
import se.sofievd.backend.outfit.service.ClothingMapper;
import se.sofievd.backend.outfit.service.ClothingService;
import se.sofievd.backend.outfit.service.RecommendationService;

import java.util.List;

@RestController
public class OutfitController {

    private final ClothingService clothingService;
    private final RecommendationService recommendationService;

    public OutfitController(ClothingService clothingService, RecommendationService recommendationService) {
        this.clothingService = clothingService;
        this.recommendationService = recommendationService;
    }

    @GetMapping("/api/weather/clothes")
    public ResponseEntity<List<ClothingDto>> getClothes() {
        List<Clothing> clothes = clothingService.getAllClothes();
        return ResponseEntity.ok().body(clothes.stream().map(ClothingMapper::toDto).toList());
    }


    @GetMapping("/api/recommendation")
    public ResponseEntity<Recommendation> getRecommendation() {
        Recommendation recommendation = recommendationService.generateRecommendation(18.063240, 58.334591, ToleranceLevel.NEUTRAL, ActivityLevel.WALKING);
        return ResponseEntity.ok().body(recommendation);
    }
}
