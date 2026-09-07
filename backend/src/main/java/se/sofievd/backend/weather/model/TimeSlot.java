package se.sofievd.backend.weather.model;

import org.springframework.cglib.core.Local;

import java.time.LocalTime;

public enum TimeSlot {
    NIGHT("Night", LocalTime.of(0, 0), LocalTime.of(6, 0)),
    MORNING("Morning", LocalTime.of(6,0), LocalTime.of(9,0)),
    MIDDAY("Midday", LocalTime.of(9,0), LocalTime.of(13,0)),
    AFTERNOON("Afternoon", LocalTime.of(13,0), LocalTime.of(17,0)),
    EVENING("Evening", LocalTime.of(17,0), LocalTime.of(21,0)),
    LATE_NIGHT("Late Night", LocalTime.of(21,0),LocalTime.of(0,0));

    private final String displayName;
    private final LocalTime startTime;
    private final LocalTime endTime;

    TimeSlot(String displayName, LocalTime startTime, LocalTime endTime) {
        this.displayName = displayName;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getDisplayName() { return displayName; }
    public LocalTime getStartTime() { return startTime; }
    public LocalTime getEndTime() { return endTime; }

    public boolean contains(LocalTime time) {
        if (this == LATE_NIGHT) {
            return !time.isBefore(startTime) || time.equals(LocalTime.MIDNIGHT);
        }
        return !time.isBefore(startTime) && time.isBefore(endTime);
    }
}
