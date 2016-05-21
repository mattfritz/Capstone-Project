package com.example.mfritz.resethabits;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.mfritz.resethabits.data.HabitsProvider;
import com.example.mfritz.resethabits.data.HabitsProvider.Routines;
import com.example.mfritz.resethabits.data.RoutineColumns;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;

public class RoutineActivityFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private final String LOG_TAG = this.getClass().getSimpleName();
    public SimpleCursorAdapter mAdapter;

    @BindView(R.id.listview_routine) ListView listView;

    public RoutineActivityFragment() { }

    public interface OnRoutineSelectedListener {
        void onRoutineSelected(Uri contentUri);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // TODO: set empty view here eventually
        listView.setAdapter(mAdapter);
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // TODO: investigate whether SimpleCursorAdapter is sufficient
        mAdapter = new SimpleCursorAdapter(getActivity(),
                android.R.layout.simple_list_item_2,
                null,
                new String[]{RoutineColumns.NAME},
                new int[]{android.R.id.text1}, 0);
    }

    @OnItemClick(R.id.listview_routine)
    void onRoutineSelected(int position, long routineId) {
        Cursor c = (Cursor) mAdapter.getItem(position);
        if (c != null) {
            ((OnRoutineSelectedListener) getActivity())
                    .onRoutineSelected(HabitsProvider.Habits.fromRoutine(routineId));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_routine, container, false);
        // TODO: unbind butterknife on destroy
        ButterKnife.bind(this, view);
        return view;
    }

    public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
        return new CursorLoader(getActivity(), Routines.CONTENT_URI, null, null, null, null);
    }

    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }

    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
    }
}
