package com.example.mfritz.resethabits;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckedTextView;
import android.widget.ListView;

import com.example.mfritz.resethabits.data.HabitColumns;
import com.example.mfritz.resethabits.data.HabitEventColumns;
import com.example.mfritz.resethabits.data.HabitsProvider;
import com.example.mfritz.resethabits.data.HabitsProvider.HabitEvents;
import com.example.mfritz.resethabits.data.HabitsProvider.Habits;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;
import butterknife.Unbinder;

/**
 * A placeholder fragment containing a simple view.
 */
public class HabitActivityFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private final String LOG_TAG = this.getClass().getSimpleName();
    private Uri mHabitsUri;
    private Unbinder mButterKnife;
    private Tracker mTracker;

    @BindView(R.id.listview_habit) ListView listView;

    public HabitsAdapter mAdapter;

    public HabitActivityFragment() {
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // TODO: set empty view here eventually

        if (mAdapter != null) {
            listView.setAdapter(mAdapter);
        }
        registerForContextMenu(listView);
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mHabitsUri = getActivity().getIntent().getData();

        ResetHabits app = (ResetHabits) getActivity().getApplication();
        mTracker = app.getDefaultTracker();
        mTracker.setScreenName(LOG_TAG);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    @OnItemClick(R.id.listview_habit)
    void onCheckboxClicked(View view, int position, long habitId) {
        CheckedTextView check = (CheckedTextView) view.findViewById(R.id.checkedtextview_habits_list);
        ContentResolver cr = getActivity().getContentResolver();
        if (check.isChecked()) {
            cr.delete(HabitEvents.fromHabit(habitId), null, null);
            check.setChecked(false);

            mTracker.send(new HitBuilders.EventBuilder()
                    .setCategory("Action")
                    .setAction("UnCompleteHabit")
                    .build());
        } else {
            ContentValues cv = new ContentValues();
            cv.put(HabitEventColumns.HABIT_ID, habitId);
            cv.put(HabitEventColumns.COMPLETE, 1);
            cv.put(HabitEventColumns.DATE, HabitsProvider.getDay());
            cr.insert(HabitEvents.CONTENT_URI, cv);
            check.setChecked(true);

            mTracker.send(new HitBuilders.EventBuilder()
                    .setCategory("Action")
                    .setAction("CompleteHabit")
                    .build());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_habit, container, false);
        mButterKnife = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mButterKnife != null) {
            mButterKnife.unbind();
        }
    }

    public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
        return new CursorLoader(getActivity(), mHabitsUri, HabitsAdapter.PROJECTION, null, null, null);
    }

    public void onLoaderReset(Loader<Cursor> loader) {
        if (mAdapter != null) {
            mAdapter.swapCursor(null);
        }
    }

    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (mAdapter == null) {
            mAdapter = new HabitsAdapter(getContext(), data);
            listView.setAdapter(mAdapter);
        } else {
            mAdapter.swapCursor(data);
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if(v.getId() == R.id.listview_habit) {
            menu.setHeaderTitle(R.string.delete_habit);
            menu.add(Menu.NONE, 0, 0, R.string.delete_text);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getTitle().equals("Delete")) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            Cursor c = (Cursor) mAdapter.getItem(info.position);
            if (c != null) {
                c.moveToFirst();
                int idIndex = c.getColumnIndex(HabitColumns.ID);
                long routineId = c.getLong(idIndex);
                getActivity().getContentResolver().delete(Habits.withId(routineId), null, null);

                mTracker.send(new HitBuilders.EventBuilder()
                        .setCategory("Action")
                        .setAction("DeleteHabit")
                        .build());
            }
        }
        return true;
    }
}
