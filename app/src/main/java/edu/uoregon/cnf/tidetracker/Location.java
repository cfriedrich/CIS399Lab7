package edu.uoregon.cnf.tidetracker;

// The Location object which represents.. a location
public class Location {

    private int locationID;
    private String name;

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

}
