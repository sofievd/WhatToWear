package se.sofievd.backend.weather.city.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record OpenMeteoCityResult(
        String name,
        Double latitude,
        Double longitude,
        String country
) {
}