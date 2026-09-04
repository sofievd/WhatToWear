package se.sofievd.backend.weather.city.openmeteo;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import se.sofievd.backend.weather.city.model.OpenMeteoGeocodingResponse;

@Service
public class OpenMeteoGeocodingClient {
    private static final String ENTRY_POINT = "https://geocoding-api.open-meteo.com";

    private final RestClient restClient;

    public OpenMeteoGeocodingClient() {
        restClient = RestClient.builder()
                .baseUrl(ENTRY_POINT)
                .build();
    }

    public OpenMeteoGeocodingResponse searchCities(String query) {
        return restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/v1/search")
                        .queryParam("name", query)
                        .queryParam("count", 10)
                        .queryParam("language", "en")
                        .queryParam("format", "json")
                        .build())
                .retrieve()
                .body(OpenMeteoGeocodingResponse.class);
    }

}
