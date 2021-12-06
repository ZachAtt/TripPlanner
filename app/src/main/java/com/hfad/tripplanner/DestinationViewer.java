package com.hfad.tripplanner;

import static com.example.easywaylocation.EasyWayLocation.LOCATION_SETTING_REQUEST_CODE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.easywaylocation.EasyWayLocation;
import com.example.easywaylocation.Listener;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class DestinationViewer extends AppCompatActivity implements Listener {
    Button button;
    int selectedTrip = -1;
    int destinationId = -1;
    public static final String TRIP_ID = "tripId";
    public static final String DESTINATION_ID = "destinationId";
    
    Destination destination;
    TripDatabaseHelper tripDatabaseHelper;

    //    Location variables....
    EasyWayLocation easyWayLocation;
    private double destLat, destLon;
    private double currentLat, currentLng;
    private String cityName = "";
    private TextView textViewMiles, textViewKilometer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_destination_viewer);

        selectedTrip = (Integer)getIntent().getExtras().get(TRIP_ID);
        destinationId = (Integer)getIntent().getExtras().get(DESTINATION_ID);

        button = (Button) findViewById(R.id.btn_ok);
        textViewMiles = findViewById(R.id.distance_miles);
        textViewKilometer = findViewById(R.id.distance_kilometers);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToTripViewer();
            }
        });

        button = (Button) findViewById(R.id.btn_note);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNoteViewer();
            }
        });

        button = (Button) findViewById(R.id.btn_direction);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDirectionViewer();
            }
        });

        TextView cityEdit = (TextView) findViewById(R.id.City);
        TextView stateEdit = (TextView) findViewById(R.id.State);
        TextView countryEdit = (TextView) findViewById(R.id.Country);
        TextView latEdit = (TextView) findViewById(R.id.Latitude);
        TextView lonEdit = (TextView) findViewById(R.id.Longitude);
        TextView arrivalDateEdit = (TextView) findViewById(R.id.Arrival_Date);
        TextView departureDateEdit = (TextView) findViewById(R.id.Departure_Date);
        TextView travelTypeEdit = (TextView) findViewById(R.id.Travel_type);
        TextView travelNumberEdit = (TextView) findViewById(R.id.Travel_number);
        TextView travelUrlEdit = (TextView) findViewById(R.id.Travel_url);
        TextView lodgingNumberEdit = (TextView) findViewById(R.id.Lodging_number);
        TextView lodgingUrlEdit = (TextView) findViewById(R.id.Lodging_url);

        tripDatabaseHelper = new TripDatabaseHelper(this);

        try{
            destination = tripDatabaseHelper.getDestination(destinationId);
        } catch (ParseException e){
            Toast toast = Toast.makeText(this, "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        String arrivalDateText = dateFormat.format(destination.getArrivalDate());
        String departureDateText = dateFormat.format(destination.getDepartureDate());

        cityName = destination.getCity();
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

        easyWayLocation = new EasyWayLocation(this, false, false, this);
        if (isPermissionGranted()) {
            easyWayLocation.startLocation();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);
        }

    }

    private boolean isPermissionGranted() {
        int permissionState = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);

        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            if (grantResults.length > 0) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    easyWayLocation.startLocation();
                }
            }
        }
    }

    public void backToTripViewer() {
        Intent intent = new Intent(this, TripViewer.class);
        intent.putExtra(TripViewer.TRIP_ID, selectedTrip);
        startActivity(intent);
    }

    public void openNoteViewer(){
        Intent intent = new Intent(this, NoteViewer.class);
        intent.putExtra(NoteViewer.DESTINATION_ID, destinationId);
        startActivity(intent);
    }

    public void openDirectionViewer() {
        Intent intent = new Intent(this, Direction.class);
        intent.putExtra("current_lat", currentLat);
        intent.putExtra("current_lng", currentLng);
        intent.putExtra("dest_lat", destLat);
        intent.putExtra("dest_lng", destLon);
        intent.putExtra("dest_name", cityName);
        startActivity(intent);
    }

    @Override
    public void locationOn() {

    }

    @Override
    public void currentLocation(Location location) {
        currentLat = location.getLatitude();
        currentLng = location.getLongitude();
        easyWayLocation.endUpdates();

        if (destLat != 0.0 && destLon != 0.0) {
            Location currentLocation = new Location("currentLocation");
            currentLocation.setLatitude(currentLat);
            currentLocation.setLongitude(currentLng);

            Location destinationLocation = new Location("destinationLocation");
            destinationLocation.setLatitude(destLat);
            destinationLocation.setLongitude(destLon);

            double distance = currentLocation.distanceTo(destinationLocation);
            textViewKilometer.setText(new DecimalFormat("##.##").format(distance / 1000) + " KM");
            textViewMiles.setText(new DecimalFormat("##.##").format(distance / 1609.344) + " Miles");
        }
    }

    @Override
    public void locationCancelled() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case LOCATION_SETTING_REQUEST_CODE:
                easyWayLocation.onActivityResult(resultCode);
                break;
        }
    }

}
