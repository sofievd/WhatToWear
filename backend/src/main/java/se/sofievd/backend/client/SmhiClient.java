package se.sofievd.backend.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import se.sofievd.backend.data.smhi.SmhiWeather;

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
        String uri = "/geotype/point/lon/" + lon + "/lat/" + lat + "/data.json?parameters=" +PARAMETERS;
        log.info("latitude: {}, longitude: {}", lat, lon);
        log.info("parameters: {}", PARAMETERS);
        log.info(uri);
        return restClient.get()
                .uri("/geotype/point/lon/{lon}/lat/{lat}/data.json?parameters={}", lon, lat,PARAMETERS)
                .retrieve()
                .body(SmhiWeather.class);

    }


}
