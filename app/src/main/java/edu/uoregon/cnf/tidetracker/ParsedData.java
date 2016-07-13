package edu.uoregon.cnf.tidetracker;

import java.util.ArrayList;

public class ParsedData {

    private ArrayList<DataItem> items;

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
}
