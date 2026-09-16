package se.sofievd.backend.weather.service;

import se.sofievd.backend.weather.model.WeatherTimeSlot;

import java.time.LocalTime;
import java.util.List;

public class WeatherUtils {

    private WeatherUtils(){

    }

    public static double calculateAveragePrecipitation(double max, double min) {
        return (max + min) / 2;
    }

    public static double calculateFeelLikeTemperature(double airTemperature, int humidity, double windSpeed) {
        double pressure = ((double) humidity / 100)
                * 6.105
                * Math.exp((17.27 * airTemperature) / (237.7 + airTemperature));

        return airTemperature + (0.33 * pressure) - (0.7 * windSpeed) - 4.0;
    }

    public static double calculateWindSpeed(double windSpeed){
        return (windSpeed * 3.6);
    }

    public static String getPrecipitationDescription(double precipitation) {
        if ( precipitation == 0)return "No rain";
        if (precipitation < 1) return "Very light rain";
        if (precipitation < 5) return "Light rain";
        if (precipitation < 10) return "Moderate rain";
        if (precipitation < 20) return "Heavy rain";
        return "Very heavy rain";
    }

    public static WeatherTimeSlot findCurrentTimeSlot(List<WeatherTimeSlot> slots, LocalTime time) {
        return slots.stream()
                .filter(slot -> slot.getTimeSlot().contains(time))
                .findFirst()
                .orElse(null);
    }

    public static List<WeatherTimeSlot> filterActiveTimeSlots(List<WeatherTimeSlot> timeSlots, LocalTime time) {
        return timeSlots.stream()
                .filter(slot -> slot.getTimeSlot().getEndTime().isAfter(time))
                .toList();
    }
}
