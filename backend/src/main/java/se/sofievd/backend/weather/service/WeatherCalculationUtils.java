package se.sofievd.backend.weather.service;

public class WeatherCalculationUtils {

    private WeatherCalculationUtils(){

    }

    public static Double calculateAveragePrecipitation(Double max, Double min) {
        return (max + min) / 2;
    }

    public static Double calculateFeelLikeTemperature(Double airTemperature, Integer humidity, Double windSpeed) {
        double pressure = ((double) humidity / 100)
                * 6.105
                * Math.exp((17.27 * airTemperature) / (237.7 + airTemperature));

        return airTemperature + (0.33 * pressure) - (0.7 * windSpeed) - 4.0;
    }

    public static Double calculateWindSpeed(Double windSpeed){
        return (windSpeed * 3.6);
    }

    public static String getPrecipitationDescription(Double precipitation) {
        if ( precipitation == 0)return "No rain";
        if (precipitation < 1) return "Very light rain";
        if (precipitation < 5) return "Light rain";
        if (precipitation < 10) return "Moderate rain";
        if (precipitation < 20) return "Heavy rain";
        return "Very heavy rain";
    }
}
