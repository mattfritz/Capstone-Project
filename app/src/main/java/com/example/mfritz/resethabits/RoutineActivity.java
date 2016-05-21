package com.example.mfritz.resethabits;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RoutineActivity extends AppCompatActivity implements RoutineActivityFragment.OnRoutineSelectedListener {
    private final String LOG_TAG = this.getClass().getSimpleName();

    @BindView(R.id.fab) FloatingActionButton fab;
    @BindView(R.id.toolbar) Toolbar toolbar;

    @OnClick(R.id.fab)
    public void createRoutine() {
        View alertView = LayoutInflater.from(this).inflate(R.layout.dialog_create_routine, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        AlertDialog dialog = builder.setTitle("Create Routine")
                .setView(alertView)
                .create();

        dialog.show();
    }

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
}
