package com.hfad.tripplanner;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Date;

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

    private static void insertDestination(SQLiteDatabase db, int tripId, String city, String state, String country,
                                          double lat, double lon, Date arrivalDate, Date departureDate,
                                          int nextDestination, String travelType, int travelNumber,
                                          String travelUrl, int lodgingNumber, String lodgingUrl){
        ContentValues destinationValues = new ContentValues();
        destinationValues.put("TRIP_ID", tripId);
        destinationValues.put("CITY", city);
        destinationValues.put("STATE", state);
        destinationValues.put("COUNTRY", country);
        destinationValues.put("LAT", lat);
        destinationValues.put("LONG", lon);
        destinationValues.put("ARRIVAL_DATE", arrivalDate.toString());
        destinationValues.put("DEPARTURE_DATE", departureDate.toString());
        destinationValues.put("NEXT_DESTINATION", nextDestination);
        destinationValues.put("TRAVEL_TYPE", travelType);
        destinationValues.put("TRAVEL_NUMBER", travelNumber);
        destinationValues.put("TRAVEL_URL", travelUrl);
        destinationValues.put("LODGING_NUMBER", lodgingNumber);
        destinationValues.put("LODGING_URL", lodgingUrl);
        db.insert("DESTINATION", null,  destinationValues);
    }

    private void updateMyDatabase(SQLiteDatabase db, int oldVersion, int newVersion){
        if(oldVersion < 1){
            db.execSQL("CREATE TABLE TRIP (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "NAME TEXT);");
            db.execSQL("CREATE TABLE DESTINATION (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "TRIP_ID INTEGER, " +
                    "CITY TEXT, " +
                    "STATE TEXT, " +
                    "COUNTRY TEXT, " +
                    "LAT DOUBLE, " +
                    "LONG DOUBLE, " +
                    "ARRIVAL_DATE TEXT, " +
                    "DEPARTURE_DATE TEXT, " +
                    "NEXT_DESTINATION INTEGER, " +
                    "TRAVEL_TYPE TEXT, " +
                    "TRAVEL_NUMBER INTEGER, " +
                    "TRAVEL_URL TEXT, " +
                    "LODGING_NUMBER INTEGER, " +
                    "LODGING_URL TEXT);");

            insertTrip(db, "Ski Trip");
            insertTrip(db, "Gambling Trip");
            insertTrip(db, "Florida Trip");

            insertDestination(db, 1, "Raleigh", "A", "A", 48, -90, new Date(), new Date(), 1, "A", 1, "A", 1, "A");
            insertDestination(db, 1, "Denver", "A", "A", 48, -90, new Date(), new Date(), 1, "A", 1, "A", 1, "A");
            insertDestination(db, 1, "Vail", "A", "A", 48, -90, new Date(), new Date(), 1, "A", 1, "A", 1, "A");
            insertDestination(db, 1, "Denver", "A", "A", 48, -90, new Date(), new Date(), 1, "A", 1, "A", 1, "A");
            insertDestination(db, 1, "Raleigh", "A", "A", 48, -90, new Date(), new Date(), 1, "A", 1, "A", 1, "A");
            insertDestination(db, 2, "Raleigh", "A", "A", 48, -90, new Date(), new Date(), 1, "A", 1, "A", 1, "A");
            insertDestination(db, 2, "Las Vegas", "A", "A", 48, -90, new Date(), new Date(), 1, "A", 1, "A", 1, "A");
            insertDestination(db, 2, "Raleigh", "A", "A", 48, -90, new Date(), new Date(), 1, "A", 1, "A", 1, "A");
            insertDestination(db, 3, "Raleigh", "A", "A", 48, -90, new Date(), new Date(), 1, "A", 1, "A", 1, "A");
            insertDestination(db, 3, "Orlando", "A", "A", 48, -90, new Date(), new Date(), 1, "A", 1, "A", 1, "A");
            insertDestination(db, 3, "Miami", "A", "A", 48, -90, new Date(), new Date(), 1, "A", 1, "A", 1, "A");
            insertDestination(db, 3, "Raleigh", "A", "A", 48, -90, new Date(), new Date(), 1, "A", 1, "A", 1, "A");
        }
        /*
        if(oldVersion < 2){
            db.execSQL("ALTER TABLE TRIP ADD COLUMN FAVORITE NUMERIC;");
        }
        */
    }
}
