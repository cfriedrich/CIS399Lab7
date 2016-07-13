package edu.uoregon.cnf.tidetracker;

import java.util.ArrayList;

public class ParsedData {

    private ArrayList<DataItem> items;

    private String locationCode;
    private String locationName;

    public ParsedData() {
        items = new ArrayList<DataItem>();
    }

    public int addItem(DataItem item) {
        items.add(item);
        return items.size();
    }

    public int addItems(ArrayList<DataItem> items)
    {
        items.addAll(items);
        return items.size();
    }

    public DataItem getItem(int index) {
        return items.get(index);
    }

    public ArrayList<DataItem> getAllItems() {
        return items;
    }

    public String getLocationCode() {
        return locationCode;
    }

    public void setLocationCode(String locationCode) {
        this.locationCode = locationCode;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }
}
