package com.example.mfritz.resethabits;

import android.app.Application;
import com.facebook.stetho.Stetho;

/**
 * Created by matt on 5/19/16.
 */
public class ResetHabits extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
    }
}

