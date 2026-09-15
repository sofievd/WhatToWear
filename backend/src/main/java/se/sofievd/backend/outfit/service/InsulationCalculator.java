package se.sofievd.backend.outfit.service;

import org.springframework.stereotype.Service;
import se.sofievd.backend.outfit.model.enums.ActivityLevel;
import se.sofievd.backend.outfit.model.enums.ToleranceLevel;
import se.sofievd.backend.outfit.model.weather.CurrentWeatherState;
import se.sofievd.backend.outfit.model.weather.DailyWeatherMetrics;
import se.sofievd.backend.weather.model.WeatherTimeSlot;

import java.util.List;

@Service
public class InsulationCalculator {

    public Double calculateEffectiveTemperature(Double temperature, Double tolerance){
        return temperature - tolerance;
    }

    public Double calculateTargetClo(Double effectiveTemperature, Double metValue, Double windSpeed){

        Double windSpeedMS = (windSpeed / 3.6);
        // convert Met in w/m^2
        Double M = metValue * 58.2;

        Double totalInsulationRequired = (33.0 - effectiveTemperature) / (0.155 * M);

        Double airInsulation = 0.7 - Math.min(0.5, 0.1 * windSpeed);

        Double targetClothingClo = totalInsulationRequired - airInsulation;

        return Math.max(targetClothingClo, 0.15);

    }

    public CurrentWeatherState analyzeCurrentWeather(WeatherTimeSlot currentSlot, ToleranceLevel tolerance, ActivityLevel activity) {
        if (currentSlot == null) {
            return new CurrentWeatherState(0, 0, 0, 0,false);
        }
        double temperature = calculateEffectiveTemperature(
                currentSlot.getMinFeelsLikeTemperature(),
                tolerance.getTemperatureOffset());

        double currentCloTargetValue = calculateTargetClo(
                temperature,
                activity.getMetValue(),
                currentSlot.getWindSpeed());
        return new CurrentWeatherState(
                currentCloTargetValue,
                temperature,
                currentSlot.getMinFeelsLikeTemperature(),
                currentSlot.getWindSpeed(),
                currentSlot.isSunny()
        );
    }

    public DailyWeatherMetrics calculateDailyMetrics(List<WeatherTimeSlot> activeTimeSlots, ToleranceLevel tolerance, ActivityLevel activity) {

        double minTemp = Double.MAX_VALUE;
        double maxTemp = -Double.MAX_VALUE;
        double precipitation = 0;
        double minClo = Double.MAX_VALUE;
        double maxClo = 0;

        boolean needSunglasses = false;
        boolean needBeanie = false;
        boolean needGloves = false;
        boolean needScarf = false;

        for (WeatherTimeSlot timeSlot : activeTimeSlots) {
            double temperature = calculateEffectiveTemperature(timeSlot.getMinFeelsLikeTemperature(), tolerance.getTemperatureOffset());
            double targetCloValue = calculateTargetClo(temperature, activity.getMetValue(), timeSlot.getWindSpeed());

            minTemp = Math.min(timeSlot.getMinFeelsLikeTemperature(), minTemp);
            maxTemp = Math.max(maxTemp, timeSlot.getMaxFeelsLikeTemperature());
            precipitation += timeSlot.getPrecipitation();
            minClo = Math.min(minClo, targetCloValue);
            maxClo = Math.max(maxClo, targetCloValue);

            if (timeSlot.isSunny() || timeSlot.getMaxUvIndex() >= 3) {
                needSunglasses = true;
            }
            if (timeSlot.getMinFeelsLikeTemperature() < 5.0 ||
                    (timeSlot.getMinFeelsLikeTemperature() < 12 && timeSlot.getWindSpeed() > 20.0)) {
                needScarf = true;
            }
            if (timeSlot.getMinFeelsLikeTemperature() < 8) {
                needBeanie = true;
            }
            if (timeSlot.getMinFeelsLikeTemperature() < 8) {
                needGloves = true;
            }
        }
        return new DailyWeatherMetrics(
                minTemp,
                maxTemp,
                precipitation,
                minClo,
                maxClo,
                needSunglasses,
                needBeanie,
                needGloves,
                needScarf);

    }

}