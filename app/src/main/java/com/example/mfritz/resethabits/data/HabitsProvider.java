package com.example.mfritz.resethabits.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.example.mfritz.resethabits.data.HabitsDatabase.Tables;

import net.simonvt.schematic.annotation.ContentProvider;
import net.simonvt.schematic.annotation.ContentUri;
import net.simonvt.schematic.annotation.InexactContentUri;
import net.simonvt.schematic.annotation.NotifyDelete;
import net.simonvt.schematic.annotation.NotifyInsert;
import net.simonvt.schematic.annotation.NotifyUpdate;
import net.simonvt.schematic.annotation.TableEndpoint;

/**
 * Created by matt on 5/18/16.
 */

@ContentProvider(authority = HabitsProvider.AUTHORITY,
        database = HabitsDatabase.class,
        packageName = "com.example.mfritz.resethabits.provider")
public class HabitsProvider {

    public static final String AUTHORITY = "com.example.mfritz.resethabits.HabitsProvider";
    private static final String BASE_URI = "content://" + AUTHORITY + "/";

    @TableEndpoint(table = Tables.ROUTINES)
    public static class Routines {

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
    }

    @TableEndpoint(table = Tables.HABITS)
    public static class Habits {

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
}
