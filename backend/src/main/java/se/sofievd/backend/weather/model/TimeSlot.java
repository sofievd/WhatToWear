package se.sofievd.backend.weather.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cglib.core.Local;

import java.sql.Time;
import java.time.LocalTime;

public enum TimeSlot {
    NIGHT("Night", LocalTime.of(0, 0), LocalTime.of(5, 59)),
    MORNING("Morning", LocalTime.of(6,0), LocalTime.of(8,59)),
    MIDDAY("Midday", LocalTime.of(9,0), LocalTime.of(12,59)),
    AFTERNOON("Afternoon", LocalTime.of(13,0), LocalTime.of(16,59)),
    EVENING("Evening", LocalTime.of(17,0), LocalTime.of(20,59)),
    LATE_NIGHT("Late Night", LocalTime.of(21,0),LocalTime.of(23,59));

    private static final Logger log = LoggerFactory.getLogger(TimeSlot.class);
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
        if (startTime.isBefore(endTime)) {
            return !time.isBefore(startTime) && time.isBefore(endTime);
        }
        return !time.isBefore(startTime) || time.isBefore(endTime);
    }

    public static TimeSlot from(LocalTime time){
        for(TimeSlot timeSlot: values()){
            //log.info("time: {}", time);
            if(timeSlot.contains(time)){
                return timeSlot;
            }
        }
        throw new IllegalArgumentException("No TimeSlot found for time: " + time);
    }
}
