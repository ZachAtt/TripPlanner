package com.hfad.tripplanner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class DestinationEditor extends AppCompatActivity {

    Button button;
    TripDatabaseHelper tripDatabaseHelper;
    Destination destination;
    int destinationId = -1;
    public static final String DESTINATION_ID = "destinationId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_destination_editor);

        destinationId = (Integer)getIntent().getExtras().get(DESTINATION_ID);

        button = (Button) findViewById(R.id.btn_cancel);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        button = (Button) findViewById(R.id.btn_save);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doSave();
                onBackPressed();
            }
        });

        button = (Button) findViewById(R.id.btn_note);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNoteViewer();
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

    public void openNoteViewer(){
        Intent intent = new Intent(this, NoteViewer.class);
        intent.putExtra(NoteViewer.DESTINATION_ID, destinationId);
        startActivity(intent);
    }
}