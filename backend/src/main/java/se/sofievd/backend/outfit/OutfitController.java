package se.sofievd.backend.outfit;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import se.sofievd.backend.outfit.model.dto.ClothingDto;
import se.sofievd.backend.outfit.model.dto.RecommendationResponse;
import se.sofievd.backend.outfit.model.enums.ActivityLevel;
import se.sofievd.backend.outfit.model.Clothing;
import se.sofievd.backend.outfit.model.Recommendation;
import se.sofievd.backend.outfit.model.enums.ToleranceLevel;
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
    public ResponseEntity<RecommendationResponse> getRecommendation(
            @RequestParam Double latitude,
            @RequestParam Double longitude,
            @RequestParam String toleranceLevel,
            @RequestParam String activityLevel
    ) {


        ToleranceLevel tolerance = ToleranceLevel.valueOf(
                toleranceLevel.toUpperCase()
        );

        ActivityLevel activity = ActivityLevel.valueOf(
                activityLevel.toUpperCase()
        );

        Recommendation recommendation = recommendationService.generateRecommendation(longitude, latitude, tolerance, activity);
        return ResponseEntity.ok().body(ClothingMapper.toRecommendationResponse(recommendation));
    }
}
