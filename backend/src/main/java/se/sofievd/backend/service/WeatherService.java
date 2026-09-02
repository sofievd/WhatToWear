package se.sofievd.backend.service;

import org.springframework.stereotype.Service;
import se.sofievd.backend.client.SmhiClient;
import se.sofievd.backend.data.Weather;
import se.sofievd.backend.data.smhi.SmhiData;
import se.sofievd.backend.data.smhi.SmhiWeather;
import se.sofievd.backend.data.smhi.TimeSerie;
import se.sofievd.backend.enums.WeatherSymbol;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Service
public class WeatherService {

    private final SmhiClient smhiClient;
    private static final ZoneId SWEDEN_ZONE = ZoneId.of("Europe/Stockholm");

    public WeatherService(SmhiClient smhiClient) {
        this.smhiClient = smhiClient;
    }

    public Weather getWeather(Double longitude, Double latitude) {
        SmhiWeather smhiWeather = smhiClient.getForecast(latitude, longitude);

        return getNowWeather(smhiWeather);
    }

    private List<TimeSerie> getTodayTimes(List<TimeSerie> timeSerieList) {
        LocalDate today = LocalDate.now(SWEDEN_ZONE);

        return timeSerieList.stream()
                .filter(t -> t.getIntervalParameterStartTime()
                        .atZoneSameInstant(SWEDEN_ZONE)
                        .toLocalDate()
                        .equals(today))
                .toList();

    }

    private Weather getNowWeather(SmhiWeather smhiWeather) {
        TimeSerie timeSerieNow = smhiWeather.getTimeSeries().get(0);
        SmhiData weatherNow = timeSerieNow.getData();
        Double temperature = weatherNow.getAirTemperature();
        Integer humidity = weatherNow.getRelativeHumidity();
        Double windSpeed = weatherNow.getWindSpeed();
        Double precipitation = calculateAveragePrecipitation(weatherNow.getMaxPrecipitation(), weatherNow.getMinPrecipitation());
        Double feelLikeTemperature = calculateFeelLikeTemperature(temperature, humidity, windSpeed);
        Integer symbolCode = weatherNow.getSymbolCode();
        WeatherSymbol weatherSymbol = WeatherSymbol.fromCode(symbolCode);
        String precipitationDescription = getPrecipitationDescription(precipitation);
        return new Weather(temperature,
                feelLikeTemperature,
                windSpeed,
                humidity,
                symbolCode,
                weatherSymbol,
                precipitation,
                precipitationDescription,
                LocalDateTime.now()
                );
    }

    private Double calculateAveragePrecipitation(Double max, Double min) {
        return (max + min) / 2;
    }

    private Double calculateFeelLikeTemperature(Double airTemperature, Integer humidity, Double windSpeed) {
        double pressure = ((double) humidity / 100) * 6.105 * Math.exp((17.27 * airTemperature) / (237.7 + airTemperature));
        return airTemperature + (0.33 * pressure) - (0.7 * windSpeed) - 4.0;
    }

    private String getPrecipitationDescription(Double precipitation) {
        if ( precipitation == 0)return "No rain";
        if (precipitation < 1) return "Very light rain";
        if (precipitation < 5) return "Light rain";
        if (precipitation < 10) return "Moderate rain";
        if (precipitation < 20) return "Heavy rain";
        return "Very heavy rain";
    }

}
