package se.sofievd.backend.weather.openmeteo.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.time.LocalDateTime;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "time",
        "temperature_2m",
        "apparent_temperature",
        "relative_humidity_2m",
        "wind_speed_10m",
        "weather_code",
        "precipitation"
})
public class OpenMeteoData {

    @JsonProperty("time")
    private List<LocalDateTime> time;

    @JsonProperty("temperature_2m")
    private List<Double> temperature;

    @JsonProperty("apparent_temperature")
    private List<Double> apparentTemperature;

    @JsonProperty("relative_humidity_2m")
    private List<Integer> relativeHumidity;

    @JsonProperty("wind_speed_10m")
    private List<Double> windSpeed;

    @JsonProperty("weather_code")
    private List<Integer> weatherCode;

    @JsonProperty("precipitation")
    private List<Double> precipitation;

    @JsonProperty("time")
    public List<LocalDateTime> getTime() {
        return time;
    }

    @JsonProperty("time")
    public void setTime(List<LocalDateTime> time) {
        this.time = time;
    }

    @JsonProperty("temperature_2m")
    public List<Double> getTemperature() {
        return temperature;
    }

    @JsonProperty("temperature_2m")
    public void setTemperature(List<Double> temperature) {
        this.temperature = temperature;
    }

    @JsonProperty("apparent_temperature")
    public List<Double> getApparentTemperature() {
        return apparentTemperature;
    }

    @JsonProperty("apparent_temperature")
    public void setApparentTemperature(List<Double> apparentTemperature) {
        this.apparentTemperature = apparentTemperature;
    }

    @JsonProperty("relative_humidity_2m")
    public List<Integer> getRelativeHumidity() {
        return relativeHumidity;
    }

    @JsonProperty("relative_humidity_2m")
    public void setRelativeHumidity(List<Integer> relativeHumidity) {
        this.relativeHumidity = relativeHumidity;
    }

    @JsonProperty("wind_speed_10m")
    public List<Double> getWindSpeed() {
        return windSpeed;
    }

    @JsonProperty("wind_speed_10m")
    public void setWindSpeed(List<Double> windSpeed) {
        this.windSpeed = windSpeed;
    }

    @JsonProperty("weather_code")
    public List<Integer> getWeatherCode() {
        return weatherCode;
    }

    @JsonProperty("weather_code")
    public void setWeatherCode(List<Integer> weatherCode) {
        this.weatherCode = weatherCode;
    }

    @JsonProperty("precipitation")
    public List<Double> getPrecipitation() {
        return precipitation;
    }

    @JsonProperty("precipitation")
    public void setPrecipitation(List<Double> precipitation) {
        this.precipitation = precipitation;
    }

    @Override
    public String toString() {
        return "OpenMeteoData{" +
                "time=" + time +
                ", temperature=" + temperature +
                ", apparentTemperature=" + apparentTemperature +
                ", relativeHumidity=" + relativeHumidity +
                ", windSpeed=" + windSpeed +
                ", weatherCode=" + weatherCode +
                ", precipitation=" + precipitation +
                '}';
    }
}
