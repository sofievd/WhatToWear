package se.sofievd.backend.data.smhi;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "cratedTime",
        "referenceTime",
        "geometry",
        "timeSeries"
})
public class SmhiWeather {
    @JsonProperty("createdTime")
    private String createdTime;

    @JsonProperty("referenceTime")
    private String referenceTime;

    @JsonProperty("geometry")
    private Geometry geometry;

    @JsonProperty("timeSeries")
    private List<TimeSerie> timeSeries;

    @JsonProperty("createdTime")
    public String getCreatedTime() {
        return createdTime;
    }

    @JsonProperty("createdTime")
    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    @JsonProperty("referenceTime")
    public String getReferenceTime() {
        return referenceTime;
    }

    @JsonProperty("referenceTime")
    public void setReferenceTime(String referenceTime) {
        this.referenceTime = referenceTime;
    }

    @JsonProperty("geometry")
    public Geometry getGeometry() {
        return geometry;
    }

    @JsonProperty("geometry")
    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }

    @JsonProperty("timeSeries")
    public List<TimeSerie> getTimeSeries() {
        return timeSeries;
    }

    @JsonProperty("timeSeries")
    public void setTimeSeries(List<TimeSerie> timeSeries) {
        this.timeSeries = timeSeries;
    }

    @Override
    public String toString() {
        return "SmhiWeather{" +
                "createdTime='" + createdTime + '\'' +
                ", referenceTime='" + referenceTime + '\'' +
                ", geometry=" + geometry +
                ", timeSeries=" + timeSeries +
                '}';
    }
}
