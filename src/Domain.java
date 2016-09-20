/**
 * Domain of a CSP variable.
 * Author: Garmastewira
 * Date: 20 September 2016
 */
public class Domain {
    private int day;
    private int startTime;
    private int duration;
    private String room;

    public Domain(int day, int startTime, int duration, String room) {
        this.day = day;
        this.startTime = startTime;
        this.duration = duration;
        this.room = room;
    }

    public Domain(Domain d) {
        day = d.day;
        startTime = d.startTime;
        duration = d.duration;
        room = d.room;
    }

    public int getDay() {
        return day;
    }

    public int getStartTime() {
        return startTime;
    }

    public int getDuration() {
        return duration;
    }

    public int getFinishTime() {
        return startTime + duration;
    }

    public String getRoom() {
        return room;
    }

    public void print() {
        System.out.println(day + ", " + startTime + ", " + duration + ", " + room);
    }
}
