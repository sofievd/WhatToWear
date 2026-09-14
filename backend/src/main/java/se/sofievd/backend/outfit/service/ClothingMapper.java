package se.sofievd.backend.outfit.service;

import se.sofievd.backend.outfit.model.dto.ClothingDto;
import se.sofievd.backend.outfit.model.Clothing;
import se.sofievd.backend.outfit.model.Outfit;
import se.sofievd.backend.outfit.model.Recommendation;
import se.sofievd.backend.outfit.model.dto.OutfitResponse;
import se.sofievd.backend.outfit.model.dto.RecommendationResponse;

public class ClothingMapper {

    private ClothingMapper() {
    }

    public static ClothingDto toDto(Clothing clothing) {
        return new ClothingDto(
                clothing.getName(),
                clothing.getCategory(),
                clothing.getImageUrl());

    }

    public static OutfitResponse toOutFitResponse(Outfit outfit) {
        return new OutfitResponse(outfit.getBase(),
                outfit.getBottom(),
                outfit.getFootwear(),
                outfit.getSocks(),
                outfit.getMid(),
                outfit.getOuter(),
                outfit.getAccessories());
    }

    public static RecommendationResponse toRecommendationResponse(Recommendation recommendation) {
        return new RecommendationResponse(toOutFitResponse(recommendation.getWearNow()),
                recommendation.getPackInBag().stream().map(ClothingMapper::toDto).toList());
    }

}
