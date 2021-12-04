package com.hfad.tripplanner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.*;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.*;
import android.app.AlertDialog;

public class NoteEditor extends AppCompatActivity {
    Button button;
    int selectedTrip = 0;
    int selectedDestination = -1;
    int selectedJournal = -1;
    int openedBy = 0;
    TripDatabaseHelper tripDatabaseHelper;
    Journal journal;

    public static final String TRIP_ID = "tripId";
    public static final String DESTINATION_ID = "destinationId";
    public static final String OPENED_BY = "openedBy";
    public static final int DESTINATION_EDITOR = 0;
    public static final int DESTINATION_VIEWER = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_editor);

        openedBy = (Integer) getIntent().getExtras().get(OPENED_BY);


       selectedTrip = (Integer) getIntent().getExtras().get(TRIP_ID);
       selectedDestination = (Integer) getIntent().getExtras().get(DESTINATION_ID);

        SQLiteOpenHelper tripDatabaseHelper = new TripDatabaseHelper(this);
        try{
            SQLiteDatabase db = tripDatabaseHelper.getReadableDatabase();
            Cursor cursor = db.query("JOURNAL",
                    new String[] {"DESTINATION_ID", "TRIP_ID", "DATE", "ENTRY"},
                    "_id = ?",
                    new String[] {Integer.toString(selectedJournal)},
                    null, null, null);

            if (cursor.moveToFirst()) {
                String destinationId = cursor.getString(0);
                String tripId = cursor.getString(1);
                String dateText = cursor.getString(2);
                String entryText = cursor.getString(3);

                TextView city = (TextView) findViewById(R.id.city);
                city.setText(destinationId);

                TextView date = (TextView) findViewById(R.id.date);
                date.setText(dateText);

                TextView entry = (TextView) findViewById(R.id.entry);
                entry.setText(entryText);
            }

            cursor.close();
            db.close();

        } catch (SQLiteException e) {
            Toast toast = Toast.makeText(this, "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }

        button = (Button) findViewById(R.id.btn_add);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doAdd();
            }
        });

        button = (Button) findViewById(R.id.btn_save);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doSave();
            }
        });

        button = (Button) findViewById(R.id.btn_delete);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doDelete();
            }
        });

        button = (Button) findViewById(R.id.btn_done);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToDestination();
            }
        });

    }


    private void backToDestination() {
        Intent intent;
        if(openedBy == DESTINATION_EDITOR){
            intent = new Intent(this, DestinationEditor.class);
            intent.putExtra(DestinationEditor.TRIP_ID, selectedTrip);
            intent.putExtra(DestinationEditor.DESTINATION_ID, selectedDestination);
        }
        else{
            intent = new Intent(this, DestinationViewer.class);
            intent.putExtra(DestinationViewer.TRIP_ID, selectedTrip);
            intent.putExtra(DestinationViewer.DESTINATION_ID, selectedDestination);
        }

        startActivity(intent);
    }
    public void deleteJournal() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle("Confirm Delete note");
        builder.setMessage("Are you sure you want to delete this note? All data for this" +
                " note will be deleted and cannot be recovered");
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
    }
    

    public void doSave(){
       //call DatabaseHelper to save contents of note text edit to database for selected note
        //No need to start new activity, just stay in this one
        //Update the note lsit to update the entry for the edited note
        
        EditText cityEdit = (EditText) findViewById(R.id.City);
        EditText dateEdit = (EditText) findViewById(R.id.date);
        EditText entryEdit = (EditText) findViewById(R.id.entry);

        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        Date date;
        try {
            date = sdf.parse(dateEdit.getText().toString());
            journal.setDate(date);
        }catch (ParseException e){
            //Do nothing, keep existing date value
            //Add a popup error dialog
        }


        journal.setDestinationId(Integer.parseInt(cityEdit.getText().toString()));
        journal.setEntry(entryEdit.getText().toString());
        tripDatabaseHelper.updateJournal(journal);
    }

    public void doDelete(){
        //call DatabaseHelper to selected note
        //No need to start new activity, just stay in this one
        //Update the note list to remove the deleted note
        TripDatabaseHelper tripDatabaseHelper = new TripDatabaseHelper(this);
        tripDatabaseHelper.deleteJournal(selectedJournal);
    }

    public void doAdd(){
        //call DatabaseHelper to save contents of note text edit to database as a new note
        //No need to start new activity, just stay in this one
        //Update note list with new note and make it the selected note
        
         EditText cityEdit = (EditText) findViewById(R.id.City);
        EditText dateEdit = (EditText) findViewById(R.id.date);
        EditText entryEdit = (EditText) findViewById(R.id.entry);

        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        Date date;
        try {
            date = sdf.parse(dateEdit.getText().toString());
            journal.setDate(date);
        }catch (ParseException e){
            //Do nothing, keep existing date value
            //Add a popup error dialog
        }

        journal.setDestinationId(Integer.parseInt(cityEdit.getText().toString()));
        journal.setEntry(entryEdit.getText().toString());
        tripDatabaseHelper.updateJournal(journal);
    }
}
