package c246.groupapp;

import com.google.firebase.firestore.IgnoreExtraProperties;

/**
 * Schedule class stores the information that will be show in main
 */
@IgnoreExtraProperties
public class Schedule{

    public static final String FIELD_TIME = "time";
    public static final String FIELD_DATE = "date";

    private String day;
    private String time;
    private String date;
    private String photo;

    public Schedule() {}

    /**
     * Non-Default constructor for creating a schedule
     * @param day
     * @param time
     * @param date
     * @param photo
     */
    public Schedule(String day, String time, String date, String photo) {
        this.day = day;
        this.time = time;
        this.date = date;
        this.photo = photo;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
