package com.example.mfritz.resethabits;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.mfritz.resethabits.data.HabitColumns;
import com.example.mfritz.resethabits.data.HabitsProvider.Habits;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;

/**
 * A placeholder fragment containing a simple view.
 */
public class HabitActivityFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private final String LOG_TAG = this.getClass().getSimpleName();
    private Uri mHabitsUri;

    @BindView(R.id.listview_habit) ListView listView;

    public SimpleCursorAdapter mAdapter;

    public HabitActivityFragment() {
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // TODO: set empty view here eventually
        listView.setAdapter(mAdapter);
        registerForContextMenu(listView);
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mHabitsUri = getActivity().getIntent().getData();

        // TODO: investigate whether SimpleCursorAdapter is sufficient
        mAdapter = new SimpleCursorAdapter(getActivity(),
                android.R.layout.simple_list_item_checked,
                null,
                new String[]{HabitColumns.NAME},
                new int[]{android.R.id.text1}, 0);
    }

    @OnItemClick(R.id.listview_habit)
    void onRoutineSelected(int position, long habitId) {
        Cursor c = (Cursor) mAdapter.getItem(position);
        if (c != null) {
            showDetailView();
            // Habits.withId(habitId);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_habit, container, false);
        // TODO: unbind butterknife on destroy
        ButterKnife.bind(this, view);
        return view;
    }

    public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
        return new CursorLoader(getActivity(), mHabitsUri, null, null, null, null);
    }

    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }

    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
    }

    private void showDetailView() {
        HabitDialogFragment habitDetail = new HabitDialogFragment();
        getFragmentManager()
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .add(android.R.id.content, habitDetail)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if(v.getId() == R.id.listview_habit) {
            // TODO: move into strings
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
            }
        }
        return true;
    }
}
