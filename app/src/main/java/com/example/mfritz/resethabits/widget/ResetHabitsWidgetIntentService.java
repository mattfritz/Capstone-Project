package com.example.mfritz.resethabits.widget;

import android.app.IntentService;
import android.content.Intent;

/**
 * Created by matt on 5/22/16.
 */
public class ResetHabitsWidgetIntentService extends IntentService {
    private final String LOG_TAG = this.getClass().getSimpleName();

    public ResetHabitsWidgetIntentService() {
        super("ResetHabitsWidgetIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

    }
}
