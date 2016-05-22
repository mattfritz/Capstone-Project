package com.example.mfritz.resethabits.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.example.mfritz.resethabits.data.HabitsDatabase.Tables;

import net.simonvt.schematic.annotation.ContentProvider;
import net.simonvt.schematic.annotation.ContentUri;
import net.simonvt.schematic.annotation.InexactContentUri;
import net.simonvt.schematic.annotation.MapColumns;
import net.simonvt.schematic.annotation.NotifyDelete;
import net.simonvt.schematic.annotation.NotifyInsert;
import net.simonvt.schematic.annotation.NotifyUpdate;
import net.simonvt.schematic.annotation.TableEndpoint;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

/**
 * Created by matt on 5/18/16.
 */

@ContentProvider(authority = HabitsProvider.AUTHORITY,
        database = HabitsDatabase.class,
        packageName = "com.example.mfritz.resethabits.provider")
public class HabitsProvider {

    public static final String AUTHORITY = "com.example.mfritz.resethabits.HabitsProvider";
    private static final String BASE_URI = "content://" + AUTHORITY + "/";

    public static String getDay() {
        Calendar cal = Calendar.getInstance(TimeZone.getDefault());
        return cal.get(Calendar.DAY_OF_YEAR) + "-" + cal.get(Calendar.YEAR);
    }

    @TableEndpoint(table = Tables.ROUTINES)
    public static class Routines {

        @MapColumns
        public static Map<String, String> mapColumns() {
            Map<String, String> map = new HashMap<>();

            map.put(RoutineColumns.HABIT_COUNT, HABIT_COUNT);
            map.put(RoutineColumns.COMPLETE_HABIT_COUNT, COMPLETE_HABIT_COUNT);

            return map;
        }

        @ContentUri(path = "routines", type = "vnd.android.cursor.dir/routine")
        public static final Uri CONTENT_URI = Uri.parse(BASE_URI + "/routines");

        @InexactContentUri(
                path = "routines/#",
                name = "ROUTINE_ID",
                type = "vnd.android.cursor.item/routine",
                whereColumn = RoutineColumns.ID,
                pathSegment = 1 )
        public static Uri withId(long id) {
            return Uri.parse(BASE_URI + "routines/" + String.valueOf(id));
        }

        static final String HABIT_COUNT = "(SELECT COUNT(*) FROM " + Tables.HABITS +
                " WHERE " + Tables.HABITS + "." + HabitColumns.ROUTINE_ID +
                "=" + Tables.ROUTINES + "." + RoutineColumns.ID + ")";

        static final String COMPLETE_HABIT_COUNT = "(SELECT COUNT(*) FROM " + Tables.HABIT_EVENTS +
                " JOIN " + Tables.HABITS +
                " ON " + Tables.HABITS + "." + HabitColumns.ID +
                    "=" + Tables.HABIT_EVENTS + "." + HabitEventColumns.HABIT_ID +
                " JOIN " + Tables.ROUTINES +
                " ON " + Tables.ROUTINES + "." + RoutineColumns.ID +
                    "=" + Tables.HABITS + "." + HabitColumns.ROUTINE_ID +
                " WHERE " + Tables.HABITS + "." + HabitColumns.ROUTINE_ID +
                    "=" + Tables.ROUTINES + "." + RoutineColumns.ID +
                " AND " + Tables.HABIT_EVENTS + "." + HabitEventColumns.COMPLETE +
                    "= 1" +
                ")";
    }

    @TableEndpoint(table = Tables.HABITS)
    public static class Habits {

        @MapColumns
        public static Map<String, String> mapColumns() {
            Map<String, String> map = new HashMap<>();

            map.put(HabitColumns.COMPLETE_TODAY, COMPLETE_TODAY);

            return map;
        }

        @ContentUri(path = "habits", type = "vnd.android.cursor.dir/habit")
        public static final Uri CONTENT_URI = Uri.parse(BASE_URI + "/habits");

        @InexactContentUri(
                path = "habits/#",
                name = "HABIT_ID",
                type = "vnd.android.cursor.item/habit",
                whereColumn = HabitColumns.ID,
                pathSegment = 1)
        public static Uri withId(long id) {
            return Uri.parse(BASE_URI + "habits/" + String.valueOf(id));
        }

        @InexactContentUri(
                path = "habits/fromRoutine/#",
                name = "HABITS_FROM_ROUTINE",
                type = "vnd.android.cursor.dir/habit",
                whereColumn = HabitColumns.ROUTINE_ID,
                pathSegment = 2)
        public static Uri fromRoutine(long routineId) {
            return Uri.parse(BASE_URI + "habits/fromRoutine/" + String.valueOf(routineId));
        }

