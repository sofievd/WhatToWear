package se.sofievd.backend.data.smhi;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "air_temperature",
        "wind_speed",
        "relative_humidity",
        "precipitation_amount_min",
        "precipitation_amount_max",
        "predominant_precipitation_type_at_surface"
})
public class SmhiData {

    @JsonProperty("air_temperature")
    private Double airTemperature;

    @JsonProperty("wind_speed")
    private Double windSpeed;

    @JsonProperty("relative_humidity")
    private Integer relativeHumidity;

    @JsonProperty("precipitation_amount_min")
    private Double precipitationAmountMin;

    @JsonProperty("precipitation_amount_max")
    private Double precipitationAmountMax;

    @JsonProperty("predominant_precipitation_type_at_surface")
    private Integer predominantPrecipitationTypeAtSurface;

    @JsonProperty("air_temperature")
    public Double getAirTemperature() {
        return airTemperature;
    }

    @JsonProperty("air_temperature")
    public void setAirTemperature(Double airTemperature) {
        this.airTemperature = airTemperature;
    }

    @JsonProperty("wind_speed")
    public Double getWindSpeed() {
        return windSpeed;
    }

    @JsonProperty("wind_speed")
    public void setWindSpeed(Double windSpeed) {
        this.windSpeed = windSpeed;
    }

    @JsonProperty("relative_humidity")
    public Integer getRelativeHumidity() {
        return relativeHumidity;
    }

    @JsonProperty("relative_humidity")
    public void setRelativeHumidity(Integer relativeHumidity) {
        this.relativeHumidity = relativeHumidity;
    }

    @JsonProperty("precipitation_amount_min")
    public Double getPrecipitationAmountMin() {
        return precipitationAmountMin;
    }

    @JsonProperty("precipitation_amount_min")
    public void setPrecipitationAmountMin(Double precipitationAmountMin) {
        this.precipitationAmountMin = precipitationAmountMin;
    }

    @JsonProperty("precipitation_amount_max")
    public Double getPrecipitationAmountMax() {
        return precipitationAmountMax;
    }

    @JsonProperty("precipitation_amount_max")
    public void setPrecipitationAmountMax(Double precipitationAmountMax) {
        this.precipitationAmountMax = precipitationAmountMax;
    }

    @JsonProperty("predominant_precipitation_type_at_surface")
    public Integer getPredominantPrecipitationTypeAtSurface() {
        return predominantPrecipitationTypeAtSurface;
    }

    @JsonProperty("predominant_precipitation_type_at_surface")
    public void setPredominantPrecipitationTypeAtSurface(Integer predominantPrecipitationTypeAtSurface) {
        this.predominantPrecipitationTypeAtSurface = predominantPrecipitationTypeAtSurface;
    }

    @Override
    public String toString() {
        return "SmhiData{" +
                "airTemperature=" + airTemperature +
                ", windSpeed=" + windSpeed +
                ", relativeHumidity=" + relativeHumidity +
                ", precipitationAmountMin=" + precipitationAmountMin +
                ", precipitationAmountMax=" + precipitationAmountMax +
                ", predominantPrecipitationTypeAtSurface=" + predominantPrecipitationTypeAtSurface +
                '}';
    }
}
