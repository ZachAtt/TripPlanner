package com.hfad.tripplanner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private SQLiteDatabase db;
    TripDatabaseHelper tripDatabaseHelper;
    int selectedTrip = -1;
    private Cursor cursor;
    Button addButton;
    Button updateButton;
    Button deleteButton;
    Button viewButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listTrips = (ListView) findViewById(R.id.List_options);
        tripDatabaseHelper = new TripDatabaseHelper(this);
        boolean haveItems = false;
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
            haveItems = cursor.getCount() > 0;
        } catch (SQLiteException e){
            Toast toast = Toast.makeText(this, "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }

        addButton = (Button) findViewById(R.id.btn_add);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTrip();
            }
        });


        updateButton = (Button) findViewById(R.id.btn_update);
        updateButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(selectedTrip >= 0){
                    openTripEditor();
                    }
                }
            });

        deleteButton = (Button) findViewById(R.id.btn_delete);
        deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(selectedTrip >= 0){
                       deleteTrip();
                    }
                }
            });

        viewButton = (Button) findViewById(R.id.btn_view);
        viewButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(selectedTrip >= 0){
                        openTripViewer();
                    }
                }
            });

        updateButton.setEnabled(haveItems);
        deleteButton.setEnabled(haveItems);
        viewButton.setEnabled(haveItems);
    }

    public void addTrip(){
        EditText tripEdit = (EditText) findViewById(R.id.newTrip);

        if(tripEdit.getText().length() > 0){
            Trip trip = new Trip();
            trip.setName(tripEdit.getText().toString());
            tripDatabaseHelper.insertTrip(trip);
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }
    public void openTripEditor(){
        Intent intent = new Intent(this, TripEditor.class);
        intent.putExtra(TripEditor.TRIP_ID, selectedTrip);
        startActivity(intent);
    }
    public void openTripViewer(){
        Intent intent = new Intent(this, TripViewer.class);
        intent.putExtra(TripViewer.TRIP_ID, selectedTrip);
        startActivity(intent);
    }
    public void deleteTrip(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle("Confirm Delete Trip");
        builder.setMessage("Are you sure you want to delete this trip? All data for this trip will" +
                "be deleted and cannot be recovered");
        builder.setPositiveButton("Confirm",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        doDelete();
                    }
                });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int x = 1;
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void doDelete(){
        tripDatabaseHelper.deleteTrip(selectedTrip);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}