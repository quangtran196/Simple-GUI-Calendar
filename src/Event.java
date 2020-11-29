import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents an event
 * Holds event's information
 */
public class Event implements Comparable<Event> {
    private String name;
    private TimeInterval time;


    /**
     * Constructs event
     * @param name event's title
     * @param date event's date
     * @param startTime event's start time
     * @param endTime event's end time
     */
    public Event(String name, LocalDate date, LocalTime startTime, LocalTime endTime) {
        this.name = name;
        this.time = new TimeInterval(date, startTime, endTime);
    }

    /**
     * To retrieve event's title
     * @return name of Event
     */
    public String getName() {
        return name;
    }

    /**
     * To retrieve event's time interval
     *
     * @return event's time interval
     */
    public TimeInterval getTime() {
        return time;
    }


    /**
     * Compares two events by their time interval
     * @param other event need to compare
     * @return int defining if Event before or after this event
     */
    @Override
    public int compareTo(Event other) {
        return this.getTime().compareTo(other.getTime());
    }

}