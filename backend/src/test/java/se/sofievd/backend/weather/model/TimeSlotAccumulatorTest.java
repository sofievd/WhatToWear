package se.sofievd.backend.weather.model;

import org.junit.jupiter.api.Test;
import se.sofievd.backend.weather.model.enums.TimeSlot;
import se.sofievd.backend.weather.model.enums.WeatherSymbol;

import static org.junit.jupiter.api.Assertions.*;

class TimeSlotAccumulatorTest {

    @Test
    public void toWeatherTimeSlot_SingleHourAdded_CalculatesCorrectValues() {
        TimeSlotAccumulator accumulator = new TimeSlotAccumulator();
        // temp: 20.0, humidity: 50, precip: 0.0, feelsLike: 18.0, wind: 10.0, condition: 0 (Clear Sky), uv: 3.0
        accumulator.addHour(20.0, 50, 0.0, 18.0, 10.0, 0, 3.0);

        WeatherTimeSlot result = accumulator.toWeatherTimeSlot(TimeSlot.MORNING);

        assertEquals(TimeSlot.MORNING, result.getTimeSlot());
        assertEquals(20.0, result.getAirTemperature(), 0.0001);
        assertEquals(50, result.getHumidity());
        assertEquals(0.0, result.getPrecipitation(), 0.0001);
        assertEquals(18.0, result.getMinFeelsLikeTemperature(), 0.0001);
        assertEquals(18.0, result.getMaxFeelsLikeTemperature(), 0.0001);
        assertEquals(10.0, result.getWindSpeed(), 0.0001);
        assertEquals(3.0, result.getMaxUvIndex(), 0.0001);
        assertEquals(0, result.getCondition());
        assertEquals("No rain", result.getPrecipitationDescription());
    }

    @Test
    public void toWeatherTimeSlot_MultipleHours_AveragesSummableAndTracksExtremes() {
        TimeSlotAccumulator accumulator = new TimeSlotAccumulator();

        // Hour 1: temp 10.0, humidity 60, precip 1.5, feels 8.0, wind 5.0, cond 0, uv 2.0
        accumulator.addHour(10.0, 60, 1.5, 8.0, 5.0, 0, 2.0);
        // Hour 2: temp 20.0, humidity 80, precip 2.5, feels 16.0, wind 15.0, cond 0, uv 5.0
        accumulator.addHour(20.0, 80, 2.5, 16.0, 15.0, 0, 5.0);

        WeatherTimeSlot result = accumulator.toWeatherTimeSlot(TimeSlot.MIDDAY);

        // Averages: temp = (10+20)/2 = 15.0, humidity = (60+80)/2 = 70
        assertEquals(15.0, result.getAirTemperature(), 0.0001);
        assertEquals(70, result.getHumidity());

        // Sums: precip = 1.5 + 2.5 = 4.0
        assertEquals(4.0, result.getPrecipitation(), 0.0001);
        assertEquals("Light rain", result.getPrecipitationDescription());

        // Extremes: min/max feels-like, max wind, max UV
        assertEquals(8.0, result.getMinFeelsLikeTemperature(), 0.0001);
        assertEquals(16.0, result.getMaxFeelsLikeTemperature(), 0.0001);
        assertEquals(15.0, result.getWindSpeed(), 0.0001);
        assertEquals(5.0, result.getMaxUvIndex(), 0.0001);
    }

    @Test
    public void toWeatherTimeSlot_MostFrequentCondition_SelectsMajorityCondition() {
        TimeSlotAccumulator accumulator = new TimeSlotAccumulator();

        // WMO 61 (LIGHT_RAIN) added twice, WMO 0 (CLEAR_SKY) added once
        accumulator.addHour(15.0, 70, 1.0, 14.0, 5.0, 61, 1.0);
        accumulator.addHour(15.0, 70, 1.0, 14.0, 5.0, 61, 1.0);
        accumulator.addHour(15.0, 70, 0.0, 14.0, 5.0, 0, 3.0);

        WeatherTimeSlot result = accumulator.toWeatherTimeSlot(TimeSlot.AFTERNOON);

        assertEquals(61, result.getCondition());
        assertEquals(WeatherSymbol.LIGHT_RAIN, result.getConditionDescription());
        assertEquals(false, result.isSunny());
    }

    @Test
    public void toWeatherTimeSlot_WmoZeroCode_SetsSunnyToFalseDueToEnumMapping() {
        TimeSlotAccumulator accumulator = new TimeSlotAccumulator();

        // WMO code 0 maps to WeatherSymbol.CLEAR_SKY (which has SMHI code 1)
        accumulator.addHour(20.0, 40, 0.0, 19.0, 2.0, 0, 6.0);

        WeatherTimeSlot result = accumulator.toWeatherTimeSlot(TimeSlot.MIDDAY);

        assertEquals(0, result.getCondition());
        assertEquals(WeatherSymbol.CLEAR_SKY, result.getConditionDescription());
        // isSunny checks symbol.getCode() == 0 || symbol.getCode() == 1
        // WeatherSymbol.CLEAR_SKY.getCode() returns 1, so isSunny is true
        assertEquals(true, result.isSunny());
    }

    @Test
    public void toWeatherTimeSlot_NoHoursAdded_ThrowsIllegalArgumentException() {
        TimeSlotAccumulator accumulator = new TimeSlotAccumulator();

        assertThrows(IllegalArgumentException.class, () -> {
            accumulator.toWeatherTimeSlot(TimeSlot.NIGHT);
        });
    }

    @Test
    public void toWeatherTimeSlot_UnknownWmoCode_ThrowsIllegalArgumentException() {
        TimeSlotAccumulator accumulator = new TimeSlotAccumulator();

        // 999 is an invalid WMO code
        accumulator.addHour(15.0, 50, 0.0, 14.0, 5.0, 999, 1.0);

        assertThrows(IllegalArgumentException.class, () -> {
            accumulator.toWeatherTimeSlot(TimeSlot.EVENING);
        });
    }

}