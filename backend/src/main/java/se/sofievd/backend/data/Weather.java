package se.sofievd.backend.data;

import java.time.LocalDateTime;

public class Weather {
    private Double airTemperature;
    private Double windSpeed;
    private Double humidity;
    private LocalDateTime date;

    public Weather() {
    }

    public Weather(Double airTemperature, Double windSpeed, Double humidity, LocalDateTime date) {
        this.airTemperature = airTemperature;
        this.windSpeed = windSpeed;
        this.humidity = humidity;
        this.date = date;
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

    public Double getHumidity() {
        return humidity;
    }

    public void setHumidity(Double humidity) {
        this.humidity = humidity;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }
}
