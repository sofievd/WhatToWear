package se.sofievd.backend.weather.service;

import org.junit.jupiter.api.Test;
import se.sofievd.backend.weather.model.WeatherTimeSlot;
import se.sofievd.backend.weather.model.enums.TimeSlot;

import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WeatherUtilsTest {

    @Test
    public void calculateAveragePrecipitation_StandardValues_ReturnsAverage() {
        double result = WeatherUtils.calculateAveragePrecipitation(10.0, 2.0);
        assertEquals(6.0, result, 0.0001);
    }

    @Test
    public void calculateAveragePrecipitation_SameMaxAndMin_ReturnsSameValue() {
        double result = WeatherUtils.calculateAveragePrecipitation(5.0, 5.0);
        assertEquals(5.0, result, 0.0001);
    }

    @Test
    public void calculateAveragePrecipitation_ZeroValues_ReturnsZero() {
        double result = WeatherUtils.calculateAveragePrecipitation(0.0, 0.0);
        assertEquals(0.0, result, 0.0001);
    }

    @Test
    public void calculateAveragePrecipitation_DecimalValues_ReturnsCorrectAverage() {
        double result = WeatherUtils.calculateAveragePrecipitation(2.5, 1.1);
        assertEquals(1.8, result, 0.0001);
    }

    @Test
    public void calculateAveragePrecipitation_SwappedInputs_ReturnsSameAverage() {
        // If min is larger than max passed by mistake
        double result = WeatherUtils.calculateAveragePrecipitation(2.0, 10.0);
        assertEquals(6.0, result, 0.0001);
    }

    @Test
    public void calculateFeelLikeTemperature_StandardInputs_ReturnsCorrectValue() {
        // Temp: 20°C, Humidity: 50%, Wind: 5 m/s
        // pressure = 0.5 * 6.105 * exp((17.27 * 20) / 257.7) = 3.0525 * exp(1.3403) = 11.661
        // expected = 20 + (0.33 * 11.661) - (0.7 * 5) - 4.0 = 20 + 3.848 - 3.5 - 4.0 = 16.348
        double result = WeatherUtils.calculateFeelLikeTemperature(20.0, 50, 5.0);
        assertEquals(16.348, result, 0.001);
    }

    @Test
    public void calculateFeelLikeTemperature_ZeroHumidityAndWind_CalculatesBaseOffset() {
        // 20°C, 0% humidity (pressure = 0), 0 wind -> 20 + 0 - 0 - 4.0 = 16.0
        double result = WeatherUtils.calculateFeelLikeTemperature(20.0, 0, 0.0);
        assertEquals(16.0, result, 0.0001);
    }

    @Test
    public void calculateFeelLikeTemperature_HighHumidity_IncreasesFeelsLikeTemp() {
        // High humidity (90%) increases vapor pressure, raising feels-like temp
        double lowHumidityResult = WeatherUtils.calculateFeelLikeTemperature(25.0, 20, 2.0);
        double highHumidityResult = WeatherUtils.calculateFeelLikeTemperature(25.0, 90, 2.0);

        assertTrue(highHumidityResult > lowHumidityResult);
    }

    @Test
    public void calculateFeelLikeTemperature_HighWind_DecreasesFeelsLikeTemp() {
        // Higher wind speed increases cooling effect, dropping feels-like temp
        double lowWindResult = WeatherUtils.calculateFeelLikeTemperature(15.0, 50, 2.0);
        double highWindResult = WeatherUtils.calculateFeelLikeTemperature(15.0, 50, 15.0);

        assertTrue(highWindResult < lowWindResult);
    }

    @Test
    public void calculateFeelLikeTemperature_SubZeroTemperature_CalculatesNegativeValues() {
        // -10°C, 80% humidity, 10 m/s wind
        double result = WeatherUtils.calculateFeelLikeTemperature(-10.0, 80, 10.0);
        assertEquals(-20.245, result, 0.001);
    }

    @Test
    public void calculateFeelLikeTemperature_ZeroDegrees_CalculatesCorrectly() {
        // 0°C, 50% humidity, 0 wind
        // pressure = 0.5 * 6.105 * exp(0) = 3.0525
        // expected = 0 + (0.33 * 3.0525) - 0 - 4.0 = -2.9926
        double result = WeatherUtils.calculateFeelLikeTemperature(0.0, 50, 0.0);
        assertEquals(-2.9926, result, 0.001);
    }

    @Test
    public void calculateFeelLikeTemperature_100PercentHumidity_HandlesMaxHumidity() {
        // Verifies integer division does not truncate ((double) humidity / 100)
        double result = WeatherUtils.calculateFeelLikeTemperature(20.0, 100, 5.0);
        assertEquals(20.196, result, 0.001);
    }

    @Test
    public void calculateWindSpeed_StandardValue_ConvertsToKmPerHour() {
        // 10 m/s * 3.6 = 36.0 km/h
        double result = WeatherUtils.calculateWindSpeed(10.0);
        assertEquals(36.0, result, 0.0001);
    }

    @Test
    public void calculateWindSpeed_Zero_ReturnsZero() {
        double result = WeatherUtils.calculateWindSpeed(0.0);
        assertEquals(0.0, result, 0.0001);
    }

    @Test
    public void calculateWindSpeed_DecimalValue_CalculatesCorrectly() {
        // 2.5 m/s * 3.6 = 9.0 km/h
        double result = WeatherUtils.calculateWindSpeed(2.5);
        assertEquals(9.0, result, 0.0001);
    }

    @Test
    public void calculateWindSpeed_NegativeValue_CalculatesCorrectly() {
        // Handling negative inputs if passed by mistake (-5.0 * 3.6 = -18.0)
        double result = WeatherUtils.calculateWindSpeed(-5.0);
        assertEquals(-18.0, result, 0.0001);
    }

    @Test
    public void getPrecipitationDescription_Zero_ReturnsNoRain() {
        String result = WeatherUtils.getPrecipitationDescription(0.0);
        assertEquals("No rain", result);
    }

    @Test
    public void getPrecipitationDescription_UnderOne_ReturnsVeryLightRain() {
        String result = WeatherUtils.getPrecipitationDescription(0.5);
        assertEquals("Very light rain", result);
    }

    @Test
    public void getPrecipitationDescription_BetweenOneAndFive_ReturnsLightRain() {
        String result = WeatherUtils.getPrecipitationDescription(3.0);
        assertEquals("Light rain", result);
    }

    @Test
    public void getPrecipitationDescription_BetweenFiveAndTen_ReturnsModerateRain() {
        String result = WeatherUtils.getPrecipitationDescription(7.5);
        assertEquals("Moderate rain", result);
    }

    @Test
    public void getPrecipitationDescription_BetweenTenAndTwenty_ReturnsHeavyRain() {
        String result = WeatherUtils.getPrecipitationDescription(15.0);
        assertEquals("Heavy rain", result);
    }

    @Test
    public void getPrecipitationDescription_TwentyOrAbove_ReturnsVeryHeavyRain() {
        String result = WeatherUtils.getPrecipitationDescription(25.0);
        assertEquals("Very heavy rain", result);
    }
    @Test
    public void getPrecipitationDescription_ExactOneBoundary_ReturnsLightRain() {
        // 1.0 is not < 1, so it moves to "Light rain"
        String result = WeatherUtils.getPrecipitationDescription(1.0);
        assertEquals("Light rain", result);
    }

    @Test
    public void getPrecipitationDescription_ExactFiveBoundary_ReturnsModerateRain() {
        // 5.0 is not < 5, so it moves to "Moderate rain"
        String result = WeatherUtils.getPrecipitationDescription(5.0);
        assertEquals("Moderate rain", result);
    }

    @Test
    public void getPrecipitationDescription_ExactTenBoundary_ReturnsHeavyRain() {
        // 10.0 is not < 10, so it moves to "Heavy rain"
        String result = WeatherUtils.getPrecipitationDescription(10.0);
        assertEquals("Heavy rain", result);
    }

    @Test
    public void getPrecipitationDescription_ExactTwentyBoundary_ReturnsVeryHeavyRain() {
        // 20.0 is not < 20, so it moves to "Very heavy rain"
        String result = WeatherUtils.getPrecipitationDescription(20.0);
        assertEquals("Very heavy rain", result);
    }

    @Test
    public void getPrecipitationDescription_NegativePrecipitation_ReturnsVeryLightRain() {
        // Negative values satisfy (< 1) and return "Very light rain"
        String result = WeatherUtils.getPrecipitationDescription(-2.0);
        assertEquals("Very light rain", result);
    }

    @Test
    public void findCurrentTimeSlot_TimeMatchesSlot_ReturnsMatchingSlot() {
        WeatherTimeSlot morningSlot = createTimeSlot(TimeSlot.MORNING);
        WeatherTimeSlot middaySlot = createTimeSlot(TimeSlot.MIDDAY);
        List<WeatherTimeSlot> slots = List.of(morningSlot, middaySlot);

        // 07:30 falls in MORNING (06:00 - 08:59)
        WeatherTimeSlot result = WeatherUtils.findCurrentTimeSlot(slots, LocalTime.of(7, 30));
        assertEquals(morningSlot, result);
    }

    @Test
    public void findCurrentTimeSlot_TimeMatchesMultipleSlots_ReturnsFirstMatch() {
        WeatherTimeSlot slot1 = createTimeSlot(TimeSlot.AFTERNOON);
        WeatherTimeSlot slot2 = createTimeSlot(TimeSlot.AFTERNOON);
        List<WeatherTimeSlot> slots = List.of(slot1, slot2);

        // 14:00 matches both AFTERNOON slots, returns the first one in the list
        WeatherTimeSlot result = WeatherUtils.findCurrentTimeSlot(slots, LocalTime.of(14, 0));
        assertEquals(slot1, result);
    }


    @Test
    public void findCurrentTimeSlot_TimeNotFound_ReturnsNull() {
        WeatherTimeSlot nightSlot = createTimeSlot(TimeSlot.NIGHT);
        List<WeatherTimeSlot> slots = List.of(nightSlot);

        // 14:00 (AFTERNOON) is not in the list containing only NIGHT
        WeatherTimeSlot result = WeatherUtils.findCurrentTimeSlot(slots, LocalTime.of(14, 0));
        assertNull(result);
    }

    @Test
    public void findCurrentTimeSlot_EmptyList_ReturnsNull() {
        WeatherTimeSlot result = WeatherUtils.findCurrentTimeSlot(List.of(), LocalTime.of(10, 0));
        assertNull(result);
    }

    @Test
    public void findCurrentTimeSlot_NullSlotsList_ThrowsNullPointerException() {
        assertThrows(NullPointerException.class, () -> {
            WeatherUtils.findCurrentTimeSlot(null, LocalTime.of(10, 0));
        });
    }

    @Test
    public void findCurrentTimeSlot_NullTime_ThrowsNullPointerException() {
        WeatherTimeSlot slot = createTimeSlot(TimeSlot.MORNING);
        assertThrows(NullPointerException.class, () -> {
            WeatherUtils.findCurrentTimeSlot(List.of(slot), null);
        });
    }

    @Test
    public void filterActiveTimeSlots_TimeBeforeEnd_KeepsSlot() {
        WeatherTimeSlot morningSlot = createTimeSlot(TimeSlot.MORNING); // End time: 08:59
        List<WeatherTimeSlot> slots = List.of(morningSlot);

        // Current time 07:00 is before 08:59 -> slot remains active
        List<WeatherTimeSlot> result = WeatherUtils.filterActiveTimeSlots(slots, LocalTime.of(7, 0));

        assertEquals(1, result.size());
        assertEquals(morningSlot, result.get(0));
    }

    @Test
    public void filterActiveTimeSlots_TimeAfterEnd_FiltersOutSlot() {
        WeatherTimeSlot morningSlot = createTimeSlot(TimeSlot.MORNING); // End time: 08:59
        List<WeatherTimeSlot> slots = List.of(morningSlot);

        // Current time 10:00 is after 08:59 -> slot filtered out
        List<WeatherTimeSlot> result = WeatherUtils.filterActiveTimeSlots(slots, LocalTime.of(10, 0));

        assertTrue(result.isEmpty());
    }

    @Test
    public void filterActiveTimeSlots_MultipleSlots_FiltersOnlyPastSlots() {
        WeatherTimeSlot morning = createTimeSlot(TimeSlot.MORNING);     // End: 08:59
        WeatherTimeSlot midday = createTimeSlot(TimeSlot.MIDDAY);       // End: 12:59
        WeatherTimeSlot afternoon = createTimeSlot(TimeSlot.AFTERNOON); // End: 16:59
        List<WeatherTimeSlot> slots = List.of(morning, midday, afternoon);

        // Current time 10:00 -> MORNING is filtered out, MIDDAY and AFTERNOON remain
        List<WeatherTimeSlot> result = WeatherUtils.filterActiveTimeSlots(slots, LocalTime.of(10, 0));

        assertEquals(2, result.size());
        assertTrue(result.contains(midday));
        assertTrue(result.contains(afternoon));
    }

    @Test
    public void filterActiveTimeSlots_ExactEndTime_FiltersOutSlot() {
        WeatherTimeSlot morningSlot = createTimeSlot(TimeSlot.MORNING); // End time: 09:00
        List<WeatherTimeSlot> slots = List.of(morningSlot);

        // 08:59.isAfter(08:59) evaluates to false -> exact end time filters out slot
        List<WeatherTimeSlot> result = WeatherUtils.filterActiveTimeSlots(slots, LocalTime.of(9, 0));

        assertTrue(result.isEmpty());
    }

    @Test
    public void filterActiveTimeSlots_OneMinuteBeforeEnd_KeepsSlot() {
        WeatherTimeSlot morningSlot = createTimeSlot(TimeSlot.MORNING); // End time: 09:00
        List<WeatherTimeSlot> slots = List.of(morningSlot);

        // 08:58 is before 08:59 -> keeps slot
        List<WeatherTimeSlot> result = WeatherUtils.filterActiveTimeSlots(slots, LocalTime.of(8, 59));

        assertEquals(1, result.size());
    }

    @Test
    public void filterActiveTimeSlots_EmptyList_ReturnsEmptyList() {
        List<WeatherTimeSlot> result = WeatherUtils.filterActiveTimeSlots(List.of(), LocalTime.of(10, 0));
        assertTrue(result.isEmpty());
    }

    @Test
    public void filterActiveTimeSlots_NullTimeSlots_ThrowsNullPointerException() {
        assertThrows(NullPointerException.class, () -> {
            WeatherUtils.filterActiveTimeSlots(null, LocalTime.of(10, 0));
        });
    }

    @Test
    public void filterActiveTimeSlots_NullTime_ThrowsNullPointerException() {
        WeatherTimeSlot slot = createTimeSlot(TimeSlot.MORNING);

        assertThrows(NullPointerException.class, () -> {
            WeatherUtils.filterActiveTimeSlots(List.of(slot), null);
        });
    }

    private WeatherTimeSlot createTimeSlot(TimeSlot timeSlot) {
        WeatherTimeSlot slot = new WeatherTimeSlot();
        slot.setTimeSlot(timeSlot);
        return slot;
    }
}