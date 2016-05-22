package com.example.mfritz.resethabits.data;

import net.simonvt.schematic.annotation.AutoIncrement;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;

import static net.simonvt.schematic.annotation.DataType.Type.INTEGER;
import static net.simonvt.schematic.annotation.DataType.Type.TEXT;

/**
 * Created by matt on 5/18/16.
 */
public interface RoutineColumns {
    @DataType(INTEGER) @PrimaryKey @AutoIncrement String ID = "_id";
    @DataType(TEXT) @NotNull String NAME = "name";

    String HABIT_COUNT = "habit_count";
    String COMPLETE_HABIT_COUNT = "complete_habit_count";
}
