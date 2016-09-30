import java.util.ArrayList;


/**
 * Domain limit for each variable retrieved from testcase file. Used in Graph class to build variables.
 * Author: Garmastewira
 * Date: 20 September 2016
 */
public class DomainLimit {
    private String name;
    private String room;
    private int startTime;
    private int finishTime;
    private int duration;
    private ArrayList<Integer> days;

    public DomainLimit(String name, String room, String startTime, String finishTime, String duration, String[] days) {
        this.name = name;
        this.room = room;
        this.startTime = parseTime(startTime);
        this.finishTime = parseTime(finishTime);
        this.duration = Integer.parseInt(duration);
        this.days = new ArrayList<>();
        for (String day : days) {
            this.days.add(Integer.parseInt(day));
        }
    }

    public int getDuration() {
        return duration;
    }

    public int getStartTime() {
        return startTime;
    }

    public int getFinishTime() {
        return finishTime;
    }

    public ArrayList<Integer> getDays() {
        return days;
    }

    public String getName() {
        return name;
    }

    public String getRoom() {
        return room;
    }

    /**
     * Convert time formatted string to integer, e.g. "07.00" to 7
     * @param time Time string
     * @return Integer time
     */
    public static int parseTime(String time) {
        return Integer.parseInt(time.split("\\.")[0]);
    }
}
