package com.example.mfritz.resethabits.data;

import android.net.Uri;

import com.example.mfritz.resethabits.data.HabitsDatabase.Tables;

import net.simonvt.schematic.annotation.ContentProvider;
import net.simonvt.schematic.annotation.ContentUri;
import net.simonvt.schematic.annotation.InexactContentUri;
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

        // TODO: need to add @Notify annotations for when data changes
    }
}
