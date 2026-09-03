package se.sofievd.backend.weather.smhi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;
import se.sofievd.backend.common.exception.SmhiApiException;
import se.sofievd.backend.common.exception.SmhiBadRequestException;
import se.sofievd.backend.common.exception.SmhiRateLimitException;
import se.sofievd.backend.common.exception.SmhiUnavailableException;
import se.sofievd.backend.weather.smhi.model.SmhiPolygon;
import se.sofievd.backend.weather.smhi.model.SmhiWeather;

@Service
public class SmhiClient {
    private final static String ENTRY_POINT = "https://opendata-download-metfcst.smhi.se/api/category/snow1g/version/1";
    private static final Logger log = LoggerFactory.getLogger(SmhiClient.class);
    private static final String PARAMETERS = "air_temperature,wind_speed,relative_humidity,precipitation_amount_max,precipitation_amount_min,predominant_precipitation_type_at_surface,symbol_code";
    private final RestClient restClient;

    public SmhiClient() {
        restClient = RestClient.builder()
                .baseUrl(ENTRY_POINT)
                .build();
    }

    public SmhiWeather getForecast(double lon, double lat) {
        log.info("getting SMHI forecast. longitude: {}, lattitude: {}", lon, lat);
        try {
            return restClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/geotype/point/lon/{lon}/lat/{lat}/data.json")
                            .queryParam("parameters", PARAMETERS)
                            .build(lon, lat))
                    .retrieve()
                    .onStatus(status -> status.value() == 400 ,
                            ((request, response) -> {
                                throw new SmhiBadRequestException("SMHI rejected the coordinates/request");
                            })
                            )
                    .onStatus( status -> status.value() == 429,
                            ((request, response) -> {
                                throw new SmhiRateLimitException("SMHI rate limit reached");
                            })
                    )
                    .onStatus(HttpStatusCode::is5xxServerError,
                            ((request, response) -> {
                                throw new SmhiUnavailableException( "SMHI server error: " + response.getStatusCode());
                            }))
                    .body(SmhiWeather.class);
        }catch (ResourceAccessException e){
            log.warn("SMHI client error.");
            throw new SmhiApiException("SMHI connection failed");
        }

    }

    public SmhiPolygon getForecastArea(){
        return restClient.get()
                .uri("/geotype/polygon.json")
                .retrieve()
                .body(SmhiPolygon.class);
    }


}
