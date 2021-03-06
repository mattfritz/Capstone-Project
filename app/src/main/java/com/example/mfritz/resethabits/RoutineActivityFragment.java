package com.example.mfritz.resethabits;

import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.mfritz.resethabits.data.HabitsProvider;
import com.example.mfritz.resethabits.data.HabitsProvider.Routines;
import com.example.mfritz.resethabits.data.RoutineColumns;
import com.example.mfritz.resethabits.data.RoutinesAdapter;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;
import butterknife.Unbinder;

public class RoutineActivityFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private final String LOG_TAG = this.getClass().getSimpleName();
    private Unbinder mButterKnife;
    private Tracker mTracker;
    public RoutinesAdapter mAdapter;

    @BindView(R.id.listview_routine) ListView listView;
    @BindView(R.id.main_adview) AdView adView;
    @BindView(R.id.empty_listview) TextView emptyListView;
    @BindView(R.id.textview_quote) TextView quote;

    public RoutineActivityFragment() { }

    public interface OnRoutineSelectedListener {
        void onRoutineSelected(Uri contentUri);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (mAdapter != null) {
            listView.setEmptyView(emptyListView);
            listView.setAdapter(mAdapter);
        }

        registerForContextMenu(listView);
        getLoaderManager().initLoader(0, null, this);

        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ResetHabits app = (ResetHabits) getActivity().getApplication();
        mTracker = app.getDefaultTracker();
        mTracker.setScreenName(LOG_TAG);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());

        new FetchQuoteTask().execute();
    }

    @OnItemClick(R.id.listview_routine)
    public void onRoutineSelected(int position, long routineId) {
        Cursor c = (Cursor) mAdapter.getItem(position);
        if (c != null) {
            ((OnRoutineSelectedListener) getActivity())
                    .onRoutineSelected(HabitsProvider.Habits.fromRoutine(routineId));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_routine, container, false);
        mButterKnife = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mButterKnife.unbind();
    }

    public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
        return new CursorLoader(getActivity(), Routines.CONTENT_URI, RoutinesAdapter.PROJECTION, null, null, null);
    }

    public void onLoaderReset(Loader<Cursor> loader) {
        if (mAdapter != null) {
            mAdapter.swapCursor(null);
        }
    }

    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (mAdapter == null) {
            mAdapter = new RoutinesAdapter(getContext(), data);
            listView.setEmptyView(emptyListView);
            listView.setAdapter(mAdapter);
        } else {
            mAdapter.swapCursor(data);
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if(v.getId() == R.id.listview_routine) {
            menu.setHeaderTitle(R.string.delete_routine);
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
                int idIndex = c.getColumnIndex(RoutineColumns.ID);
                long routineId = c.getLong(idIndex);
                getActivity().getContentResolver().delete(Routines.withId(routineId), null, null);

                mTracker.send(new HitBuilders.EventBuilder()
                    .setCategory("Action")
                    .setAction("DeleteRoutine")
                    .build());
            }
        }
        return true;
    }

    public class FetchQuoteTask extends AsyncTask<Void, Void, String> {
        private final String LOG_TAG = this.getClass().getSimpleName();
        private final OkHttpClient client = new OkHttpClient();

        @Override
        protected String doInBackground(Void... params) {
            Request request = new Request.Builder()
                    .url("http://quotes.rest/qod.json?category=inspire")
                    .build();

            try {
                Response response = client.newCall(request).execute();
                if (!response.isSuccessful()) {
                    throw new IOException("Unsuccessful request: " + response);
                }

                String body = response.body().string();
                JSONObject responseJson = new JSONObject(body);

                JSONObject contents = responseJson.getJSONObject("contents");
                JSONArray quotes = contents.getJSONArray("quotes");
                String quote = quotes.getJSONObject(0).getString("quote");

                Log.d(LOG_TAG, "Quote: " + quote);
                return quote;
            } catch (Exception e) {
                Log.e(LOG_TAG, "Error fetching quote: " + e.getMessage());
                return "";
            }
        }

        @Override
        protected void onPostExecute(String s) {
            quote.setText(s);
        }
    }
}
