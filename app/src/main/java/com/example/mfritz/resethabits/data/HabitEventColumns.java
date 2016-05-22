package com.example.mfritz.resethabits.data;

import net.simonvt.schematic.annotation.AutoIncrement;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.DefaultValue;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;
import net.simonvt.schematic.annotation.References;

import static com.example.mfritz.resethabits.data.HabitsDatabase.Tables;
import static net.simonvt.schematic.annotation.DataType.Type.INTEGER;
import static net.simonvt.schematic.annotation.DataType.Type.TEXT;

/**
 * Created by matt on 5/21/16.
 */
public interface HabitEventColumns {
    @DataType(INTEGER) @PrimaryKey @AutoIncrement String ID = "_id";
    @DataType(INTEGER) @NotNull @DefaultValue("0") String COMPLETE = "complete";
    @DataType(TEXT) @NotNull String DATE = "date";

    @DataType(INTEGER) @References(table = Tables.HABITS, column = HabitColumns.ID)
    String HABIT_ID = "habit_id";
}
