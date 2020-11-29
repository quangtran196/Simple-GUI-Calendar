import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Create View for Calendar
 */
public class CalendarView {
    private final String[] DAYS = {"S", "M", "T", "W", "T", "F", "S"};

    private CalendarControl control;
    private JFrame frame;
    private JPanel monthViewPanel,eventViewPanel,midMonthView;
    private JTextArea eventText;
    private JLabel monthYear, dateOnEventView;
    private JButton[] buttonList = new JButton[32];

    /**
     * Construct The View class
     * @param control  data controller
     */
    public CalendarView(CalendarControl control) {
        this.control = control;


        //create frame
        frame = new JFrame("My Simple Calendar");
        frame.setSize(550, 270);
        //frame.setResizable(false);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //create month view panel
        //contain month's information
        monthViewPanel = new JPanel();
        monthViewPanel.setLayout(new BorderLayout());

        //top part contains month/year, < > buttons
        JPanel topMonthView = new JPanel();
        //create label to contain month/year
        monthYear = new JLabel();
        DateTimeFormatter formater = DateTimeFormatter.ofPattern("MMMM yyyy");
        String curDate = formater.format(control.getTodayDate());
        monthYear.setText(curDate);
        //create < > btn
        JButton nextBtn = new JButton(">");
        nextBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eventText.selectAll();
                eventText.replaceSelection("");
                redrawCalendar(control.nextDate());
                DateTimeFormatter formater = DateTimeFormatter.ofPattern("dd");
                showDateOnEventView(Integer.parseInt(formater.format(control.getTodayDate())));
            }
        });
        JButton prevBtn = new JButton("<");
        prevBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eventText.selectAll();
                eventText.replaceSelection("");
                redrawCalendar(control.prevDate());
                DateTimeFormatter formater = DateTimeFormatter.ofPattern("dd");
                showDateOnEventView(Integer.parseInt(formater.format(control.getTodayDate())));
            }
        });
        //add buttons to monthview
        topMonthView.setLayout(new FlowLayout());
        topMonthView.add(monthYear);
        topMonthView.add(prevBtn);
        topMonthView.add(nextBtn);


        //Create middle view of monthview which contains all days of months
        midMonthView = new JPanel();
        midMonthView.setLayout(new GridLayout(6, 7, 10 , 10));
        //add button to panel
        addBtnDates();




        //create bottom view of monthViewPanel
        JPanel botMonthView = new JPanel();
        botMonthView.setLayout(new FlowLayout());
        //create buttons
        //add button
        JButton create = new JButton("Create");
        create.setBackground(Color.red);
        create.setOpaque(true);
        create.setBorderPainted(false);
        create.setForeground(Color.white);
        create.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createEvent(control.getTodayDate());
            }
        });
        //quit button
        JButton quit = new JButton("Quit");
        quit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                control.saveToFile();
                String s = e.getActionCommand();
                if (s.equals("Quit")) {
                    JDialog d = new JDialog();
                    d.setTitle("Notification");
                    d.setBounds(200,200,250,70);
                    d.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                    JButton close = new JButton("Close");
                    JLabel noti = new JLabel("Events has been saved!");
                    d.add(noti);
                    d.add(close);
                    d.setLayout(new FlowLayout());
                    d.setVisible(true);
                    close.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            System.exit(0);
                        }
                    });
                }
            }
        });
        quit.setBackground(Color.red);
        quit.setOpaque(true);
        quit.setBorderPainted(false);
        quit.setForeground(Color.white);
        //add buttons to bottom panel
        botMonthView.add(create);
        botMonthView.add(quit);

        monthViewPanel.add(topMonthView, BorderLayout.NORTH);
        monthViewPanel.add(midMonthView, BorderLayout.CENTER);
        monthViewPanel.add(botMonthView, BorderLayout.SOUTH);
        //create event view and text area.
        eventViewPanel = new JPanel();
        eventText = new JTextArea(15, 20);
        eventText.setBorder(BorderFactory.createLineBorder(Color.gray));
        //create label to display the date
        dateOnEventView = new JLabel();
        displayEvent();
        DateTimeFormatter formater1 = DateTimeFormatter.ofPattern("MM/dd");
        dateOnEventView.setText(control.getTodayDate().getDayOfWeek() +  "  "
                + formater1.format(control.getTodayDate()));
        eventViewPanel.setLayout(new BorderLayout());
        eventViewPanel.add(dateOnEventView, BorderLayout.NORTH);
        eventViewPanel.add(eventText, BorderLayout.CENTER);



        //Add components to frame
        frame.add(monthViewPanel);
        frame.add(eventViewPanel);
        BoxLayout boxLayout = new BoxLayout(frame.getContentPane(), BoxLayout.X_AXIS);
        frame.setLayout(boxLayout);
        frame.pack();
        frame.setVisible(true);
    }

    /**
     * Create a new window for creating new events
     * @param date get the date
     */
    public void createEvent(LocalDate date){
        JFrame frame = new JFrame();
        //frame.setSize(300,150);
        frame.setBounds(100, 100, 300, 130);
        frame.setTitle("Create Event");
        frame.setResizable(false);


        JLabel label = new JLabel("Event Title");
        JTextField eventTitle = new JTextField( 15);

        JTextField dateTime = new JTextField(date.toString());
        dateTime.setEditable(false);
        JTextField startTime = new JTextField("00:00", 5);
        startTime.setForeground(Color.gray);
        startTime.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e ){
                startTime.setText("");
            }
        });
        JTextField endTime = new JTextField("00:00", 5);
        endTime.setForeground(Color.gray);
        endTime.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e ){
                endTime.setText("");
            }
        });
        JLabel to = new JLabel("to");
        JButton saveBtn = new JButton("Save");
        saveBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String eventName = eventTitle.getText();
                LocalTime start = LocalTime.parse(startTime.getText());
                LocalTime end = LocalTime.parse(endTime.getText());
                boolean checkAdd = control.addEvents(new Event(eventName,control.getTodayDate(), start, end));
                String s = e.getActionCommand();
                if (s.equals("Save")) {
                    JDialog d = new JDialog();
                    d.setTitle("Notification");
                    d.setBounds(200,200,250,70);
                    d.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                    JLabel noti;
                    JButton close = new JButton("Close");
                    if(checkAdd){
                        eventText.selectAll();
                        eventText.replaceSelection("");
                        displayEvent();
                        noti = new JLabel("Event " + eventName + " has been added!");
                    }else{
                        noti = new JLabel("Time conflict! Could not add " + eventName);
                    }

                    d.add(noti);
                    d.add(close);
                    d.setLayout(new FlowLayout());
                    d.setVisible(true);
                    close.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            d.dispose();
                        }
                    });

                }
            }
        });

        JButton closeBtn = new JButton("Close");
        frame.add(label);
        frame.add(eventTitle);
        frame.add(dateTime);
        frame.add(startTime);
        frame.add(to);
        frame.add(endTime);
        frame.add(saveBtn);
        frame.add(closeBtn);
        frame.setLayout(new FlowLayout());
        frame.setVisible(true);
        closeBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });

    }


    /**
     * Add buttons to represent days in month
     */
    public void addBtnDates(){

        for (int i = 0; i < DAYS.length; i++ ) {
            midMonthView.add(new JLabel("        " + DAYS[i]));
        }
        //get the start day of a month
        LocalDate todayDate = control.getTodayDate().withDayOfMonth(1);
        java.util.Date date = java.sql.Date.valueOf(todayDate);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int startDay = cal.get(Calendar.DAY_OF_WEEK);
        for (int i = 1; i < startDay; i++){
            JButton btn = new JButton();
            btn.setEnabled(false);
            btn.setBorderPainted(false);
            midMonthView.add(btn);

        }

        //add btn to month view
        int daysOfWeek = control.getDaysInMonth();
        for (int i = 1; i <= daysOfWeek; i++){
            final int dateOnBtn = i;
            JButton btn = new JButton(Integer.toString(i));
            btn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    DateTimeFormatter formater = DateTimeFormatter.ofPattern("dd");
                    int changeDate = dateOnBtn;
                    int curDate = Integer.parseInt(formater.format(control.getTodayDate()));
                    control.changeDate(changeDate - curDate);
                    showDateOnEventView(dateOnBtn);

                }
            });
            btn.setOpaque(true);
            btn.setBorderPainted(false);
            buttonList[i] = btn;
            midMonthView.add(buttonList[i]);
        }
        highlightDate(control.getCurDay());
    }

    /**
     * highlight the current date
     * @param date
     */
    private void highlightDate (int date){
        buttonList[date].setBackground(Color.white);
        buttonList[date].setOpaque(true);
        buttonList[date].setForeground(Color.red);
    }

    /**
     * redraw the month view to highlight the date
     * as user move or choose a date from calendar
     * @param date get date
     */
    public void redrawCalendar(LocalDate date){
        DateTimeFormatter formater = DateTimeFormatter.ofPattern("MMMM yyyy");
        String curDate = formater.format(date);
        monthYear.setText(curDate);
        midMonthView.removeAll();
        addBtnDates();
        midMonthView.validate();
        midMonthView.repaint();


    }

    /**
     * Show current data time
     * @param date
     */
    public void showDateOnEventView(int date){
        DateTimeFormatter formater = DateTimeFormatter.ofPattern("MM/dd");
        highlightDate(date);
        dateOnEventView.setText(control.getTodayDate().getDayOfWeek() +  "  "
                + formater.format(control.getTodayDate()));
        redrawCalendar(control.getTodayDate());
        displayEvent();
    }

    /**
     * display existing events to event view
     */
    public void displayEvent(){
        eventText.selectAll();
        eventText.replaceSelection("");
        // print today date
        for (Map.Entry<LocalDate, ArrayList<Event>> entry : control.getEvents().entrySet()) {
            if (entry.getKey().isEqual(control.getTodayDate())) {
                for (Event event : entry.getValue()) {
                    DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("H:mm");
                    eventText.append("\n" + formatter1.format(event.getTime().getStartTime())
                            + " " + formatter1.format(event.getTime().getEndTime()) + " " + event.getName());
                }
            }
        }

    }

}
