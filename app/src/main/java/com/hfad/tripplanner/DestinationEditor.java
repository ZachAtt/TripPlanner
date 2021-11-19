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

public class DestinationEditor extends AppCompatActivity {

    Button button;
    int selectedTrip = 0;
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
                backtoTripEditor();
            }
        });

        EditText cityEdit = (EditText) findViewById(R.id.City);
        EditText latEdit = (EditText) findViewById(R.id.Latitude);
        EditText lonEdit = (EditText) findViewById(R.id.Longitude);
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



    public void backtoTripEditor(){
        Intent intent = new Intent(this, TripEditor.class);
        intent.putExtra(TripViewer.TRIP_ID, selectedTrip);
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