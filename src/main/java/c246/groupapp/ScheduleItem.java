package c246.groupapp;

import java.util.ArrayList;

/**
 * Stores the basic info fo an individual schedule item that every item has.
 */
public class ScheduleItem {

    private Employee e;
    private String date;
    private String startTime;
    private String endTime;

    public ScheduleItem(Employee e, String date, String startTime, String endTime) {
        this.e = e;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getDate() {
        return date;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public Employee getE() {
        return e;
    }


}
