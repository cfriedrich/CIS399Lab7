package edu.uoregon.cnf.tidetracker;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;

/**
 * Created by Christopher on 7/12/2016.
 */
public class TideTrackerDBHelper extends SQLiteOpenHelper {

    // Database Constants
    public static final String DB_NAME = "tidetracker.db";
    public static final int DB_VERSION = 1;

    // Table constants
    public static final String LOCATION_TABLE = "locations";
    public static final String PREDICTION_TABLE = "predictions";

    public static final String LOCATION_ID = "locationID";
    public static final int LOCATION_ID_COL = 0;

    public static final String LOCATION_NAME = "name";
    public static final int LOCATION_NAME_COL = 1;

    public static final String LOCATION_CODE = "code";
    public static final int LOCATION_CODE_COL = 2;

    public static final String LOCATION_LAT = "latitude";
    public static final int LOCATION_LAT_COL = 3;

    public static final String LOCATION_LON = "longitude";
    public static final int LOCATION_LON_COL = 4;

    public static final String PREDICTION_ID = "predictionID";
    public static final int PREDICTION_ID_COL = 0;

    public static final String PREDICTION_LOCATION_ID = "locationID";
    public static final int PREDICTION_LOCATION_COL = 1;

    public static final String PREDICTION_DATE = "date";
    public static final int PREDICTION_DATE_COL = 2;

    public static final String PREDICTION_TIME = "time";
    public static final int PREDICTION_TIME_COL = 3;

    public static final String PREDICTION_HIGHLOW = "highlow";
    public static final int PREDICTION_HIGHLOW_COL = 4;

    public static final String PREDICTION_FEET = "feet";
    public static final int PREDICTION_FEET_COL = 5;

    public static final String PREDICTION_CM = "centimeters";
    public static final int PREDICTION_CM_COL = 6;

    public static final String CREATE_LOCATION_TABLE =
            "CREATE TABLE " + LOCATION_TABLE + " (" +
                    LOCATION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    LOCATION_NAME + " TEXT NOT NULL UNIQUE, " +
                    LOCATION_CODE + " TEXT NOT NULL UNIQUE, " +
                    LOCATION_LON + " TEXT NULL, " +
                    LOCATION_LAT + " TEXT NULL;";

    public static final String CREATE_PREDICTION_TABLE =
            "CREATE TABLE " + PREDICTION_TABLE + " (" +
                    PREDICTION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    PREDICTION_LOCATION_ID + " INTEGER NOT NULL, " +
                    PREDICTION_DATE + " TEXT NOT NULL, " +
                    PREDICTION_TIME + " TEXT NOT NULL, " +
                    PREDICTION_HIGHLOW + " TEXT NOT NULL, " +
                    PREDICTION_FEET + " TEXT NOT NULL, " +
                    PREDICTION_CM + " TEXT NOT NULL);";

    public static final String DROP_LOCATION_TABLE =
            "DROP TABLE IF EXISTS " + LOCATION_TABLE;

    public static final String DROP_PREDICTION_TABLE =
            "DROP TABLE IF EXISTS " + PREDICTION_TABLE;

    public TideTrackerDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version)
    {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_LOCATION_TABLE);
        db.execSQL(CREATE_PREDICTION_TABLE);
    }

    public void insertPrediction(SQLiteDatabase db, DataItem item, String location, String locationCode, String locationLat, String locationLon)
    {
        
        db.execSQL("INSERT OR IGNORE INTO " + LOCATION_TABLE + " VALUES (NULL, " + location + ", " + locationCode +
                ", " + locationLat + ", " + locationLon + " );");

        db.execSQL("INSERT INTO " + PREDICTION_TABLE + " VALUES (NULL, '" +
                item.getLocation() + "', '" + item.getShortDate() + "', '" + item.getTimeString() + "', '" +
                item.getHighlow() + "', '" + item.getFeet() + "', '" + item.getCentimeters() + "');");
    }

    public void populateDatabase(SQLiteDatabase db, String[] locations, ArrayList<DataItem> predictions)
    {
        HashMap<String, Integer> locs = new HashMap<String, Integer>();

        // I wanted to do this cleaner... :P  Ran out of time
        locs.put("ast", 1);
        locs.put("flo", 2);
        locs.put("gol", 3);
        locs.put("sou", 4);

        // Rebuild the entire database
        onUpgrade(db, 1, 1);
        Dictionary<String, String> locDict = null;

        for(int i = 0; i < (locations.length); i++)
        {
            db.execSQL("INSERT INTO " + LOCATION_TABLE + " VALUES (NULL, '" + locations[i] + "')");
        }

        for(int i = 0; i < predictions.size(); i++)
        {
            DataItem item = predictions.get(i);
            int locID = locs.get(item.getLocation());
            db.execSQL("INSERT INTO " + PREDICTION_TABLE + " VALUES (NULL, '" +
                    String.valueOf(locID) + "', '" + item.getShortDate() + "', '" + item.getTimeString() + "', '" +
                    item.getHighlow() + "', '" + item.getFeet() + "', '" + item.getCentimeters() + "');");
        }

    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(TideTrackerDB.DROP_LOCATION_TABLE);
        db.execSQL(TideTrackerDB.DROP_PREDICTION_TABLE);
        onCreate(db);
    }
}
