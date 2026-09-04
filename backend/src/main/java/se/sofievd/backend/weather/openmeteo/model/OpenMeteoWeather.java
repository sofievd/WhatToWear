package se.sofievd.backend.weather.openmeteo.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "latitude",
        "longitude",
        "timezone",
        "current",
        "hourly"
})
public class OpenMeteoWeather {

    @JsonProperty("latitude")
    private Double latitude;

    @JsonProperty("longitude")
    private Double longitude;

    @JsonProperty("timezone")
    private String timezone;

    @JsonProperty("current")
    private OpenMeteoCurrent current;

    @JsonProperty("hourly")
    private OpenMeteoData hourly;

    @JsonProperty("latitude")
    public Double getLatitude() {
        return latitude;
    }

    @JsonProperty("latitude")
    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    @JsonProperty("longitude")
    public Double getLongitude() {
        return longitude;
    }

    @JsonProperty("longitude")
    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    @JsonProperty("timezone")
    public String getTimezone() {
        return timezone;
    }

    @JsonProperty("timezone")
    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    @JsonProperty("current")
    public OpenMeteoCurrent getCurrent() {
        return current;
    }

    @JsonProperty("current")
    public void setCurrent(OpenMeteoCurrent current) {
        this.current = current;
    }

    @JsonProperty("hourly")
    public OpenMeteoData getHourly() {
        return hourly;
    }

    @JsonProperty("hourly")
    public void setHourly(OpenMeteoData hourly) {
        this.hourly = hourly;
    }

    @Override
    public String toString() {
        return "OpenMeteoWeather{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                ", timezone='" + timezone + '\'' +
                ", current=" + current +
                ", hourly=" + hourly +
                '}';
    }
}
