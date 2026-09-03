package se.sofievd.backend.weather.openmeteo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;
import se.sofievd.backend.common.exception.OpenMeteoBadRequestException;
import se.sofievd.backend.common.exception.OpenMeteoRateLimitException;
import se.sofievd.backend.common.exception.OpenMeteoUnavailabeException;
import se.sofievd.backend.weather.openmeteo.model.OpenMeteoWeather;

@Service
public class OpenMeteoClient {

    private static final String ENTRY_POINT = "https://api.open-meteo.com";
    private static final String PARAMETERS = "temperature_2m,apparent_temperature,wind_speed_10m,relative_humidity_2m,weather_code,precipitation";
    private static final Logger log = LoggerFactory.getLogger(OpenMeteoClient.class);
    private final RestClient restClient;

    public OpenMeteoClient(){
        restClient= RestClient.builder()
                .baseUrl(ENTRY_POINT)
                .build();
    }

    public OpenMeteoWeather getForecast(double lon, double lat){
        log.info("Getting Open Meteo forecast. Longitude: {}, Latitude: {}", lon, lat);
        try{
            return restClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/v1/forecast")
                            .queryParam("latitude", lat)
                            .queryParam("longitude", lon)
                            .queryParam("hourly", PARAMETERS)
                            .queryParam("timezone", "auto")
                            .build())
                    .retrieve()
                    .onStatus(status -> status.value() == 400,
                            (request, response) ->
                            {throw new OpenMeteoBadRequestException("Invalid request to Open-Meteo");
                            })
                    .onStatus(status -> status.value() == 429,
                            (request, response) ->
                            {throw new OpenMeteoRateLimitException("OpenMeteo rate limit reached");
                            })
                    .onStatus(HttpStatusCode::is5xxServerError,
                            (request, response) ->
                            {throw new OpenMeteoUnavailabeException("OpenMeteo server error: " + response.getStatusCode());
                            })
                    .body(OpenMeteoWeather.class);
        }catch(ResourceAccessException e){
            log.warn("OpenMeteo client error");
            throw new OpenMeteoUnavailabeException("OpenMeteo connection failed");
        }
    }

}
