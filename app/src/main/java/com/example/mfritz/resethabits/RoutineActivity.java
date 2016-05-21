package com.example.mfritz.resethabits;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RoutineActivity extends AppCompatActivity implements RoutineActivityFragment.OnRoutineSelectedListener {
    private final String LOG_TAG = this.getClass().getSimpleName();

    @BindView(R.id.fab) FloatingActionButton fab;
    @BindString(R.string.add_routine) String addText;

    @OnClick(R.id.fab)
    public void testListener(View view) {
        // TODO: add new routines here
        Snackbar.make(view, addText, Snackbar.LENGTH_SHORT)
                .setAction("Action", null).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routine);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public void onRoutineSelected(Uri contentUri) {
        Intent intent = new Intent(this, HabitActivity.class).setData(contentUri);
        startActivity(intent);
    }
}
