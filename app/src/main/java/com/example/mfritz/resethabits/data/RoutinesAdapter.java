package com.example.mfritz.resethabits.data;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.mfritz.resethabits.R;
import com.example.mfritz.resethabits.data.HabitsDatabase.Tables;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by matt on 5/22/16.
 */
public class RoutinesAdapter extends CursorAdapter {
    private final String LOG_TAG = this.getClass().getSimpleName();


    public static String[] PROJECTION = new String[] {
        Tables.ROUTINES + "." + RoutineColumns.ID,
        RoutineColumns.NAME,
        RoutineColumns.HABIT_COUNT,
        RoutineColumns.COMPLETE_HABIT_COUNT
    };

    public RoutinesAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_routine, parent, false);
        view.setTag(new ViewHolder(view));
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder holder = (ViewHolder) view.getTag();

        int nameIndex = cursor.getColumnIndex(RoutineColumns.NAME);
        String name = cursor.getString(nameIndex);
        holder.name.setText(name);

        int countIndex = cursor.getColumnIndex(RoutineColumns.HABIT_COUNT);
        String total = Integer.toString(cursor.getInt(countIndex));
        int completeIndex = cursor.getColumnIndex(RoutineColumns.COMPLETE_HABIT_COUNT);
        String totalComplete = Integer.toString(cursor.getInt(completeIndex));
        holder.habits.setText(totalComplete + "/" + total);
    }

    static class ViewHolder {
        @BindView(R.id.textview_habits_routine_list) TextView habits;
        @BindView(R.id.textview_name_routine_list) TextView name;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
