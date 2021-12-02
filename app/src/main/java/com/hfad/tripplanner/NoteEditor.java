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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;

public class NoteEditor extends AppCompatActivity {
    Button button;
    int selectedTrip = 0;
    int selectedDestination = -1;
    int selectedJournal = -1;
    TripDatabaseHelper tripDatabaseHelper;
    Journal journal;

    public static final String TRIP_ID = "tripId";
    public static final String DESTINATION_ID = "destinationId";
    public static final String JOURNAL_ID = "journalId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_editor);

        int journalId = (Integer) getIntent().getExtras().get(JOURNAL_ID);
        selectedTrip = (Integer) getIntent().getExtras().get(TRIP_ID);

        SQLiteOpenHelper tripDatabaseHelper = new TripDatabaseHelper(this);
        try{
            SQLiteDatabase db = tripDatabaseHelper.getReadableDatabase();
            Cursor cursor = db.query("JOURNAL",
                    new String[] {"DESTINATION_ID", "TRIP_ID", "DATE", "ENTRY"},
                    "_id = ?",
                    new String[] {Integer.toString(journalId)},
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
                backToDestinationViewer();
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

        button = (Button) findViewById(R.id.btn_cancel);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToDestinationEditor();
            }
        });

    }


    private void backToDestinationViewer() {
        Intent intent = new Intent(this, DestinationViewer.class);
        startActivity(intent);
    }

    public void doSave(){
        Intent intent = new Intent(this, NoteEditor.class);
        startActivity(intent);
    }

    public void doDelete(){
        Intent intent = new Intent(this, NoteEditor.class);
        startActivity(intent);
    }

    private void backToDestinationEditor() {
        Intent intent = new Intent(this, DestinationEditor.class);
        startActivity(intent);
    }
}