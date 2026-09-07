package se.sofievd.backend.weather;

import org.springframework.stereotype.Service;
import se.sofievd.backend.weather.openmeteo.OpenMeteoClient;
import se.sofievd.backend.weather.openmeteo.model.OpenMeteoCurrent;
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

    public Weather getGlobalWeather(Double longitude, Double latitude) {
        OpenMeteoCurrent current = openMeteoClient
                .getForecast(longitude, latitude)
                .getCurrent();
        WeatherSymbol symbol = WeatherSymbol.fromWmoCode(current.getWeatherCode());

        return new Weather(
                current.getTemperature(),
                current.getApparentTemperature(),
                current.getWindSpeed(),
                current.getRelativeHumidity(),
                symbol.getCode(),
                symbol,
                current.getPrecipitation(),
                WeatherCalculationUtils.getPrecipitationDescription(current.getPrecipitation()),
                LocalDateTime.now()
        );
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
        Double windSpeed = WeatherCalculationUtils.calculateWindSpeed(weatherNow.getWindSpeed());
        Double precipitation = WeatherCalculationUtils.calculateAveragePrecipitation(weatherNow.getMaxPrecipitation(), weatherNow.getMinPrecipitation());
        Double feelLikeTemperature = WeatherCalculationUtils.calculateFeelLikeTemperature(temperature, humidity, windSpeed);
        Integer symbolCode = weatherNow.getSymbolCode();
        WeatherSymbol weatherSymbol = WeatherSymbol.fromCode(symbolCode);
        String precipitationDescription = WeatherCalculationUtils.getPrecipitationDescription(precipitation);
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


}
