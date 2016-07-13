package edu.uoregon.cnf.tidetracker;

// The Location object which represents.. a location
public class Location {

    private int locationID;
    private String name;

    private String locationCode;
    private String latitude;
    private String longitude;
    private String predictionType;

    public Location ()
    {
        name = "";
    }

    public Location(String name)
    {
        this.name = name;
    }

    public int getLocationID() {
        return locationID;
    }

    public void setLocationID(int locationID) {
        locationID = locationID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocationCode() {
        return locationCode;
    }

    public void setLocationCode(String locationCode) {
        this.locationCode = locationCode;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getPredictionType() {
        return predictionType;
    }

    public void setPredictionType(String predictionType) {
        this.predictionType = predictionType;
    }
}
