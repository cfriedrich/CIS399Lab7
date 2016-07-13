package edu.uoregon.cnf.tidetracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;

public class TideTrackerDB {
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

    public static final String LOCATION_TYPE = "type";
    public static final int LOCATION_TYPE_COL = 5;

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
                    LOCATION_LAT + " TEXT NULL, " +
                    LOCATION_TYPE + " TEXT NULL);";

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

    // Database and Database helper objects
    private SQLiteDatabase db;
    private TideTrackerDBHelper dbHelper;

    // Constructor
    public TideTrackerDB(Context context) {
        dbHelper = new TideTrackerDBHelper(context, DB_NAME, null, DB_VERSION);
    }

    public void fillData(TideTrackerDB database, String[] locations, ArrayList<DataItem> predictions)
    {
        openWriteableDB();
        dbHelper.populateDatabase(db, locations, predictions);
    }

    private void openReadableDB() {
        db = dbHelper.getReadableDatabase();
    }

    private void openWriteableDB() {
        db = dbHelper.getWritableDatabase();
    }

    private void closeDB() {
        if (db != null)
            db.close();
    }

    private void closeCursor(Cursor cursor) {
        if (cursor != null)
            cursor.close();
    }

    public ArrayList<Location> getLocations() {
        ArrayList<Location> locations = new ArrayList<Location>();
        openReadableDB();
        Cursor cursor = db.query(LOCATION_TABLE,
                null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            Location location = new Location();
            location.setLocationID(cursor.getInt(LOCATION_ID_COL));
            location.setName(cursor.getString(LOCATION_NAME_COL));
            location.setLocationCode(cursor.getString(LOCATION_CODE_COL));
            location.setLongitude(cursor.getString(LOCATION_LON_COL));
            location.setLatitude(cursor.getString(LOCATION_LAT_COL));
            location.setPredictionType(cursor.getString(LOCATION_TYPE_COL));
            locations.add(location);
        }
        closeCursor(cursor);
        closeDB();

        return locations;
    }

    public boolean insertLocation(Location location)
    {
        openWriteableDB();
        boolean successFlag = true;
        try {
            db.execSQL("INSERT OR IGNORE INTO " + LOCATION_TABLE + " VALUES (NULL, " + location.getName() + ", " +
                    location.getLocationCode() + ", " + location.getLongitude() + ", " + location.getLatitude() + ", " +
                    location.getPredictionType() + " );");
        } catch (SQLException e) {
            successFlag = false;
        }

        return successFlag;
    }

    // Grab the location ID based on an abbreviated location
    public Location getLocationByCode(String locationCode) {

        Location location = new Location();

        openReadableDB();
        String[] whereArgs = new String[]{
                locationCode
        };

        String queryString = "SELECT * FROM locations WHERE code = ?;";
        Cursor cursor = db.rawQuery(queryString, whereArgs);
        if(cursor.getCount() > 0) {
            cursor.moveToFirst();
            location.setLocationID(cursor.getInt(LOCATION_ID_COL));
            location.setName(cursor.getString(LOCATION_NAME_COL));
            location.setLocationCode(cursor.getString(LOCATION_CODE_COL));
            location.setLongitude(cursor.getString(LOCATION_LON_COL));
            location.setLatitude(cursor.getString(LOCATION_LAT_COL));
            location.setPredictionType(cursor.getString(LOCATION_TYPE_COL));
        }
        closeCursor(cursor);
        closeDB();

        return location;
    }

    // Grab the location ID based on an abbreviated location
    public int getLocationID(String location) {

        int locationID = 0;
        openReadableDB();
        String[] whereArgs = new String[]{
            location
        };

        String queryString = "SELECT * FROM locations WHERE name != ?;";
        Cursor cursor = db.rawQuery(queryString, whereArgs);
        if(cursor.getCount() > 0) {
            cursor.moveToFirst();
            locationID = cursor.getInt(LOCATION_ID_COL);
        }
        closeCursor(cursor);
        closeDB();

        return locationID;
    }

    // Get all the prediction objects
    public ArrayList<Prediction> getPredictions(String locationID, String date, String date2) {
        ArrayList<Prediction> predictions = new ArrayList<Prediction>();
        openReadableDB();

        String queryString = "";
        String[] whereArgs = new String[3];
        if(date != date2) {
            whereArgs[0] = locationID;
            whereArgs[1] = date;
            whereArgs[2] = date2;

            queryString = "SELECT * " +
                    "FROM " + PREDICTION_TABLE + " " +
                    "WHERE " + PREDICTION_LOCATION_ID + " = ? AND " +
                    PREDICTION_DATE + " IN ( ? , ? );";
        }
        else
        {
            whereArgs[0] = locationID;
            whereArgs[1] = date;

            queryString = "SELECT * " +
                    "FROM " + PREDICTION_TABLE + " " +
                    "WHERE " + PREDICTION_LOCATION_ID + " = ? AND " +
                    PREDICTION_DATE + " = ?;";
        }
        Cursor cursor = db.rawQuery(queryString, whereArgs);
        while (cursor.moveToNext()) {
            Prediction prediction = new Prediction();
            prediction.setPredictionID(cursor.getInt(PREDICTION_ID_COL));
            prediction.setLocationID(cursor.getInt(PREDICTION_LOCATION_COL));
            prediction.setDate(cursor.getString(PREDICTION_DATE_COL));
            prediction.setTime(cursor.getString(PREDICTION_TIME_COL));
            prediction.setHighlow(cursor.getString(PREDICTION_HIGHLOW_COL));
            prediction.setFeet(cursor.getString(PREDICTION_FEET_COL));
            prediction.setCentimeters(cursor.getString(PREDICTION_CM_COL));

            predictions.add(prediction);
        }
        closeCursor(cursor);
        closeDB();

        return predictions;
    }
}
