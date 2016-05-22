package com.example.mfritz.resethabits;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.CursorAdapter;

import com.example.mfritz.resethabits.data.HabitColumns;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by matt on 5/22/16.
 */
public class HabitsAdapter extends CursorAdapter {
    private final String LOG_TAG = this.getClass().getSimpleName();

    public HabitsAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_habit, parent, false);
        view.setTag(new ViewHolder(view));
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder holder = (ViewHolder) view.getTag();

        int nameIndex = cursor.getColumnIndex(HabitColumns.NAME);
        String name = cursor.getString(nameIndex);
        holder.item.setText(name);

        int completedIndex = cursor.getColumnIndex(HabitColumns.COMPLETE_TODAY);
        int completed = cursor.getInt(completedIndex);
        boolean isChecked = completed == 1;
        holder.item.setChecked(isChecked);
    }

    static class ViewHolder {
        @BindView(R.id.checkedtextview_habits_list) CheckedTextView item;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
