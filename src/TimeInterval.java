import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Represents time interval for events
 */
public class TimeInterval implements Comparable<TimeInterval> {
    private LocalDate startDate;
    private LocalTime startTime;
    private LocalTime endTime;

    /**
     * Constructs a time interval for an event
     * @param date event's date
     * @param startTime event's start time
     * @param endTime event's end time
     */
    public TimeInterval(LocalDate date, LocalTime startTime, LocalTime endTime) {
        this.startDate = date;
        this.startTime = startTime;
        this.endTime = endTime;
    }



    /**
     * to retrieve event's start date
     * @return event's start date
     */
    public LocalDate getStartDate() {
        return this.startDate;
    }

    /**
     * to retrieve event's start time
     * @return event's start time
     */
    public LocalTime getStartTime() {
        return this.startTime;
    }

    /**
     * To retrieve event's end time
     * @return event's end time
     */
    public LocalTime getEndTime() {
        return this.endTime;
    }

    /**
     * To check whether two events are overlapped or not
     * @param timeInterval get time interval of one event
     * @return true if overlapped, false if not
     */
    public boolean overLap(TimeInterval timeInterval) {
        return this.getStartTime().isBefore(timeInterval.getEndTime())
                && this.getEndTime().isAfter(timeInterval.getStartTime());
    }

    /**
     * To compare two time interval
     * @param other
     * @return Negative is this object's start time is before other's start
     * time, positive if after other's, equal if same
     */
    @Override
    public int compareTo(TimeInterval other) {
        return this.getStartTime().compareTo(other.getStartTime());
    }
}