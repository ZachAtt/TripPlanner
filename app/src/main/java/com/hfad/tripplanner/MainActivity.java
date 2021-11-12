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
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private SQLiteDatabase db;
    int selectedTrip = 0;
    private Cursor cursor;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = (Button) findViewById(R.id.btn_add);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTripActivity();
            }
        });

        button = (Button) findViewById(R.id.btn_update);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTripActivity();
            }
        });

        button = (Button) findViewById(R.id.btn_delete);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTripActivity();
            }
        });

        button = (Button) findViewById(R.id.btn_view);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTripViewer();
            }
        });

        ListView listTrips = (ListView) findViewById(R.id.List_options);
        SQLiteOpenHelper tripDatabaseHelper = new TripDatabaseHelper(this);
        try{
            db = tripDatabaseHelper.getReadableDatabase();
            cursor = db.query("TRIP",
                    new String[]{"_id", "NAME"},
                    null, null, null, null, null);

            SimpleCursorAdapter listAdapter = new SimpleCursorAdapter(this,
                    android.R.layout.simple_list_item_1,
                    cursor,
                    new String[]{"NAME"},
                    new int[]{android.R.id.text1},
                    0);
            listTrips.setAdapter(listAdapter);

            AdapterView.OnItemClickListener itemClickListener =
                    new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> listTrips,
                                                View itemView,
                                                int position,
                                                long id) {
                            selectedTrip = (int) id;
                            //Find a way to highlight selected item

                            /*
                            Intent intent = new Intent(DrinkCategoryActivity.this,
                                    DrinkActivity.class);
                            intent.putExtra(DrinkActivity.EXTRA_DRINKID, (int) id);
                            startActivity(intent);

                             */
                        }
                    };
            listTrips.setOnItemClickListener(itemClickListener);
        } catch (SQLiteException e){
            Toast toast = Toast.makeText(this, "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }
    }
    public void openTripActivity(){
        Intent intent = new Intent(this, TripEditor.class);
        intent.putExtra(TripEditor.TRIP_ID, selectedTrip);
        startActivity(intent);
    }
    public void openTripViewer(){
        Intent intent = new Intent(this, TripViewer.class);
        intent.putExtra(TripViewer.TRIP_ID, selectedTrip);
        startActivity(intent);
    }
}