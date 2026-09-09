package se.sofievd.backend.weather.model;

import se.sofievd.backend.weather.WeatherSymbol;

import java.time.LocalDateTime;

public class Weather {
    private Double airTemperature;
    private Double feelsLikeTemperature;
    private Double windSpeed;
    private Integer humidity;
    private Integer condition;
    private WeatherSymbol conditionDescription;
    private LocalDateTime time;
    private Double precipitation;
    private String precipitationDescription;

    public Weather() {
    }

    public Weather(Double airTemperature,
                   Double feelsLikeTemperature,
                   Double windSpeed,
                   Integer humidity,
                   Integer condition,
                   WeatherSymbol conditionDescription,
                   Double precipitation,
                   String precipitationDescription,
                   LocalDateTime time) {
        this.airTemperature = airTemperature;
        this.feelsLikeTemperature = feelsLikeTemperature;
        this.windSpeed = windSpeed;
        this.humidity = humidity;
        this.condition = condition;
        this.conditionDescription = conditionDescription;
        this.precipitation = precipitation;
        this.precipitationDescription = precipitationDescription;
        this.time = time;
    }

    public Double getAirTemperature() {
        return airTemperature;
    }

    public void setAirTemperature(Double airTemperature) {
        this.airTemperature = airTemperature;
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

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public Double getFeelsLikeTemperature() {
        return feelsLikeTemperature;
    }

    public void setFeelsLikeTemperature(Double feelsLikeTemperature) {
        this.feelsLikeTemperature = feelsLikeTemperature;
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
