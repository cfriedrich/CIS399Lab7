package edu.uoregon.cnf.tidetracker;


// The Prediction object
public class Prediction {

    private int predictionID;
    private int locationID;
    private String date;
    private String time;
    private String highlow;
    private String feet;
    private String centimeters;

    public Prediction() {
    }

    public Prediction(int locationID) {
        this.locationID = locationID;
    }

    public Prediction(int locationID, String date, String time, String highlow, String feet, String inches) {
        this.locationID = locationID;
        this.date = date;
        this.time = time;
        this.highlow = highlow;
        this.feet = feet;
        this.centimeters = centimeters;
    }

    public int getPredictionID() {
        return predictionID;
    }

    public void setPredictionID(int predictionID) {
        this.predictionID = predictionID;
    }

    public int getLocationID() {
        return locationID;
    }

    public void setLocationID(int locationID) {
        this.locationID = locationID;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getHighlow() {
        return highlow;
    }

    public void setHighlow(String highlow) {
        this.highlow = highlow;
    }

    public String getFeet() {
        return feet;
    }

    public void setFeet(String feet) {
        this.feet = feet;
    }

    public String getCentimeters() {
        return centimeters;
    }

    public void setCentimeters(String centimeters) {
        this.centimeters = centimeters;
    }



}
