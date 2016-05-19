package com.example.mfritz.resethabits;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.example.mfritz.resethabits.data.HabitsProvider.Routines;
import com.example.mfritz.resethabits.data.RoutineColumns;

import java.util.Random;

public class RoutineActivity extends AppCompatActivity {
    private final String LOG_TAG = this.getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routine);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Bad to run on main thread, just for testing
        testContentProvider();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

    }

    private void testContentProvider() {
        // Insert dummy data
        ContentValues cv = new ContentValues();
        cv.put(RoutineColumns.NAME, "TEST ROUTINE #" + new Random().nextInt());
        cv.put(RoutineColumns.ACTIVE, 1);
        getApplicationContext().getContentResolver().insert(Routines.CONTENT_URI, cv);

        // Query routines content
        Cursor c = getApplicationContext().getContentResolver().query(
                Routines.CONTENT_URI,
                null,
                null,
                null,
                null
        );

        // Test cursor results
        try {
            if (c.moveToFirst()) {
                Log.d(LOG_TAG, "QUERIED ROUTINE CONTENTURI");
                String name = c.getString(c.getColumnIndex(RoutineColumns.NAME));
                Log.d(LOG_TAG, "FIRST RESULT NAME: " + name);
                c.moveToLast();
                name = c.getString(c.getColumnIndex(RoutineColumns.NAME));
                Log.d(LOG_TAG, "LAST RESULT NAME: " + name);
                Log.d(LOG_TAG, "NUMBER OF CURSOR ITEMS: " + c.getCount());
            } else {
                Log.d(LOG_TAG, "ROUTINE CONTENTURI QUERY RESULTS EMPTY");
            }
        } catch (Exception e) {
            Log.d(LOG_TAG, "CURSOR IS NULL");
        } finally {
            if (c != null)
                c.close();
        }
    }

}
