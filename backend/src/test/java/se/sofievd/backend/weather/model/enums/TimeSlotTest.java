package se.sofievd.backend.weather.model.enums;

import org.junit.jupiter.api.Test;

import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class TimeSlotTest {

    @Test
    public void timeSlot_ExactStartBoundary_ReturnsTrue() {
        // 06:00 is the exact start time of MORNING
        assertTrue(TimeSlot.MORNING.contains(LocalTime.of(6, 0)));
    }

    @Test
    public void timeSlot_ExactEndBoundary_ShouldBeInclusive() {
        // 05:59 is the end time of NIGHT
        // With your current code this will FAIL because isBefore(05:59) is false for 05:59
        assertTrue(TimeSlot.NIGHT.contains(LocalTime.of(5, 59)));
    }

    @Test
    public void timeSlotFrom_ValidTime_ReturnsCorrectEnum() {
        assertEquals(TimeSlot.MORNING, TimeSlot.from(LocalTime.of(7, 15)));
        assertEquals(TimeSlot.EVENING, TimeSlot.from(LocalTime.of(18, 30)));
    }

}