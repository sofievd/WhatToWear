package se.sofievd.backend.outfit.explanation;

import se.sofievd.backend.preferences.PreferencesRequest;

public record OutfitExplanationRequest(
        Double latitude,
        Double longitude,
        OutfitPayload recommendation,
        PreferencesRequest preferences
) {
    public record OutfitPayload(
            Double targetCloValue,
            ClothingPayload baseLayer,
            ClothingPayload midLayer,
            ClothingPayload outerLayer,
            ClothingPayload bottom,
            ClothingPayload footwear,
            ClothingPayload accessory
    ) {}

    public record ClothingPayload(String name, String category, Double cloValue) {}
}