package c246.groupapp;

import java.util.Calendar;

public class Birthday {
    private int day;
    private int month;
    private int year;
    private Calendar birthday;

    public Birthday(int day, int month, int year) {
        this.day = day;
        this.month = month;
        this.year = year;
        birthday.set(year, month, day);
    }

    public void getAge(){
        Calendar currentTime = Calendar.getInstance();
        int age = currentTime.get(Calendar.YEAR) - birthday.get(Calendar.YEAR);
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public Calendar getBirthday() {
        return birthday;
    }

    public void setBirthday(Calendar birthday) {
        this.birthday = birthday;
    }
}
