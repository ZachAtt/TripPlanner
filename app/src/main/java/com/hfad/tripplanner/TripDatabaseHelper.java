package com.hfad.tripplanner;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TripDatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "tripPlanner";
    private static final int DB_VERSION = 1;

    TripDatabaseHelper(Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        updateMyDatabase(db, 0, DB_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        updateMyDatabase(db, oldVersion, newVersion);
    }

    private static void insertTrip(SQLiteDatabase db, String name){
        ContentValues tripValues = new ContentValues();
        tripValues.put("NAME", name);
        db.insert("TRIP", null, tripValues);
    }

    private static void insertDestination(SQLiteDatabase db, int tripId, String city){
        ContentValues destinationValues = new ContentValues();
        destinationValues.put("TRIP_ID", tripId);
        destinationValues.put("CITY", city);
        db.insert("DESTINATION", null,  destinationValues);
    }

    private void updateMyDatabase(SQLiteDatabase db, int oldVersion, int newVersion){
        if(oldVersion < 1){
            db.execSQL("CREATE TABLE TRIP (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "NAME TEXT);");
            db.execSQL("CREATE TABLE DESTINATION (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "TRIP_ID INTEGER, " +
                    "CITY TEXT);");

            insertTrip(db, "Ski Trip");
            insertTrip(db, "Gambling Trip");
            insertTrip(db, "Florida Trip");

            insertDestination(db, 1, "Raleigh");
            insertDestination(db, 1, "Denver");
            insertDestination(db, 1, "Vail");
            insertDestination(db, 1, "Denver");
            insertDestination(db, 1, "Raleigh");
            insertDestination(db, 2, "Raleigh");
            insertDestination(db, 2, "Las Vegas");
            insertDestination(db, 2, "Raleigh");
            insertDestination(db, 3, "Raleigh");
            insertDestination(db, 3, "Orlando");
            insertDestination(db, 3, "Miami");
            insertDestination(db, 3, "Raleigh");
        }
        /*
        if(oldVersion < 2){
            db.execSQL("ALTER TABLE TRIP ADD COLUMN FAVORITE NUMERIC;");
        }
        */
    }
}
