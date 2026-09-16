package se.sofievd.backend.preferences.model;

import se.sofievd.backend.outfit.model.dto.ClothingDto;

import java.util.List;

public record PreferencesResponse(
        PreferencesRequest preferences,
        List<ClothingDto> availableClothing
) {
}