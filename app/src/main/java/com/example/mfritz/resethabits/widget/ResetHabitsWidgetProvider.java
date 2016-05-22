package com.example.mfritz.resethabits.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.example.mfritz.resethabits.R;
import com.example.mfritz.resethabits.RoutineActivity;

/**
 * Created by matt on 5/22/16.
 */
public class ResetHabitsWidgetProvider extends AppWidgetProvider {
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateWidget(context, appWidgetManager, appWidgetId);
        }
    }

    void updateWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        Intent widgetUpdateIntent = new Intent(context, ResetHabitsWidgetIntentService.class);
        context.startService(widgetUpdateIntent);

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);
        Intent openMainAppIntent = new Intent(context, RoutineActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, openMainAppIntent, 0);

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }
}
