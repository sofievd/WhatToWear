package se.sofievd.backend.weather.city.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record OpenMeteoCityResult(
        Integer id,
        String name,
        Double latitude,
        Double longitude,
        Double elevation,
        String timezone,

        @JsonProperty("feature_code")
        String featureCode,

        @JsonProperty("country_code")
        String countryCode,

        String country,

        @JsonProperty("country_id")
        Integer countryId,

        Integer population,
        String[] postcodes,

        String admin1,
        String admin2,
        String admin3,
        String admin4,

        @JsonProperty("admin1_id")
        Integer admin1Id,

        @JsonProperty("admin2_id")
        Integer admin2Id,

        @JsonProperty("admin3_id")
        Integer admin3Id,

        @JsonProperty("admin4_id")
        Integer admin4Id
) {
}