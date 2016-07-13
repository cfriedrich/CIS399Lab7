package edu.uoregon.cnf.tidetracker;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DataItem {

    private SimpleDateFormat dateInFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss aa");
    private SimpleDateFormat dateOutFormat = new SimpleDateFormat("yyyy/MM/dd EEEE");
    private SimpleDateFormat shortDateOutFormat = new SimpleDateFormat("yyyy/MM/dd");
    private String location = null;
    private String dateString = null;
    private String day = null;
    private String highlow = null;
    private String ft = null;
    private String cm = null;
    private String timeString = null;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDateString() {
        return dateString;
    }

    public void setDateString(String date) {
        this.dateString = date;
    }

    public String getDay() {
        return this.day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getHighlow() {
        return this.highlow;
    }

    public void setHighlow(String highlow) {
        this.highlow = highlow;
    }

    public String getFeet() {
        return ft;
    }

    public void setFeet(String feet) {
        this.ft = feet;
    }

    public String getCentimeters() {
        return cm;
    }

    public void setCentimeters(String centimeters) {
        this.cm = centimeters;
    }

    public String getTimeString() {
        return timeString;
    }

    public void setTime(String time) {
        this.timeString = time;
    }

    public String getFormattedDate() {
        try {
            Date date = dateInFormat.parse(this.dateString.trim());
            String formattedDate = dateOutFormat.format(date);
            return formattedDate;
        }
        catch (Exception e) {
        }
        return this.dateString + " " + this.day;
    }

    public String getShortDate() {
        try {
            Date date = dateInFormat.parse(this.dateString.trim());
            String formattedDate = shortDateOutFormat.format(date);
            return formattedDate;
        }
        catch (Exception e) {
            return this.dateString;
        }
    }
}
