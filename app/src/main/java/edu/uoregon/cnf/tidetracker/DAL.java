package edu.uoregon.cnf.tidetracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * Created by Christopher on 7/12/2016.
 */
public class DAL {
    private Context context = null;
    private TideTrackerDB db;
    private ArrayList<HashMap<String, String>> locations = new ArrayList<HashMap<String, String>>();

    public DAL(Context context) {
        this.context = context;
        db = new TideTrackerDB(context);
    }

    // Parse the XML files and put the data in the db
    public void fillDBWithLocationPredictions(String xmlData, String locationCode) {
        // Get the data from the XML string
        ParsedData items = parseXml(xmlData);
        items.setLocationCode(locationCode);    // This field isn't in the xml file, so we add it here

        // Initialize database

        Location location = db.getLocationByCode(locationCode);


        // Put weather forecast in the database
        ContentValues cv = new ContentValues();

//        for(DataItem item : items.getAllItems())
//        {
//            cv.clear();
//            cv.put("Date", item.getFormattedDate());
//            cv.put("Time", item.getTimeString());
//            cv.put("LocationCode", items.getLocationCode());				// stored in items, not item
//            cv.put("LocationName", items.getLocationName());			// stored in items, not item
//            cv.put("HighLow", item.getHighlow());
//            cv.put("Feet", item.getFeet());
//            cv.put("Centimeters", item.getCentimeters());
//            db.;//("Predictions", null, cv);
//        }
//        db.close();
    }

    public ArrayList<Prediction> getPredictionsFromDb(TideTrackerDB db, String location, String date1, String date2)
    {
        ArrayList<Prediction> predictions = new ArrayList<Prediction>();

        predictions.addAll(db.getPredictions(location, date1, date2));
        //SQLiteDatabase db = helper.getReadableDatabase();

        // Get a weather forecast for one location
//        String query = "SELECT * FROM Forecast WHERE Zip = ? ORDER BY Date ASC";
//        // "SELECT * FROM Forecast WHERE Zip = ? AND Date = ? ORDER BY Date ASC";
//        String[] variables = new String[]{location};    // rawQuery must not include a trailing ';'
//        return db.rawQuery(query, variables);
        return predictions;
    }

    public ParsedData parseXml(String xmlData) {
        try {
            // get the XML reader
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();
            XMLReader xmlreader = parser.getXMLReader();

            // set content handler
            DataParser dataParser = new DataParser();
            xmlreader.setContentHandler(dataParser);

            // parse the data
            xmlreader.parse(new InputSource(new StringReader(xmlData)));

            // set the weather items
            ParsedData data = dataParser.getFeed();
            return data;
        } catch (Exception e) {
            return null;
        }
    }
}
