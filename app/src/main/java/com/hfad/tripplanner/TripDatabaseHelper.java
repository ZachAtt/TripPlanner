package com.hfad.tripplanner;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.view.View;
import android.widget.AdapterView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class TripDatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "tripPlanner";
    private static final int DB_VERSION = 3;
    private SQLiteDatabase db;

    TripDatabaseHelper(Context context){
        super(context, DB_NAME, null, DB_VERSION);
        db = getWritableDatabase();
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

    public ArrayList<Trip> getTripList(){
        db = getWritableDatabase();
        ArrayList<Trip> list = new ArrayList<Trip>();
        try{
            Cursor cursor = db.query("TRIP",
                    new String[]{"_id", "NAME"},
                    null, null, null, null, null);
            if(cursor.getCount() > 0){
                cursor.moveToFirst();
                do{
                    Trip trip = new Trip();
                    trip.setId(cursor.getInt(0));
                    trip.setName(cursor.getString(1));
                }while (cursor.moveToNext());
            }
        } catch (SQLiteException e){
            //Toast toast = Toast.makeText(this, "Database unavailable", Toast.LENGTH_SHORT);
            //toast.show();
        }

        return list;
    }

    public void insertTrip(Trip trip){
        db = getWritableDatabase();
        ContentValues tripValues = getContentValues(trip);
        db.insert("TRIP", null, tripValues);
    }

    public void updateTrip(Trip trip){
        db = getWritableDatabase();
        ContentValues tripValues = getContentValues(trip);
        db.update("TRIP", tripValues, "_id=?", new String[]{String.valueOf(trip.getId())});
    }

    private void insertTrip(String name){
        db = getWritableDatabase();
        ContentValues tripValues = new ContentValues();
        tripValues.put("NAME", name);
        db.insert("TRIP", null, tripValues);
    }

    public void deleteTrip(int id){
        db = getWritableDatabase();
        db.delete("TRIP", "_id=?", new String[]{String.valueOf(id)});
    }

    public Trip getTrip(int id){
        db = getWritableDatabase();
        Trip trip = new Trip();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query("TRIP",
                new String[] {"NAME"},
                "_id = ?",
                new String[] {Integer.toString(id)},
                null, null, null);

        trip.setId(id);
        if(cursor.moveToFirst()){
            trip.setName(cursor.getString(0));
        }

        cursor.close();
        db.close();

        return trip;
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
        db = getWritableDatabase();
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
        db = getWritableDatabase();
        ContentValues destinationValues = getContentValues(destination);
        db.insert("DESTINATION", null,  destinationValues);
    }

    public void updateDestination(Destination destination){
        db = getWritableDatabase();
        ContentValues destinationValues = getContentValues(destination);
        db.update("DESTINATION", destinationValues, "_id=?", new String[]{String.valueOf(destination.getId())});
    }

    public void deleteDestination(int id){
        db = getWritableDatabase();
        db.delete("DESTINATION", "_id=?", new String[]{String.valueOf(id)});
    }

    public Destination getDestination(int id) throws ParseException{
        db = getWritableDatabase();
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

    private void insertMultimedia(int destinationId, int tripId,
                                         String fileName, Date date, String note){
        db = getWritableDatabase();
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
        db = getWritableDatabase();
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

    public long insertNote(int destinationId, String note) {
        db = getWritableDatabase();
        //SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("DESTINATION_ID", destinationId);
        contentValues.put("NOTE", note);
        return db.insert("NOTE", null, contentValues);
    }

    public void deleteNote(int noteId) {
        db = getWritableDatabase();
        //SQLiteDatabase db = this.getWritableDatabase();
        db.delete("NOTE", "_id=?", new String[]{String.valueOf(noteId)});
    }

    public void updateNote(int noteId, String note) {
        db = getWritableDatabase();
        //SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("NOTE", note);
        db.update("NOTE", contentValues, "_id=?", new String[]{String.valueOf(noteId)});
    }

    public List<Note> getAllNotes(int destinationId) {
        db = getWritableDatabase();
        //SQLiteDatabase db = this.getReadableDatabase();
        List<Note> noteList = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT _id, DESTINATION_ID, NOTE FROM NOTE where DESTINATION_ID =?",
                new String[]{Integer.toString(destinationId)});

        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                Note note = new Note(cursor.getInt(0), cursor.getInt(1), cursor.getString(2));
                noteList.add(note);
            }
        }
        cursor.close();
        return noteList;
    }

   private void updateMyDatabase(int oldVersion, int newVersion){
       db = getWritableDatabase();
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

       if (oldVersion < 3){
           db.execSQL("DROP TABLE JOURNAL");

           db.execSQL("CREATE TABLE NOTE (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                   "DESTINATION_ID INTEGER, " +
                   "NOTE TEXT);");

           insertNote(1, "Starting our trip");
           insertNote(2, "Stuffed ourselves at Mile High Pancake House");
           insertNote(3, "Enjoying the mountains");
           insertNote(6, "Starting our trip");
           insertNote(7, "Lady luck is with us. WINNING!");
           insertNote(9, "Starting our trip");
       }

    }
}
