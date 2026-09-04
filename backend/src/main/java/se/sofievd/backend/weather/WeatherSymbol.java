package se.sofievd.backend.weather;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

public enum WeatherSymbol {

    CLEAR_SKY(1, "Clear sky"),
    NEARLY_CLEAR_SKY(2, "Nearly clear sky" ),
    VARIABLE_CLOUDINESS(3, "Variable cloudiness" ),
    HALFCLEAR_SKY(4, "Halfclear sky"),
    CLOUDY_SKY(5, "Cloudy sky"),
    OVERCAST(6, "Overcast"),
    FOG(7, "Fog"),

    // rain
    LIGHT_RAIN_SHOWERS(8, "Light rain showers"),
    MODERATE_RAIN_SHOWERS(9, "Moderate rain showers"),
    HEAVY_RAIN_SHOWERS(10, "Heavy rain showers"),
    THUNDERSTORM(11, "Thunderstorm"),

    // Sleet
    LIGHT_SLEET_SHOWERS(12, "Light sleet showers"),
    MODERATE_SLEET_SHOWERS(13, "Moderate sleet showers"),
    HEAVY_SLEET_SHOWERS(14, "Heavy sleet showers"),

    // Snow
    LIGHT_SNOW_SHOWERS(15, "Light snow showers"),
    MODERATE_SNOW_SHOWERS(16, "Moderate snow showers"),
    HEAVY_SNOW_SHOWERS(17, "Heavy snow showers"),

    // Continu rain
    LIGHT_RAIN(18, "Light rain"),
    MODERATE_RAIN(19, "Moderate rain"),
    HEAVY_RAIN(20, "Heavy rain"),
    THUNDER(21, "Thunder"),
    LIGHT_SLEET(22, "Light sleet"),
    MODERATE_SLEET(23, "Moderate sleet"),
    HEAVY_SLEET(24, "Heavy sleet"),
    LIGHT_SNOWFALL(25, "Light snowfall"),
    MODERATE_SNOWFALL(26, "Moderate snowfall"),
    HEAVY_SNOWFALL(27, "Heavy snowfall");

    private final int code;
    private final String description;

    WeatherSymbol(int code, String description) {
        this.code = code;
        this.description = description;

    }

    // Zorgt ervoor dat Jackson het getal uit de SMHI JSON automatisch omzet naar dit Enum type
    @JsonCreator
    public static WeatherSymbol fromCode(int code) {
        return Arrays.stream(values())
                .filter(symbol -> symbol.code == code)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown SMHI weer-code: " + code));
    }

    public static WeatherSymbol fromWmoCode(int code) {
        return switch (code) {
            case 0 -> CLEAR_SKY;
            case 1 -> NEARLY_CLEAR_SKY;
            case 2 -> VARIABLE_CLOUDINESS;
            case 3 -> OVERCAST;
            case 45, 48 -> FOG;
            case 51, 53, 55, 56, 57 -> LIGHT_RAIN;
            case 61, 63, 66, 80, 81 -> MODERATE_RAIN;
            case 65, 67, 82 -> HEAVY_RAIN;
            case 71, 77, 85 -> LIGHT_SNOWFALL;
            case 73, 86 -> MODERATE_SNOWFALL;
            case 75 -> HEAVY_SNOWFALL;
            case 95, 96, 99 -> THUNDERSTORM;
            default -> throw new IllegalArgumentException("Unknown WMO weather code: " + code);
        };
    }

    @JsonValue
    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }


}
