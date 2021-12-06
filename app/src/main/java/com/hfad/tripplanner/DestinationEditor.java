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
import java.util.*;

public class DestinationEditor extends AppCompatActivity {

    Button button;
    TripDatabaseHelper tripDatabaseHelper;
    Destination destination;
    int destinationId = -1;
    public static final String DESTINATION_ID = "destinationId";

    EditText cityEdit;
    EditText stateEdit;
    EditText countryEdit;
    EditText latEdit;
    EditText lonEdit;
    EditText arrivalDateEdit;
    EditText departureDateEdit;
    EditText travelTypeEdit;
    EditText travelNumberEdit;
    EditText travelUrlEdit;
    EditText lodgingNumberEdit;
    EditText lodgingUrlEdit;

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

        cityEdit = (EditText) findViewById(R.id.City);
        stateEdit = (EditText) findViewById(R.id.State);
        countryEdit = (EditText) findViewById(R.id.Country);
        latEdit = (EditText) findViewById(R.id.Latitude);
        lonEdit = (EditText) findViewById(R.id.Longitude);
        arrivalDateEdit = (EditText) findViewById(R.id.Arrival_Date);
        departureDateEdit = (EditText) findViewById(R.id.Departure_Date);
        travelTypeEdit = (EditText) findViewById(R.id.Travel_type);
        travelNumberEdit = (EditText) findViewById(R.id.Travel_number);
        travelUrlEdit = (EditText) findViewById(R.id.Travel_url);
        lodgingNumberEdit = (EditText) findViewById(R.id.Lodging_number);
        lodgingUrlEdit = (EditText) findViewById(R.id.Lodging_url);

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
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        Date arrivalDate = new Date();
        Date departureDate = new Date();

        try{
            arrivalDate = dateFormat.parse(arrivalDateEdit.getText().toString());
            departureDate = dateFormat.parse(departureDateEdit.getText().toString());
        }catch (ParseException e){
            Toast toast = Toast.makeText(this, "Could parse date, current date used", Toast.LENGTH_SHORT);
            toast.show();
        }

        destination.setCity(cityEdit.getText().toString());
        destination.setState(stateEdit.getText().toString());
        destination.setCountry(countryEdit.getText().toString());
        destination.setLat(Double.parseDouble(latEdit.getText().toString()));
        destination.setLon(Double.parseDouble(lonEdit.getText().toString()));
        destination.setArrivalDate(arrivalDate);
        destination.setDepartureDate(departureDate);
        destination.setTravelType(travelTypeEdit.getText().toString());
        destination.setTravelNumber(Integer.parseInt(travelNumberEdit.getText().toString()));
        destination.setTravelUrl(travelUrlEdit.getText().toString());
        destination.setLodgingNumber(Integer.parseInt(lodgingNumberEdit.getText().toString()));
        destination.setLodgingUrl(lodgingUrlEdit.getText().toString());

        tripDatabaseHelper.updateDestination(destination);
    }

    public void openNoteViewer(){
        Intent intent = new Intent(this, NoteViewer.class);
        intent.putExtra(NoteViewer.DESTINATION_ID, destinationId);
        startActivity(intent);
    }
}