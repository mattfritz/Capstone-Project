package com.example.mfritz.resethabits;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.example.mfritz.resethabits.data.HabitsProvider;
import com.example.mfritz.resethabits.data.RoutineColumns;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RoutineActivity extends AppCompatActivity implements RoutineActivityFragment.OnRoutineSelectedListener {
    private final String LOG_TAG = this.getClass().getSimpleName();

    @BindView(R.id.fab) FloatingActionButton fab;
    @BindView(R.id.toolbar) Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routine);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
    }

    @Override
    public void onRoutineSelected(Uri contentUri) {
        Intent intent = new Intent(this, HabitActivity.class).setData(contentUri);
        startActivity(intent);
    }

    @OnClick(R.id.fab)
    public void createRoutine() {
        final Context context = this;
        final View alertView = LayoutInflater.from(context).inflate(R.layout.dialog_create_routine, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        AlertDialog dialog = builder.setTitle(R.string.create_routine)
                .setView(alertView)
                .setPositiveButton(R.string.create_text, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText nameField = (EditText) alertView.findViewById(R.id.create_routine_edittext);
                        String name = nameField.getText().toString();

                        ContentValues cv = new ContentValues();
                        cv.put(RoutineColumns.NAME, name);
                        context.getContentResolver().insert(HabitsProvider.Routines.CONTENT_URI, cv);
                    }
                })
                .setNegativeButton(R.string.cancel_text, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .create();

        dialog.show();
    }

}
