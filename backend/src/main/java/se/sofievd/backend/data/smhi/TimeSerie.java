package se.sofievd.backend.data.smhi;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "time",
        "intervalParametersStartTime",
        "data"
})
public class TimeSerie {
    @JsonProperty("time")
    private String time;

    @JsonProperty("intervalParametersStartTime")
    private String intervalParameterStartTime;

    @JsonProperty("data")
    private SmhiData data;

    @JsonProperty("time")
    public String getTime() {
        return time;
    }

    @JsonProperty("time")
    public void setTime(String time) {
        this.time = time;
    }

    @JsonProperty("intervalParametersStartTime")
    public String getIntervalParameterStartTime() {
        return intervalParameterStartTime;
    }

    @JsonProperty("intervalParametersStartTime")
    public void setIntervalParameterStartTime(String intervalParameterStartTime) {
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