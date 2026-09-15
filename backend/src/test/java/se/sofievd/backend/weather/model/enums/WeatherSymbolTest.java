package se.sofievd.backend.weather.model.enums;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WeatherSymbolTest {

    @Test
    public void fromCode_ValidSmhiCode_ReturnsCorrectEnum() {
        // Code 1 -> CLEAR_SKY
        WeatherSymbol symbol = WeatherSymbol.fromCode(1);
        assertEquals(WeatherSymbol.CLEAR_SKY, symbol);
        assertEquals("Clear sky", symbol.getDescription());
    }

    @Test
    public void fromCode_SnowfallCode_ReturnsCorrectEnum() {
        // Code 25 -> LIGHT_SNOWFALL
        WeatherSymbol symbol = WeatherSymbol.fromCode(25);
        assertEquals(WeatherSymbol.LIGHT_SNOWFALL, symbol);
        assertEquals("Light snowfall", symbol.getDescription());
    }

    @Test
    public void fromCode_UnknownCode_ThrowsIllegalArgumentException() {
        // Code 999 is invalid for SMHI codes
        assertThrows(IllegalArgumentException.class, () -> {
            WeatherSymbol.fromCode(999);
        });
    }

    @Test
    public void fromWmoCode_ClearSkyCodeZero_ReturnsClearSky() {
        WeatherSymbol symbol = WeatherSymbol.fromWmoCode(0);
        assertEquals(WeatherSymbol.CLEAR_SKY, symbol);
    }

    @Test
    public void fromWmoCode_FogCodes_ReturnsFog() {
        assertEquals(WeatherSymbol.FOG, WeatherSymbol.fromWmoCode(45));
        assertEquals(WeatherSymbol.FOG, WeatherSymbol.fromWmoCode(48));
    }

    @Test
    public void fromWmoCode_ThunderstormCodes_ReturnsThunderstorm() {
        assertEquals(WeatherSymbol.THUNDERSTORM, WeatherSymbol.fromWmoCode(95));
        assertEquals(WeatherSymbol.THUNDERSTORM, WeatherSymbol.fromWmoCode(96));
        assertEquals(WeatherSymbol.THUNDERSTORM, WeatherSymbol.fromWmoCode(99));
    }

    @Test
    public void fromWmoCode_UnknownWmoCode_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> {
            WeatherSymbol.fromWmoCode(999);
        });
    }

    @Test
    public void getCode_ReturnsCorrectSmhiIntegerCode() {
        assertEquals(1, WeatherSymbol.CLEAR_SKY.getCode());
        assertEquals(6, WeatherSymbol.OVERCAST.getCode());
    }

    @Test
    public void getDescription_ReturnsCorrectTextDescription() {
        assertEquals("Overcast", WeatherSymbol.OVERCAST.getDescription());
        assertEquals("Light rain showers", WeatherSymbol.LIGHT_RAIN_SHOWERS.getDescription());
    }

}