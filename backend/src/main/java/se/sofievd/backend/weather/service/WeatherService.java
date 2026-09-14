package se.sofievd.backend.weather.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import se.sofievd.backend.common.exception.LocationNotSupportedException;
import se.sofievd.backend.weather.WeatherCalculationUtils;
import se.sofievd.backend.weather.WeatherSymbol;
import se.sofievd.backend.weather.model.TimeSlot;
import se.sofievd.backend.weather.model.Weather;
import se.sofievd.backend.weather.model.WeatherTimeSlot;
import se.sofievd.backend.weather.openmeteo.OpenMeteoClient;
import se.sofievd.backend.weather.openmeteo.model.OpenMeteoCurrent;
import se.sofievd.backend.weather.openmeteo.model.OpenMeteoData;
import se.sofievd.backend.weather.openmeteo.model.OpenMeteoWeather;
import se.sofievd.backend.weather.smhi.SmhiAreaService;
import se.sofievd.backend.weather.smhi.SmhiClient;
import se.sofievd.backend.weather.smhi.model.SmhiData;
import se.sofievd.backend.weather.smhi.model.SmhiWeather;
import se.sofievd.backend.weather.smhi.model.TimeSerie;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class WeatherService {


    private static final Logger log = LoggerFactory.getLogger(WeatherService.class);
    private final SmhiClient smhiClient;
    private final SmhiAreaService areaService;
    private final OpenMeteoClient openMeteoClient;
    private static final ZoneId SWEDEN_ZONE = ZoneId.of("Europe/Stockholm");

    public WeatherService(SmhiClient smhiClient, SmhiAreaService areaService, OpenMeteoClient openMeteoClient) {
        this.smhiClient = smhiClient;
        this.areaService = areaService;
        this.openMeteoClient = openMeteoClient;
    }

//    public Weather getSmhiWeather(Double longitude, Double latitude) {
//        if (!areaService.isInsideArea(longitude, latitude)) {
//            throw new LocationNotSupportedException(
//                    "Location is outside the SMHI forecast area"
//            );
//        }
//
//        SmhiWeather smhiWeather = smhiClient.getForecast(longitude, latitude);
//        return getNowWeather(smhiWeather);
//    }

    public List<WeatherTimeSlot> getAllDayWeather(Double longitude, Double latitude) {
        OpenMeteoWeather openMeteoWeather = openMeteoClient.getForecast(longitude, latitude);
        OpenMeteoData data = openMeteoWeather.getHourly();

        List<Weather> weatherList = new ArrayList<>();

        for (int i = 0; i < data.getTime().size(); i++) {
            Weather weather = createWeather(data, i);
            weatherList.add(weather);
        }

        return getWeatherTimeSlots(weatherList);
    }

    // renamed to getCurrentWeather
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

    private Weather createWeather(OpenMeteoData data, int i) {
        Weather weather = new Weather();
        weather.setTime(data.getTime().get(i));
        weather.setAirTemperature(data.getTemperature().get(i));
        weather.setPrecipitation(data.getPrecipitation().get(i));
        weather.setFeelsLikeTemperature(data.getApparentTemperature().get(i));
        weather.setWindSpeed(data.getWindSpeed().get(i));
        weather.setHumidity(data.getRelativeHumidity().get(i));
        weather.setCondition(data.getWeatherCode().get(i));
        weather.setUvIndex(data.getUvIndex().get(i));

        return weather;
    }

    private List<WeatherTimeSlot> getWeatherTimeSlots(List<Weather> weatherList) {
        List<WeatherTimeSlot> weatherTimeSlots = new ArrayList<>();
        Map<TimeSlot, List<Weather>> weatherByTimeSlot = new HashMap<>();

        for(Weather weather: weatherList){
            LocalTime time = weather.getTime().toLocalTime();
//            LocalTime time = LocalTime.of(weather.getTime().getHour(), weather.getTime().getMinute());
            TimeSlot timeSlot = TimeSlot.from(time);
            weatherByTimeSlot
                    .computeIfAbsent(timeSlot, list -> new ArrayList<>())
                    .add(weather);
        }

        for(TimeSlot timeSlot: TimeSlot.values()){
            List<Weather> weatherForTimeSlot = weatherByTimeSlot.get(timeSlot);

            if(weatherForTimeSlot == null || weatherForTimeSlot.isEmpty()){
                throw new IllegalArgumentException("No weather data for time slot: " + timeSlot);
            }
            weatherTimeSlots.add(createWeatherTimeSlot(weatherForTimeSlot, timeSlot));
        }

        return weatherTimeSlots;
    }

    private WeatherTimeSlot createWeatherTimeSlot(List<Weather> weatherForTimeSlot, TimeSlot timeSlot) {
        if (weatherForTimeSlot.isEmpty()) {
            throw new IllegalArgumentException("weatherForTimeSlot cannot be empty");
        }

        WeatherTimeSlot weatherTimeSlot = new WeatherTimeSlot();
        weatherTimeSlot.setTimeSlot(timeSlot);

        Map<Integer, Integer> counts = new HashMap<>();

        double airTemperatureSum = 0;
        Integer humiditySum = 0;
        double maxWindSpeed = 0;
        double minFeelsLikeTemperature = Double.POSITIVE_INFINITY;
        double maxFeelsLikeTemperature = 0;
        double precipitationSum = 0;
        double maxUvIndex = 0;
        boolean isSunny = false;

        Integer mostFrequentCondition = weatherForTimeSlot.get(0).getCondition();
        int highestCount = 0;

        for (Weather w : weatherForTimeSlot) {
            airTemperatureSum += w.getAirTemperature();
            humiditySum += w.getHumidity();
            precipitationSum += w.getPrecipitation();

            maxWindSpeed = Math.max(maxWindSpeed, w.getWindSpeed());
            maxFeelsLikeTemperature = Math.max(maxFeelsLikeTemperature, w.getFeelsLikeTemperature());
            minFeelsLikeTemperature = Math.min(minFeelsLikeTemperature, w.getFeelsLikeTemperature());
            maxUvIndex = Math.max(maxUvIndex, w.getUvIndex());

            counts.put(w.getCondition(), counts.getOrDefault(w.getCondition(), 0) + 1);
        }

        for(Map.Entry<Integer,Integer> entry: counts.entrySet()){
            if(entry.getValue() > highestCount){
                mostFrequentCondition = entry.getKey();
                highestCount = entry.getValue();
            }
        }

        double averageAirTemperature = airTemperatureSum / weatherForTimeSlot.size();
        Integer averageHumidity =   (int) Math.round((double) humiditySum / weatherForTimeSlot.size());
        WeatherSymbol symbol = WeatherSymbol.fromWmoCode(mostFrequentCondition);

        if(symbol.getCode() == 0 || symbol.getCode() == 1){
            isSunny = true;
        }

        weatherTimeSlot.setAirTemperature(averageAirTemperature);
        weatherTimeSlot.setMinFeelsLikeTemperature(minFeelsLikeTemperature);
        weatherTimeSlot.setMaxFeelsLikeTemperature(maxFeelsLikeTemperature);
        weatherTimeSlot.setHumidity(averageHumidity);
        weatherTimeSlot.setWindSpeed(maxWindSpeed);
        weatherTimeSlot.setCondition(mostFrequentCondition);
        weatherTimeSlot.setPrecipitation(precipitationSum);
        weatherTimeSlot.setConditionDescription(symbol);
        weatherTimeSlot.setPrecipitationDescription(WeatherCalculationUtils.getPrecipitationDescription(precipitationSum));
        weatherTimeSlot.setSunny(isSunny);
        weatherTimeSlot.setMaxUvIndex(maxUvIndex);

        return weatherTimeSlot;
    }

//    private List<TimeSerie> getTodayTimes(List<TimeSerie> timeSerieList) {
//        LocalDate today = LocalDate.now(SWEDEN_ZONE);
//
//        return timeSerieList.stream()
//                .filter(t -> t.getIntervalParameterStartTime()
//                        .atZoneSameInstant(SWEDEN_ZONE)
//                        .toLocalDate()
//                        .equals(today))
//                .toList();
//    }
//
//    private Weather getNowWeather(SmhiWeather smhiWeather) {
//        TimeSerie timeSerieNow = smhiWeather.getTimeSeries().get(0);
//        SmhiData weatherNow = timeSerieNow.getData();
//        Double temperature = weatherNow.getAirTemperature();
//        Integer humidity = weatherNow.getRelativeHumidity();
//        Double windSpeed = WeatherCalculationUtils.calculateWindSpeed(weatherNow.getWindSpeed());
//        Double precipitation = WeatherCalculationUtils.calculateAveragePrecipitation(weatherNow.getMaxPrecipitation(), weatherNow.getMinPrecipitation());
//        Double feelLikeTemperature = WeatherCalculationUtils.calculateFeelLikeTemperature(temperature, humidity, windSpeed);
//        Integer symbolCode = weatherNow.getSymbolCode();
//        WeatherSymbol weatherSymbol = WeatherSymbol.fromCode(symbolCode);
//        String precipitationDescription = WeatherCalculationUtils.getPrecipitationDescription(precipitation);
//        return new Weather(temperature,
//                feelLikeTemperature,
//                windSpeed,
//                humidity,
//                symbolCode,
//                weatherSymbol,
//                precipitation,
//                precipitationDescription,
//                LocalDateTime.now()
//        );
//    }


}
