package com.example.android.booklister;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.util.List;

import androidx.annotation.Nullable;


public class BookLoader extends AsyncTaskLoader<List<Book>> {

    /** Tag for log messages */
    private static final String LOG_TAG = BookLoader.class.getName();

    /** Query URL */
    private String mUrl;

    /**
     * Constructs a new {@link BookLoader}.
     *
     * @param context of the activity
     * @param url to load data from
     */
    public BookLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        Log.v("EarthquakeLoader" , "onStartLoading kaam kar raha hai");
        forceLoad();
    }

    /**
     * This is on a background thread.
     */
    @Nullable
    @Override
    public List<Book> loadInBackground() {

        Log.v("BookLoader" , "loadInBackground kaam kar raha hai");
        if (mUrl == null) {
            return null;
        }

        // Perform the network request, parse the response, and extract a list of earthquakes.
        List<Book> books = QueryUtils.fetchBookData(mUrl);
        return books;
    }
}
