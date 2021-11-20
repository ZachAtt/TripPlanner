package com.hfad.tripplanner;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.text.ParseException;
import java.util.Date;
import java.text.SimpleDateFormat;

public class TripDatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "tripPlanner";
    private static final int DB_VERSION = 2;
    private SQLiteDatabase db;

    TripDatabaseHelper(Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        this.db = db;
        updateMyDatabase(0, DB_VERSION);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        updateMyDatabase(oldVersion, newVersion);
    }

    private ContentValues getContentValues(Trip trip){
        ContentValues tripValues = new ContentValues();
        tripValues.put("NAME", trip.getName());

        return tripValues;
    }

    public void insertTrip(Trip trip){
        ContentValues tripValues = getContentValues(trip);
        db.insert("TRIP", null, tripValues);
    }

    public void updateTrip(Trip trip){
        ContentValues tripValues = getContentValues(trip);
        db.update("TRIP", tripValues, "_id=?", new String[]{String.valueOf(trip.getId())});
    }

    private void insertTrip(String name){
        ContentValues tripValues = new ContentValues();
        tripValues.put("NAME", name);
        db.insert("TRIP", null, tripValues);
    }

    private ContentValues getContentValues(Destination destination){
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        String arrivalDateText = dateFormat.format(destination.getArrivalDate());
        String departureDateText = dateFormat.format(destination.getDepartureDate());
        ContentValues destinationValues = new ContentValues();
        destinationValues.put("TRIP_ID", destination.getTripId());
        destinationValues.put("CITY", destination.getCity());
        destinationValues.put("STATE", destination.getState());
        destinationValues.put("COUNTRY", destination.getCountry());
        destinationValues.put("LAT", destination.getLat());
        destinationValues.put("LONG", destination.getLon());
        destinationValues.put("ARRIVAL_DATE", arrivalDateText);
        destinationValues.put("DEPARTURE_DATE", departureDateText);
        destinationValues.put("NEXT_DESTINATION", destination.getNextDestination());
        destinationValues.put("TRAVEL_TYPE", destination.getTravelType());
        destinationValues.put("TRAVEL_NUMBER", destination.getTravelNumber());
        destinationValues.put("TRAVEL_URL", destination.getTravelUrl());
        destinationValues.put("LODGING_NUMBER", destination.getLodgingNumber());
        destinationValues.put("LODGING_URL", destination.getLodgingUrl());

        return destinationValues;
    }

    private void insertDestination(int tripId, String city, String state, String country,
                                          double lat, double lon, Date arrivalDate, Date departureDate,
                                          int nextDestination, String travelType, int travelNumber,
                                          String travelUrl, int lodgingNumber, String lodgingUrl){
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        String arrivalDateText = dateFormat.format(arrivalDate);
        String departureDateText = dateFormat.format(departureDate);
        ContentValues destinationValues = new ContentValues();
        destinationValues.put("TRIP_ID", tripId);
        destinationValues.put("CITY", city);
        destinationValues.put("STATE", state);
        destinationValues.put("COUNTRY", country);
        destinationValues.put("LAT", lat);
        destinationValues.put("LONG", lon);
        destinationValues.put("ARRIVAL_DATE", arrivalDateText);
        destinationValues.put("DEPARTURE_DATE", departureDateText);
        destinationValues.put("NEXT_DESTINATION", nextDestination);
        destinationValues.put("TRAVEL_TYPE", travelType);
        destinationValues.put("TRAVEL_NUMBER", travelNumber);
        destinationValues.put("TRAVEL_URL", travelUrl);
        destinationValues.put("LODGING_NUMBER", lodgingNumber);
        destinationValues.put("LODGING_URL", lodgingUrl);
        db.insert("DESTINATION", null,  destinationValues);
    }

    public void insertDestination(Destination destination){
        ContentValues destinationValues = getContentValues(destination);
        db.insert("DESTINATION", null,  destinationValues);
    }

    public void updateDestination(Destination destination){
        ContentValues destinationValues = getContentValues(destination);
        db.update("DESTINATION", destinationValues, "_id=?", new String[]{String.valueOf(destination.getId())});
    }

    public Destination getDestination(int id) throws ParseException{
        Destination destination = new Destination();
            SQLiteDatabase db = getReadableDatabase();
            Cursor cursor = db.query("DESTINATION",
                    new String[] {"TRIP_ID", "CITY", "STATE", "COUNTRY", "LAT", "LONG", "ARRIVAL_DATE",
                            "DEPARTURE_DATE", "NEXT_DESTINATION", "TRAVEL_TYPE", "TRAVEL_NUMBER",
                            "TRAVEL_URL", "LODGING_NUMBER", "LODGING_URL"},
                    "_id = ?",
                    new String[] {Integer.toString(id)},
                    null, null, null);

            destination.setId(id);
            if(cursor.moveToFirst()){
                SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");

                destination.setTripId(cursor.getInt(0));
                destination.setCity(cursor.getString(1));
                destination.setState(cursor.getString(2));
                destination.setCountry(cursor.getString(3));
                destination.setLat(cursor.getDouble(4));
                destination.setLon(cursor.getDouble(5));
                destination.setArrivalDate(dateFormat.parse(cursor.getString(6)));
                destination.setDepartureDate(dateFormat.parse(cursor.getString(7)));
                destination.setNextDestination(cursor.getInt(8));
                destination.setTravelType(cursor.getString(9));
                destination.setTravelNumber(cursor.getInt(10));
                destination.setTravelUrl(cursor.getString(11));
                destination.setLodgingNumber(cursor.getInt(12));
                destination.setLodgingUrl(cursor.getString(13));
            }

            cursor.close();
            db.close();

        return destination;
    }

    private void insertJournal(int destinationId, int tripId, Date date,
                                      String entry){
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        String dateText = dateFormat.format(date);

        ContentValues journalValues = new ContentValues();
        journalValues.put("DESTINATION_ID", destinationId);
        journalValues.put("TRIP_ID", tripId);
        journalValues.put("DATE", dateText);
        journalValues.put("ENTRY", entry);
        db.insert("JOURNAL", null, journalValues);
    }

    private void insertMultimedia(int destinationId, int tripId,
                                         String fileName, Date date, String note){
        ContentValues dataValues = new ContentValues();
        dataValues.put("DESTINATION_ID", destinationId);
        dataValues.put("TRIP_ID", tripId);
        dataValues.put("FILE_NAME", fileName);
        dataValues.put("DATE", date.toString());
        dataValues.put("NOTE", note);
        db.insert("MULTIMEDIA", null, dataValues);
    }

    public void insertActivity(int destinationId, String name,
                                       String description, String category, double cost,
                                       double hours, Date date){
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        String dateText = dateFormat.format(date);
        ContentValues activityValues = new ContentValues();
        activityValues.put("DESTINATION_ID", destinationId);
        activityValues.put("NAME", name);
        activityValues.put("DESCRIPTION", description);
        activityValues.put("CATEGORY", category);
        activityValues.put("COST", cost);
        activityValues.put("HOURS", hours);
        activityValues.put("DATE", dateText);
        db.insert("ACTIVITY", null, activityValues);
    }

   private void updateMyDatabase(int oldVersion, int newVersion){
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

            insertTrip("Ski Trip");
            insertTrip("Gambling Trip");
            insertTrip("Florida Trip");

            insertDestination(1, "Raleigh", "A", "A", 48, -90, new Date(), new Date(), 1, "A", 1, "A", 1, "A");
            insertDestination(1, "Denver", "A", "A", 48, -90, new Date(), new Date(), 1, "A", 1, "A", 1, "A");
            insertDestination(1, "Vail", "A", "A", 48, -90, new Date(), new Date(), 1, "A", 1, "A", 1, "A");
            insertDestination(1, "Denver", "A", "A", 48, -90, new Date(), new Date(), 1, "A", 1, "A", 1, "A");
            insertDestination(1, "Raleigh", "A", "A", 48, -90, new Date(), new Date(), 1, "A", 1, "A", 1, "A");
            insertDestination(2, "Raleigh", "A", "A", 48, -90, new Date(), new Date(), 1, "A", 1, "A", 1, "A");
            insertDestination(2, "Las Vegas", "A", "A", 48, -90, new Date(), new Date(), 1, "A", 1, "A", 1, "A");
            insertDestination(2, "Raleigh", "A", "A", 48, -90, new Date(), new Date(), 1, "A", 1, "A", 1, "A");
            insertDestination(3, "Raleigh", "A", "A", 48, -90, new Date(), new Date(), 1, "A", 1, "A", 1, "A");
            insertDestination(3, "Orlando", "A", "A", 48, -90, new Date(), new Date(), 1, "A", 1, "A", 1, "A");
            insertDestination(3, "Miami", "A", "A", 48, -90, new Date(), new Date(), 1, "A", 1, "A", 1, "A");
            insertDestination(3, "Raleigh", "A", "A", 48, -90, new Date(), new Date(), 1, "A", 1, "A", 1, "A");
        }

        if(oldVersion < 2) {
            //db.execSQL("ALTER TABLE TRIP ADD COLUMN FAVORITE NUMERIC;");
            db.execSQL("CREATE TABLE JOURNAL (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "DESTINATION_ID INTEGER, " +
                    "TRIP_ID INTEGER, " +
                    "DATE TEXT, " +
                    "ENTRY TEXT);");
            db.execSQL("CREATE TABLE MULTIMEDIA (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "DESTINATION_ID INTEGER, " +
                    "TRIP_ID INTEGER, " +
                    "FILE_NAME TEXT, " +
                    "DATE TEXT, " +
                    "NOTE TEXT);");
            db.execSQL("CREATE TABLE ACTIVITY (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "DESTINATION_ID INTEGER, " +
                    "NAME TEXT, " +
                    "DESCRIPTION TEXT, " +
                    "CATEGORY TEXT, " +
                    "COST DOUBLE, " +
                    "HOURS DOUBLE, " +
                    "DATE TEXT);");
        }
            insertJournal(1, 1, new Date(), "Starting our trip");
            insertJournal(1, 2, new Date(), "Stuffed ourselves at Mile High Pancake House");
            insertJournal(1, 3, new Date(), "Enjoying the mountains");
            insertJournal(2, 4, new Date(), "Starting our trip");
            insertJournal(2, 5, new Date(), "Lady luck is with us. WINNING!");
            insertJournal(3, 6, new Date(), "Starting our trip");

            insertMultimedia(1, 1, "Image1", new Date(), "Starting our trip");
            insertMultimedia(1, 2, "Image2", new Date(), "I can see for miles");
            insertMultimedia(1, 3, "Image3", new Date(), "Majestic view");
            insertMultimedia(2, 4, "Image4", new Date(), "Starting our trip");
            insertMultimedia(2, 5, "Image5", new Date(), "Pile of chips");
            insertMultimedia(3, 6, "Image6", new Date(), "Starting our trip");

            insertActivity(2, "City tour", "Tour the city of Denver", "Tour", 100.00, 3, new Date());
            insertActivity(3, "Ski lesson", "Going to learn how to ski", "Lessons", 125.00, 2, new Date());
            insertActivity(7, "Casino", "Gambling", "Entertainment", 200.00, 3, new Date());
            insertActivity(10, "Disney World", "Enjoy the rides", "Theme Park", 250.00, 10, new Date());
        //}
    }
}
