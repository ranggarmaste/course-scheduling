/**
 * Domain of a CSP variable.
 * Author: Garmastewira
 * Date: 20 September 2016
 */
public class Domain {
    private int day;
    private int startTime;
    //Kenapa dura
    private int duration;
    private String room;

    public Domain(int day, int startTime, int duration, String room) {
        this.day = day;
        this.startTime = startTime;
        this.duration = duration;
        this.room = room;
    }

    public Domain(Domain d) {
        System.out.println("sampe sini");
        System.out.println(d);
        System.out.println("ngk sampe sini");
        day = d.day;
        startTime = d.startTime;
        duration = d.duration;
        room = d.room;
    }

    public static boolean isSame(Domain d1, Domain d2) {
        return (d1.day==d2.day && d1.startTime==d2.startTime && d1.duration==d2.duration && d1.room==d2.room);
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
