package se.sofievd.backend.weather.openmeteo.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "latitude",
        "longitude",
        "timezone",
        "hourly"
})
public class OpenMeteoWeather {

    @JsonProperty("latitude")
    private Double latitude;

    @JsonProperty("longitude")
    private Double longitude;

    @JsonProperty("timezone")
    private String timezone;

    @JsonProperty("hourly")
    private OpenMeteoData openMeteoData;

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

    @JsonProperty("hourly")
    public OpenMeteoData getOpenMeteoData() {
        return openMeteoData;
    }

    @JsonProperty("hourly")
    public void setOpenMeteoData(OpenMeteoData openMeteoData) {
        this.openMeteoData = openMeteoData;
    }

    @Override
    public String toString() {
        return "OpenMeteoWeather{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                ", timezone='" + timezone + '\'' +
                ", openMeteoData=" + openMeteoData +
                '}';
    }
}
