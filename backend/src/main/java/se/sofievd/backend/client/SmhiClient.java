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
    private final RestClient restClient;

    public SmhiClient() {
        restClient = RestClient.builder()
                .baseUrl(ENTRY_POINT)
                .build();
    }

    public SmhiWeather getForecast(double lat, double lon) {
        log.info("latitude: {}, longitude: {}", lat, lon);
        return restClient.get()
                .uri("/geotype/point/lon/{lon}/lat/{lat}/data.json?parameters=air_temperature,wind_speed,relative_humidity,predominant_precipitation_type_at_surface,precipitation_amount_mean,precipitation_amount_max,precipitation_amount_min", lon, lat)
                .retrieve()
                .body(SmhiWeather.class);

    }


}
