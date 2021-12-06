package com.hfad.tripplanner;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
    int selectedDestination = -1;
    int selectedTrip = 0;
    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tripeditor);

        boolean haveItems = false;
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
            haveItems = cursor.getCount() > 0;
        } catch (SQLiteException e){
            Toast toast = Toast.makeText(this, "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }

        button = (Button) findViewById(R.id.btn_add);
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

        if(haveItems){
            button = (Button) findViewById(R.id.btn_update);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(selectedDestination >= 0){
                        openDestinationEditor();
                    }
                }
            });

            button = (Button) findViewById(R.id.btn_delete);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(selectedDestination >= 0){
                        deleteDestination();
                    }
                }
            });
        }
    }
    
    public void openDestinationEditor(){
        Intent intent = new Intent(this, DestinationEditor.class);
        intent.putExtra(DestinationEditor.DESTINATION_ID, selectedDestination);
        startActivity(intent);
    }

    public void deleteDestination(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle("Confirm Delete Destination");
        builder.setMessage("Are you sure you want to delete this destination? All data for this" +
                " destination will be deleted and cannot be recovered");
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
        TripDatabaseHelper tripDatabaseHelper = new TripDatabaseHelper(this);
        tripDatabaseHelper.deleteDestination(selectedDestination);
    }

    public void goBackToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}