package se.sofievd.backend.weather.smhi.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.time.OffsetDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "time",
        "intervalParametersStartTime",
        "data"
})
public class TimeSerie {
    @JsonProperty("time")
    private OffsetDateTime time;

    @JsonProperty("intervalParametersStartTime")
    private OffsetDateTime intervalParameterStartTime;

    @JsonProperty("data")
    private SmhiData data;

    @JsonProperty("time")
    public OffsetDateTime getTime() {
        return time;
    }

    @JsonProperty("time")
    public void setTime(OffsetDateTime time) {
        this.time = time;
    }

    @JsonProperty("intervalParametersStartTime")
    public OffsetDateTime getIntervalParameterStartTime() {
        return intervalParameterStartTime;
    }

    @JsonProperty("intervalParametersStartTime")
    public void setIntervalParameterStartTime(OffsetDateTime intervalParameterStartTime) {
        this.intervalParameterStartTime = intervalParameterStartTime;
    }

    @JsonProperty("data")
    public SmhiData getData() {
        return data;
    }

    @JsonProperty("data")
    public void setData(SmhiData data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "TimeSerie{" +
                "time='" + time + '\'' +
                ", intervalParameterStartTime='" + intervalParameterStartTime + '\'' +
                ", data=" + data +
                '}';
    }
}