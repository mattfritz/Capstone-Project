package com.example.mfritz.resethabits.widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.database.Cursor;
import android.widget.RemoteViews;

import com.example.mfritz.resethabits.R;
import com.example.mfritz.resethabits.data.HabitsProvider.Routines;
import com.example.mfritz.resethabits.data.RoutineColumns;
import com.example.mfritz.resethabits.data.RoutinesAdapter;

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
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        ComponentName widgetProvider = new ComponentName(this, ResetHabitsWidgetProvider.class);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(widgetProvider);

        Cursor c = getContentResolver().query(Routines.CONTENT_URI, RoutinesAdapter.PROJECTION, null, null, null);

        if (c == null) return;
        if (c.moveToFirst()) {
            int countIndex = c.getColumnIndex(RoutineColumns.HABIT_COUNT);
            String total = Integer.toString(c.getInt(countIndex));
            int completeIndex = c.getColumnIndex(RoutineColumns.COMPLETE_HABIT_COUNT);
            String totalComplete = Integer.toString(c.getInt(completeIndex));

            String widgetText = totalComplete + " / " + total;

            for (int appWidgetId : appWidgetIds) {
                RemoteViews views = new RemoteViews(getPackageName(), R.layout.widget);

                views.setTextViewText(R.id.widget_text, widgetText);

                appWidgetManager.updateAppWidget(appWidgetId, views);
            }
        }

        c.close();
    }
}
