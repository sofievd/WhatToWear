package se.sofievd.backend.outfit.model.dto;

import java.util.List;

public record RecommendationResponse(OutfitResponse wearNow,
                                     List<ClothingDto> packInBag) {
}
