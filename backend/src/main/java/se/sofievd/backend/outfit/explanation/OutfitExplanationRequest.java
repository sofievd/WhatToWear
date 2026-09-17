package se.sofievd.backend.outfit.explanation;

import se.sofievd.backend.preferences.model.PreferencesRequest;

import java.util.List;

public record OutfitExplanationRequest(
        Double latitude,
        Double longitude,
        OutfitPayload recommendation,
        PreferencesRequest preferences
) {

    public record OutfitPayload(
            WearNowPayload wearNow,
            List<ClothingPayload> packInBag
    ) {}

    public record WearNowPayload(
            ClothingPayload base,
            ClothingPayload mid,
            ClothingPayload outer,
            ClothingPayload bottom,
            ClothingPayload footwear,
            ClothingPayload socks,
            List<ClothingPayload> accessories
    ) {}

    public record ClothingPayload(
            String name,
            String category,
            Double cloValue
    ) {}
}