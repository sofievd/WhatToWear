package se.sofievd.backend.weather.smhi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;
import se.sofievd.backend.common.exception.SmhiApiException;
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
                    .body(SmhiWeather.class);
        }catch (HttpClientErrorException e){
            log.warn(  "SMHI client error. status={}, body={}",
                    e.getStatusCode(),
                    e.getResponseBodyAsString());
            throw new SmhiApiException("SMHI rejected the request: " + e.getStatusCode());
        }catch (HttpServerErrorException e){
            log.error(
                    "SMHI server error. status={}, body={}",
                    e.getStatusCode(),
                    e.getResponseBodyAsString()
            );
            throw new SmhiUnavailableException("SMHI is currently unavailable");
        } catch (ResourceAccessException e) {
            log.error("Could not connect to SMHI", e);

            throw new SmhiUnavailableException(
                    "Could not connect to SMHI"
            );
        }

    }

    public SmhiPolygon getForecastArea(){
        return restClient.get()
                .uri("/geotype/polygon.json")
                .retrieve()
                .body(SmhiPolygon.class);
    }


}
