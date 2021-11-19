package com.hfad.tripplanner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class DestinationViewer extends AppCompatActivity {
    Button button;
    int selectedTrip = 0;
    public static final String TRIP_ID = "tripId";
    public static final String DESTINATION_ID = "destinationId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_destination_viewer);

        button = (Button) findViewById(R.id.btn_ok);
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
                double lat = cursor.getDouble(1);
                double lon = cursor.getDouble(2);

                cityEdit.setText(cityText);
                latEdit.setText(String.valueOf(lat));
                lonEdit.setText(String.valueOf(lon));
            }


            cursor.close();
            db.close();


        } catch (SQLiteException e){
            Toast toast = Toast.makeText(this, "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
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
}
