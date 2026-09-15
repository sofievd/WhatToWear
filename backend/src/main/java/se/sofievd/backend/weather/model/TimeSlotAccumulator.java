package se.sofievd.backend.weather.model;

import se.sofievd.backend.weather.model.enums.TimeSlot;
import se.sofievd.backend.weather.model.enums.WeatherSymbol;
import se.sofievd.backend.weather.service.WeatherUtils;

import java.util.HashMap;
import java.util.Map;

public class TimeSlotAccumulator {
    private double airTempSum = 0;
    private int humiditySum = 0;
    private double precipitationSum = 0;
    private double maxWindSpeed = 0;
    private double minFeelsLikeTemp = Double.POSITIVE_INFINITY;
    private double maxFeelsLikeTemp = Double.NEGATIVE_INFINITY;
    private double maxUvIndex = 0;
    private int count = 0;
    private final Map<Integer, Integer> conditionCounts = new HashMap<>();

    public void addHour(double temp,
                        int humidity,
                        double precipitation,
                        double feelsLike,
                        double wind,
                        int condition,
                        double uv){
        airTempSum += temp;
        humiditySum += humidity;
        precipitationSum += precipitation;
        maxWindSpeed = Math.max(maxWindSpeed, wind);
        maxFeelsLikeTemp = Math.max(maxFeelsLikeTemp, feelsLike);
        minFeelsLikeTemp = Math.min(minFeelsLikeTemp, feelsLike);
        maxUvIndex = Math.max(maxUvIndex, uv);

        conditionCounts.put(condition, conditionCounts.getOrDefault(condition, 0) + 1);
        count ++;
    }

    public WeatherTimeSlot toWeatherTimeSlot(TimeSlot timeSlot){
        if(count == 0){
          throw new IllegalArgumentException("No weather data for time slot: " + timeSlot);
        }
        int mostFrequentConndition = conditionCounts.entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(0);

        WeatherSymbol symbol = WeatherSymbol.fromWmoCode(mostFrequentConndition);
        boolean isSunny = (symbol.getCode() == 0 || symbol.getCode() == 1);

        WeatherTimeSlot weatherTimeSlot = new WeatherTimeSlot();
        weatherTimeSlot.setTimeSlot(timeSlot);
        weatherTimeSlot.setAirTemperature(airTempSum / count);
        weatherTimeSlot.setMinFeelsLikeTemperature(minFeelsLikeTemp);
        weatherTimeSlot.setMaxFeelsLikeTemperature(maxFeelsLikeTemp);
        weatherTimeSlot.setHumidity(humiditySum / count);
        weatherTimeSlot.setWindSpeed(maxWindSpeed);
        weatherTimeSlot.setCondition(mostFrequentConndition);
        weatherTimeSlot.setPrecipitation(precipitationSum);
        weatherTimeSlot.setConditionDescription(symbol);
        weatherTimeSlot.setPrecipitationDescription(WeatherUtils.getPrecipitationDescription(precipitationSum));
        weatherTimeSlot.setSunny(isSunny);
        weatherTimeSlot.setMaxUvIndex(maxUvIndex);

        return weatherTimeSlot;

    }
}
