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

import com.example.mfritz.resethabits.data.HabitColumns;

import butterknife.BindView;
import butterknife.ButterKnife;

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
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mHabitsUri = getActivity().getIntent().getData();

        // TODO: investigate whether SimpleCursorAdapter is sufficient
        mAdapter = new SimpleCursorAdapter(getActivity(),
                android.R.layout.simple_list_item_2,
                null,
                new String[]{HabitColumns.NAME},
                new int[]{android.R.id.text1}, 0);
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
}
