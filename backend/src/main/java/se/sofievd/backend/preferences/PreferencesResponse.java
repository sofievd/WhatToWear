package se.sofievd.backend.preferences;

import se.sofievd.backend.outfit.ClothingDto;

import java.util.List;

public record PreferencesResponse(
        PreferencesRequest preferences,
        List<ClothingDto> availableClothing
) {
}