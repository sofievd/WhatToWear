package se.sofievd.backend.outfit.model.dto;

import se.sofievd.backend.outfit.model.Clothing;
import se.sofievd.backend.outfit.model.Outfit;

import java.util.List;

public record RecommendationResponse(Outfit wearNow,
                                     List<Clothing> packInBag) {
}
