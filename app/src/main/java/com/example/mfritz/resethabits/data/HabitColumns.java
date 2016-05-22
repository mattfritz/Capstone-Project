package com.example.mfritz.resethabits.data;

import com.example.mfritz.resethabits.data.HabitsDatabase.Tables;

import net.simonvt.schematic.annotation.AutoIncrement;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;
import net.simonvt.schematic.annotation.References;

import static net.simonvt.schematic.annotation.DataType.Type.INTEGER;
import static net.simonvt.schematic.annotation.DataType.Type.TEXT;

/**
 * Created by matt on 5/18/16.
 */
public interface HabitColumns {
    @DataType(INTEGER) @PrimaryKey @AutoIncrement String ID = "_id";
    @DataType(TEXT) @NotNull String NAME = "name";
    @DataType(TEXT) String DESCRIPTION = "description";
    @DataType(INTEGER) String COMPLETE_TODAY = "complete_today";

    @DataType(INTEGER) @References(table = Tables.ROUTINES, column = RoutineColumns.ID)
    String ROUTINE_ID = "routineId";

}
