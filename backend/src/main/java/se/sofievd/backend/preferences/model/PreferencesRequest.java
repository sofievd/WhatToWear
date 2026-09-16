package se.sofievd.backend.preferences.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record PreferencesRequest(
        @NotBlank
        @Pattern(regexp = "sitting|walking|biking|jogging|running")
        String activity,
        @NotBlank
        @Pattern(regexp = "cold|neutral|hot")
        String thermalSensitivity
) {
}