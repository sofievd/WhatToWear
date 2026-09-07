package se.sofievd.backend.preferences;

import jakarta.validation.constraints.NotBlank;

public record PreferencesRequest(
        @NotBlank String activity,
        @NotBlank String thermalSensitivity,
        @NotBlank String style
) {
}