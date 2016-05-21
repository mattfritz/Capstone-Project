package com.example.mfritz.resethabits;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HabitActivity extends AppCompatActivity {
    private final String LOG_TAG = this.getClass().getSimpleName();

    @BindView(R.id.fab) FloatingActionButton fab;
    @BindView(R.id.toolbar) Toolbar toolbar;

    @OnClick(R.id.fab)
    public void createHabit() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
    }

}
