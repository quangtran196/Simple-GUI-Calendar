import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.Map.*;

/**
 * Controller of the program
 */
public class CalendarControl {
    private TreeMap<LocalDate, ArrayList<Event>> events = new TreeMap<LocalDate, ArrayList<Event>>();
    private LocalDate todayDate = LocalDate.now();

    /**
     * construct controller
     * read events from given file
     */
    public CalendarControl(){
            readEvents();

    }

    /**
     * give access data structure of list of event
     * @return events' list
     */
    public TreeMap<LocalDate, ArrayList<Event>> getEvents(){
        return events;
    }

    /**
     * set date for today
     * @param date user's choosen data
     */
    public void setTodayDate(LocalDate date){
        this.todayDate = date;
    }
    /**
     * To retrieve current date
     *
     * @return current date
     */
    public LocalDate getTodayDate() {
        return todayDate;
    }

    /**
     * get current day
     * @return current day
     */
    public int getCurDay(){
        return (todayDate.getDayOfMonth());
    }

    /**
     * change as user move or choose other data from calendar view
     * @param num distance from current date to user chosen date (by days)
     * @return user's chosen date
     */
    public LocalDate changeDate(int num){
        setTodayDate(todayDate.plusDays(num));
        return todayDate;
    }

    /**
     * move one day forward
     * @return the day after current day
     */
    public LocalDate nextDate(){
         setTodayDate(todayDate.plusDays(1));
         return todayDate;
    }

    /**
     * move one day backward
     * @return the day before current day
     */
    public LocalDate prevDate(){
        setTodayDate(todayDate.minusDays(1));
        return todayDate;
    }

    /**
     * number of days in month
     *
     * @return number of days
     */
    public int getDaysInMonth() {
        return todayDate.lengthOfMonth();
    }

    /**
     * To add event to calendar
     *
     * @param event event's information
     */
    public boolean addEvents(Event event) {
        //find the date
        LocalDate start = event.getTime().getStartDate();
        // Check if the new event is overlapped with any other events in the date
        if (isOverLapping(start, event) == false) {
            // If there are no events in that date, add a new ArrayList of
            // Events
            events.putIfAbsent(start, new ArrayList<Event>());
            events.get(start).add(event);
            for (Entry<LocalDate, ArrayList<Event>> entry : events.entrySet()) {
                Collections.sort(entry.getValue());
            }

            return true;
        } else {
            return false;
        }
    }

    /**
     * check time conflict
     * @param date date of the event
     * @param newEvent events information
     * @return true/false
     */
    public boolean isOverLapping(LocalDate date, Event newEvent) {
        for (Entry<LocalDate, ArrayList<Event>> entry : events.entrySet()) {
            for (Event e : entry.getValue()) {
                if (e.getTime().getStartDate().equals(date) && e.getTime().overLap(newEvent.getTime())) {
                    return true;
                }
            }
        }
        return false;
    }


    /**
     * To read and load events from an input to calendar
     */
    public void readEvents() {
        String fileName = "events.txt";

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            // Read file line by line
            while ((line = br.readLine()) != null) {
                // First line contains name
                String name = line;
                line = br.readLine();
                String[] token = line.split(" ");
                    // One Time event
                    DateTimeFormatter formatterDate = DateTimeFormatter.ofPattern("M/d/yy");
                    LocalDate startDate = LocalDate.parse(token[0], formatterDate);

                    DateTimeFormatter formatterHour = DateTimeFormatter.ofPattern("H:mm");
                    LocalTime startTime = LocalTime.parse(token[1], formatterHour);
                    LocalTime endTime = LocalTime.parse(token[2], formatterHour);

                    addEvents(new Event(name, startDate, startTime, endTime));
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Error " + e.getMessage());
        }
    }

    /**
     * save events to output file
     *
     * @throws FileNotFoundException
     */
    public void saveToFile() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("events.txt"));

            for (Entry<LocalDate, ArrayList<Event>> entry : events.entrySet()) {
                for (Event e : entry.getValue()) {
                    writer.write(e.getName());
                    writer.newLine();
                    DateTimeFormatter formatterDate = DateTimeFormatter.ofPattern("M/d/yy");

                    writer.write(formatterDate.format(e.getTime().getStartDate()).toString());
                    writer.write(" ");
                    writer.write(e.getTime().getStartTime().toString());
                    writer.write(" ");
                    writer.write(e.getTime().getEndTime().toString());
                    writer.newLine();
                }
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
