package se.sofievd.backend.weather.city;

import org.springframework.stereotype.Service;
import se.sofievd.backend.weather.city.model.CitySuggestion;
import se.sofievd.backend.weather.city.model.OpenMeteoGeocodingResponse;
import se.sofievd.backend.weather.city.openmeteo.OpenMeteoGeocodingClient;

import java.util.List;

@Service
public class CityService {
    private final OpenMeteoGeocodingClient geocodingClient;

    public CityService(OpenMeteoGeocodingClient geocodingClient) {
        this.geocodingClient = geocodingClient;
    }
    public List<CitySuggestion> searchCities(String query) {

        OpenMeteoGeocodingResponse response =
                geocodingClient.searchCities(query);

        if (response == null || response.results() == null) {
            return List.of();
        }

        return response.results()
                .stream()
                .map(city -> new CitySuggestion(
                        city.name(),
                        city.country(),
                        city.latitude(),
                        city.longitude()
                ))
                .toList();
    }
}
