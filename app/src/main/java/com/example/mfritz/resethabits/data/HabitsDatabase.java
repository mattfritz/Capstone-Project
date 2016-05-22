package com.example.mfritz.resethabits.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import net.simonvt.schematic.annotation.Database;
import net.simonvt.schematic.annotation.IfNotExists;
import net.simonvt.schematic.annotation.OnCreate;
import net.simonvt.schematic.annotation.OnUpgrade;
import net.simonvt.schematic.annotation.Table;

/**
 * Created by matt on 5/18/16.
 */

@Database(version = HabitsDatabase.VERSION, packageName = "com.example.mfritz.resethabits.provider")
public class HabitsDatabase {

    public static final int VERSION = 1;

    private HabitsDatabase() {}

    public static class Tables {
        @Table(RoutineColumns.class) @IfNotExists public static final String ROUTINES = "routines";
        @Table(HabitColumns.class) @IfNotExists public static final String HABITS = "habits";
        @Table(HabitEventColumns.class) @IfNotExists public static final String HABIT_EVENTS = "habit_events";
    }

    @OnCreate public static void onCreate(Context context, SQLiteDatabase db) {
        // TODO: Add database seeds
    }

    @OnUpgrade public static void onUpgrade(
            Context context, SQLiteDatabase db, int oldVersion, int newVersion) {}
}
