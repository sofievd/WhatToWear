package se.sofievd.backend.weather.smhi.model;

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
        "predominant_precipitation_type_at_surface",
        "symbol_code"
})
public class SmhiData {

    @JsonProperty("air_temperature")
    private Double airTemperature;

    @JsonProperty("wind_speed")
    private Double windSpeed;

    @JsonProperty("relative_humidity")
    private Integer relativeHumidity;

    @JsonProperty("precipitation_amount_min")
    private Double minPrecipitation;

    @JsonProperty("precipitation_amount_max")
    private Double maxPrecipitation;

    @JsonProperty("predominant_precipitation_type_at_surface")
    private Integer typeOfPrecipitation;

    @JsonProperty("symbol_code")
    private Integer symbolCode;

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
    public Double getMinPrecipitation() {
        return minPrecipitation;
    }

    @JsonProperty("precipitation_amount_min")
    public void setMinPrecipitation(Double precipitationAmountMin) {
        this.minPrecipitation = precipitationAmountMin;
    }

    @JsonProperty("precipitation_amount_max")
    public Double getMaxPrecipitation() {
        return maxPrecipitation;
    }

    @JsonProperty("precipitation_amount_max")
    public void setMaxPrecipitation(Double precipitationAmountMax) {
        this.maxPrecipitation = precipitationAmountMax;
    }

    @JsonProperty("predominant_precipitation_type_at_surface")
    public Integer getTypeOfPrecipitation() {
        return typeOfPrecipitation;
    }

    @JsonProperty("predominant_precipitation_type_at_surface")
    public void setTypeOfPrecipitation(Integer predominantPrecipitationTypeAtSurface) {
        this.typeOfPrecipitation = predominantPrecipitationTypeAtSurface;
    }

    @JsonProperty("symbol_code")
    public Integer getSymbolCode() {
        return symbolCode;
    }

    @JsonProperty("symbol_code")
    public void setSymbolCode(Integer symbolCode) {
        this.symbolCode = symbolCode;
    }

    @Override
    public String toString() {
        return "SmhiData{" +
                "airTemperature=" + airTemperature +
                ", windSpeed=" + windSpeed +
                ", relativeHumidity=" + relativeHumidity +
                ", precipitationAmountMin=" + minPrecipitation +
                ", precipitationAmountMax=" + maxPrecipitation +
                ", predominantPrecipitationTypeAtSurface=" + typeOfPrecipitation +
                ", symbolCode=" + symbolCode +
                '}';
    }
}
