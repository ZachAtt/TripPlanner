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
import android.widget.TextView;
import android.widget.Toast;

import com.example.easywaylocation.EasyWayLocation;
import com.example.easywaylocation.Listener;

import java.text.DecimalFormat;

public class DestinationViewer extends AppCompatActivity implements Listener {
    Button button;
    int selectedTrip = 0;
    public static final String TRIP_ID = "tripId";
    public static final String DESTINATION_ID = "destinationId";

    //    Location variables....
    EasyWayLocation easyWayLocation;
    private double destLat, destLon;

    private TextView textViewMiles, textViewKilometer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_destination_viewer);

        button = (Button) findViewById(R.id.btn_ok);
        textViewMiles = findViewById(R.id.distance_miles);
        textViewKilometer = findViewById(R.id.distance_kilometers);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToTripViewer();
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
        TextView latEdit = (TextView) findViewById(R.id.Latitude);
        TextView lonEdit = (TextView) findViewById(R.id.Longitude);
        int destinationId = (Integer)getIntent().getExtras().get(DESTINATION_ID);
        selectedTrip = (Integer)getIntent().getExtras().get(TRIP_ID);
        cityEdit.setText(String.valueOf(destinationId));

        SQLiteOpenHelper tripDatabaseHelper = new TripDatabaseHelper(this);
        try{
            SQLiteDatabase db = tripDatabaseHelper.getReadableDatabase();
            Cursor cursor = db.query("DESTINATION",
                    new String[] {"CITY", "LAT", "LONG"},
                    "_id = ?",
                    new String[] {Integer.toString(destinationId)},
                    null, null, null);

            if(cursor.moveToFirst()){
                String cityText = cursor.getString(0);
                destLat = cursor.getDouble(1);
                destLon = cursor.getDouble(2);

                cityEdit.setText(cityText);
                latEdit.setText(String.valueOf(destLat));
                lonEdit.setText(String.valueOf(destLon));
            }


            cursor.close();
            db.close();


        } catch (SQLiteException e){
            Toast toast = Toast.makeText(this, "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }

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

    public void openDirectionViewer() {
        Intent intent = new Intent(this, Direction.class);
        startActivity(intent);
    }

    @Override
    public void locationOn() {

    }

    @Override
    public void currentLocation(Location location) {
        double lati = location.getLatitude();
        double longi = location.getLongitude();
        easyWayLocation.endUpdates();

        if (destLat != 0.0 && destLon != 0.0) {
            Location currentLocation = new Location("currentLocation");
            currentLocation.setLatitude(lati);
            currentLocation.setLongitude(longi);

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
