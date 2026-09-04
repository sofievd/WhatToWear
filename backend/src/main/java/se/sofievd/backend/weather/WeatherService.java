package se.sofievd.backend.weather;

import org.springframework.stereotype.Service;
import se.sofievd.backend.weather.openmeteo.OpenMeteoClient;
import se.sofievd.backend.weather.openmeteo.model.OpenMeteoWeather;
import se.sofievd.backend.weather.smhi.SmhiClient;
import se.sofievd.backend.common.exception.LocationNotSupportedException;
import se.sofievd.backend.weather.smhi.model.SmhiData;
import se.sofievd.backend.weather.smhi.model.SmhiWeather;
import se.sofievd.backend.weather.smhi.model.TimeSerie;
import se.sofievd.backend.weather.smhi.SmhiAreaService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Service
public class WeatherService {

    private final SmhiClient smhiClient;
    private final SmhiAreaService areaService;
    private final OpenMeteoClient openMeteoClient;
    private static final ZoneId SWEDEN_ZONE = ZoneId.of("Europe/Stockholm");

    public WeatherService(SmhiClient smhiClient, SmhiAreaService areaService, OpenMeteoClient openMeteoClient) {
        this.smhiClient = smhiClient;
        this.areaService = areaService;
        this.openMeteoClient = openMeteoClient;
    }

    public Weather getWeather(Double longitude, Double latitude) {
        if(!areaService.isInsideArea(longitude, latitude)){
            throw new LocationNotSupportedException(
                    "Location is outside the SMHI forecast area"
            );
        }

        SmhiWeather smhiWeather = smhiClient.getForecast(longitude, latitude);
        return getNowWeather(smhiWeather);
    }

    public String getOpenMeteoWeather(Double longitude, Double latitude){
        OpenMeteoWeather openMeteoWeather = openMeteoClient.getForecast(longitude, latitude);
        System.out.println(openMeteoWeather);
       // return openMeteoWeather;
        return "meteo weather gotten";
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
        Double windSpeed = calculateWindSpeed(weatherNow.getWindSpeed());
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

    private Double calculateWindSpeed(Double windSpeed){
        return (windSpeed * 3.6);
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
