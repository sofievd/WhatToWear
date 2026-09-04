package se.sofievd.backend.weather.city.model;


import java.util.List;

public record OpenMeteoGeocodingResponse(
        List<OpenMeteoCityResult> results
) {
}
