package se.sofievd.backend.weather.openmeteo.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OpenMeteoCurrent {
    @JsonProperty("temperature_2m")
    private Double temperature;

    @JsonProperty("apparent_temperature")
    private Double apparentTemperature;

    @JsonProperty("relative_humidity_2m")
    private Integer relativeHumidity;

    @JsonProperty("wind_speed_10m")
    private Double windSpeed;

    @JsonProperty("weather_code")
    private Integer weatherCode;

    @JsonProperty("precipitation")
    private Double precipitation;

    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    public Double getApparentTemperature() {
        return apparentTemperature;
    }

    public void setApparentTemperature(Double apparentTemperature) {
        this.apparentTemperature = apparentTemperature;
    }

    public Integer getRelativeHumidity() {
        return relativeHumidity;
    }

    public void setRelativeHumidity(Integer relativeHumidity) {
        this.relativeHumidity = relativeHumidity;
    }

    public Double getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(Double windSpeed) {
        this.windSpeed = windSpeed;
    }

    public Integer getWeatherCode() {
        return weatherCode;
    }

    public void setWeatherCode(Integer weatherCode) {
        this.weatherCode = weatherCode;
    }

    public Double getPrecipitation() {
        return precipitation;
    }

    public void setPrecipitation(Double precipitation) {
        this.precipitation = precipitation;
    }
}