        static final String COMPLETE_TODAY = "(SELECT " + HabitEventColumns.COMPLETE + " FROM " + Tables.HABIT_EVENTS +
                " WHERE " + Tables.HABITS + "." + HabitColumns.ID +
                "=" + Tables.HABIT_EVENTS + "." + HabitEventColumns.HABIT_ID +
                    " AND " + Tables.HABIT_EVENTS + "." + HabitEventColumns.DATE +
                        "=" + getDay() +
                ")";

        @NotifyDelete(paths = "habits/#")
        public static Uri[] onDelete(Context context, Uri uri) {
            long habitId = Long.valueOf(uri.getLastPathSegment());
            Cursor c = context.getContentResolver().query(uri, null, null, null, null);
            c.moveToFirst();
            long routineId = c.getLong(c.getColumnIndex(HabitColumns.ROUTINE_ID));
            c.close();

            return new Uri[] { withId(habitId), fromRoutine(routineId), Routines.withId(routineId) };
        }

        @NotifyInsert(paths = "habits")
        public static Uri[] onInsert(ContentValues cv) {
            long routineId = cv.getAsLong(HabitColumns.ROUTINE_ID);
            return new Uri[] { fromRoutine(routineId), Routines.withId(routineId) };
        }

        @NotifyUpdate(paths = "habits/#")
        public static Uri[] onUpdate(Context context, Uri uri, String where, String[] args) {
            long habitId = Long.valueOf(uri.getLastPathSegment());
            Cursor c = context.getContentResolver().query(uri, null, null, null, null);
            c.moveToFirst();
            long routineId = c.getLong(c.getColumnIndex(HabitColumns.ROUTINE_ID));
            c.close();

            return new Uri[] { withId(habitId), fromRoutine(routineId), Routines.withId(routineId) };
        }
    }

    @TableEndpoint(table = Tables.HABIT_EVENTS)
    public static class HabitEvents {

        @ContentUri(path = "habitEvents", type = "vnd.android.cursor.dir/habitEvent")
        public static final Uri CONTENT_URI = Uri.parse(BASE_URI + "/habitEvents");

        @InexactContentUri(
                path = "habitEvents/#",
                name = "HABIT_EVENT_ID",
                type = "vnd.android.cursor.item/habitEvent",
                whereColumn = HabitEventColumns.ID,
                pathSegment = 1)
        public static Uri withId(long id) {
            return Uri.parse(BASE_URI + "habitEvents/" + String.valueOf(id));
        }

        @InexactContentUri(
                path = "habitEvents/fromHabit/#",
                name = "HABIT_EVENTS_FROM_HABIT",
                type = "vnd.android.cursor.dir/habitEvent",
                whereColumn = HabitEventColumns.HABIT_ID,
                pathSegment = 2)
        public static Uri fromHabit(long habitId) {
            return Uri.parse(BASE_URI + "habitEvents/fromHabit/" + String.valueOf(habitId));
        }

        @NotifyDelete(paths = "habitEvents/#")
        public static Uri[] onDelete(Context context, Uri uri) {
            long habitEventId = Long.valueOf(uri.getLastPathSegment());
            Cursor c = context.getContentResolver().query(uri, null, null, null, null);
            c.moveToFirst();
            long habitId = c.getLong(c.getColumnIndex(HabitEventColumns.HABIT_ID));
            c.close();

            return new Uri[] { withId(habitEventId), fromHabit(habitId), Habits.withId(habitId) };
        }

        @NotifyInsert(paths = "habitEvents")
        public static Uri[] onInsert(ContentValues cv) {
            long habitId = cv.getAsLong(HabitEventColumns.HABIT_ID);
            return new Uri[] { fromHabit(habitId), Habits.withId(habitId) };
        }

        @NotifyUpdate(paths = "habitEvents/#")
        public static Uri[] onUpdate(Context context, Uri uri, String where, String[] args) {
            long habitEventId = Long.valueOf(uri.getLastPathSegment());
            Cursor c = context.getContentResolver().query(uri, null, null, null, null);
            c.moveToFirst();
            long habitId = c.getLong(c.getColumnIndex(HabitEventColumns.HABIT_ID));
            c.close();

            return new Uri[] { withId(habitEventId), fromHabit(habitId), Habits.withId(habitId) };
        }
    }
}
