package com.hfad.tripplanner;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class TripEditor extends AppCompatActivity {
    public static final String TRIP_ID = "tripId";
    private SQLiteDatabase db;
    private Cursor cursor;
    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tripeditor);
        
        button = (Button) findViewById(R.id.btn_add);
        button.setOnClickListener(new View.OnClickListener() {
        @Override
            public void onClick(View v) {
                openDestinationEditor();
            }
        });
         
        button = (Button) findViewById(R.id.btn_update);
        button.setOnClickListener(new View.OnClickListener() {
        @Override
           public void onClick(View v) {
               openDestinationEditor();
           }
         });
         
        button = (Button) findViewById(R.id.btn_cancel);
        button.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
               goBackToMainActivity();
         }
      });

        int tripId = (Integer)getIntent().getExtras().get(TRIP_ID);

        ListView listDestinations = (ListView) findViewById(R.id.List_options);
        SQLiteOpenHelper tripDatabaseHelper = new TripDatabaseHelper(this);
        try{
            SQLiteDatabase db = tripDatabaseHelper.getReadableDatabase();
            Cursor cursor = db.query("DESTINATION",
                    new String[] {"_id" ,"CITY"},
                    "TRIP_ID = ?",
                    new String[] {Integer.toString(tripId)},
                    null, null, null);

            SimpleCursorAdapter listAdapter = new SimpleCursorAdapter(this,
                    android.R.layout.simple_list_item_1,
                    cursor,
                    new String[]{"CITY"},
                    new int[]{android.R.id.text1},
                    0);
            listDestinations.setAdapter(listAdapter);

                    /*
            if(cursor.moveToFirst()){
                String cityText = cursor.getString(0);

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
        } catch (SQLiteException e){
            Toast toast = Toast.makeText(this, "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }
    }
    
    public void openDestinationEditor(){
        Intent intent = new Intent(this, DestinationEditor.class);
        startActivity(intent);
    }
    public void goBackToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

    }
}