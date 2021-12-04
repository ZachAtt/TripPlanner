package com.hfad.tripplanner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class DestinationEditor extends AppCompatActivity {

    Button button;
    int selectedTrip = -1;
    int selectedDestination = -1;
    TripDatabaseHelper tripDatabaseHelper;
    Destination destination;
    public static final String TRIP_ID = "tripId";
    public static final String DESTINATION_ID = "destinationId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_destination_editor);

        button = (Button) findViewById(R.id.btn_cancel);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backtoTripEditor();
            }
        });

        button = (Button) findViewById(R.id.btn_save);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doSave();
                backtoTripEditor();
            }
        });

        button = (Button) findViewById(R.id.btn_note);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedTrip >= 0) {
                    openNoteEditor();
                }
            }
        });

        EditText cityEdit = (EditText) findViewById(R.id.City);
        EditText stateEdit = (EditText) findViewById(R.id.State);
        EditText countryEdit = (EditText) findViewById(R.id.Country);
        EditText latEdit = (EditText) findViewById(R.id.Latitude);
        EditText lonEdit = (EditText) findViewById(R.id.Longitude);
        EditText arrivalDateEdit = (EditText) findViewById(R.id.Arrival_Date);
        EditText departureDateEdit = (EditText) findViewById(R.id.Departure_Date);
        EditText travelTypeEdit = (EditText) findViewById(R.id.Travel_type);
        EditText travelNumberEdit = (EditText) findViewById(R.id.Travel_number);
        EditText travelUrlEdit = (EditText) findViewById(R.id.Travel_url);
        EditText lodgingNumberEdit = (EditText) findViewById(R.id.Lodging_number);
        EditText lodgingUrlEdit = (EditText) findViewById(R.id.Lodging_url);
        int destinationId = (Integer)getIntent().getExtras().get(DESTINATION_ID);
        selectedTrip = (Integer)getIntent().getExtras().get(TRIP_ID);

        tripDatabaseHelper = new TripDatabaseHelper(this);

        try{
            destination = tripDatabaseHelper.getDestination(destinationId);
        }catch (ParseException e){
            Toast toast = Toast.makeText(this, "Could not get destination", Toast.LENGTH_SHORT);
            toast.show();
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        String arrivalDateText = dateFormat.format(destination.getArrivalDate());
        String departureDateText = dateFormat.format(destination.getDepartureDate());

        cityEdit.setText(destination.getCity());
        stateEdit.setText(destination.getState());
        countryEdit.setText(destination.getCountry());
        latEdit.setText(String.valueOf(destination.getLat()));
        lonEdit.setText(String.valueOf(destination.getLon()));
        arrivalDateEdit.setText(arrivalDateText);
        departureDateEdit.setText(departureDateText);
        travelTypeEdit.setText(destination.getTravelType());
        travelNumberEdit.setText(String.valueOf(destination.getTravelNumber()));
        travelUrlEdit.setText(destination.getTravelUrl());
        lodgingNumberEdit.setText(String.valueOf(destination.getLodgingNumber()));
        lodgingUrlEdit.setText(destination.getLodgingUrl());

        /*
        (int tripId, String city, String state, String country,
                                          double lat, double lon, Date arrivalDate, Date departureDate,
                                          int nextDestination, String travelType, int travelNumber,
                                          String travelUrl, int lodgingNumber, String lodgingUrl)
         */
    }

    public void backtoTripEditor(){
        Intent intent = new Intent(this, TripEditor.class);
        intent.putExtra(TripViewer.TRIP_ID, selectedTrip);
        startActivity(intent);
    }


    public void doSave(){
        EditText cityEdit = (EditText) findViewById(R.id.City);
        EditText latEdit = (EditText) findViewById(R.id.Latitude);
        EditText lonEdit = (EditText) findViewById(R.id.Longitude);

        destination.setCity(cityEdit.getText().toString());
        destination.setLat(Double.parseDouble(latEdit.getText().toString()));
        destination.setLon(Double.parseDouble(lonEdit.getText().toString()));

        tripDatabaseHelper.updateDestination(destination);
    }

    public void openNoteEditor(){
        Intent intent = new Intent(this, NoteEditor.class);
        intent.putExtra(NoteEditor.TRIP_ID, selectedTrip);
        intent.putExtra(NoteEditor.DESTINATION_ID, selectedDestination);
        intent.putExtra(NoteEditor.OPENED_BY, NoteEditor.DESTINATION_EDITOR);
        startActivity(intent);
    }


    /*
            if(cursor.moveToFirst()){
                String cityText = cursor.getString(-1);

                TextView name = (TextView) findViewById(R.id.name);
                name.setText(cityText);

                TextView description = (TextView)findViewById(R.id.description);
                description.setText(descriptionText);

                ImageView photo = (ImageView) findViewById(R.id.photo);
                photo.setImageResource(photoId);
                photo.setContentDescription(cityText);
            }


            cursor.close();
            db.close();

                     */
}