package se.sofievd.backend.weather.model;

import se.sofievd.backend.weather.WeatherSymbol;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class WeatherTimeSlot {
    private TimeSlot timeSlot;
    private Double airTemperature;
    private Double minFeelsLikeTemperature;
    private Double maxFeelsLikeTemperature;
    private Double windSpeed;
    private Integer humidity;
    private Integer condition;
    private WeatherSymbol conditionDescription;
    private Double precipitation;
    private String precipitationDescription;

    public TimeSlot getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(TimeSlot timeSlot) {
        this.timeSlot = timeSlot;
    }

    public Double getAirTemperature() {
        return airTemperature;
    }

    public void setAirTemperature(Double airTemperature) {
        this.airTemperature = airTemperature;
    }

    public Double getMinFeelsLikeTemperature() {
        return minFeelsLikeTemperature;
    }

    public void setMinFeelsLikeTemperature(Double minFeelsLikeTemperature) {
        this.minFeelsLikeTemperature = minFeelsLikeTemperature;
    }

    public Double getMaxFeelsLikeTemperature() {
        return maxFeelsLikeTemperature;
    }

    public void setMaxFeelsLikeTemperature(Double maxFeelsLikeTemperature) {
        this.maxFeelsLikeTemperature = maxFeelsLikeTemperature;
    }

    public Double getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(Double windSpeed) {
        this.windSpeed = windSpeed;
    }

    public Integer getHumidity() {
        return humidity;
    }

    public void setHumidity(Integer humidity) {
        this.humidity = humidity;
    }

    public Integer getCondition() {
        return condition;
    }

    public void setCondition(Integer condition) {
        this.condition = condition;
    }

    public WeatherSymbol getConditionDescription() {
        return conditionDescription;
    }

    public void setConditionDescription(WeatherSymbol conditionDescription) {
        this.conditionDescription = conditionDescription;
    }

    public Double getPrecipitation() {
        return precipitation;
    }

    public void setPrecipitation(Double precipitation) {
        this.precipitation = precipitation;
    }

    public String getPrecipitationDescription() {
        return precipitationDescription;
    }

    public void setPrecipitationDescription(String precipitationDescription) {
        this.precipitationDescription = precipitationDescription;
    }
}
