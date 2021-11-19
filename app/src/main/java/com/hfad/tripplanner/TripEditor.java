package com.hfad.tripplanner;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class TripEditor extends AppCompatActivity {
    public static final String TRIP_ID = "tripId";
    int selectedDestination = 0;
    int selectedTrip = 0;
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

        selectedTrip = (Integer)getIntent().getExtras().get(TRIP_ID);
        ListView listDestinations = (ListView) findViewById(R.id.List_options);
        SQLiteOpenHelper tripDatabaseHelper = new TripDatabaseHelper(this);
        try{
            SQLiteDatabase db = tripDatabaseHelper.getReadableDatabase();
            Cursor cursor = db.query("DESTINATION",
                    new String[] {"_id" ,"CITY"},
                    "TRIP_ID = ?",
                    new String[] {Integer.toString(selectedTrip)},
                    null, null, null);

            SimpleCursorAdapter listAdapter = new SimpleCursorAdapter(this,
                    android.R.layout.simple_list_item_1,
                    cursor,
                    new String[]{"CITY"},
                    new int[]{android.R.id.text1},
                    0);
            listDestinations.setAdapter(listAdapter);

            AdapterView.OnItemClickListener itemClickListener =
                    new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> listDestinations,
                                                View itemView,
                                                int position,
                                                long id) {
                            selectedDestination = (int) id;
                        }
                    };
            listDestinations.setOnItemClickListener(itemClickListener);
        } catch (SQLiteException e){
            Toast toast = Toast.makeText(this, "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }
    }
    
    public void openDestinationEditor(){
        Intent intent = new Intent(this, DestinationEditor.class);
        intent.putExtra(DestinationEditor.TRIP_ID, selectedTrip);
        intent.putExtra(DestinationEditor.DESTINATION_ID, selectedDestination);
        startActivity(intent);
    }
    public void goBackToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}