package com.example.android.booklister;

import androidx.appcompat.app.AppCompatActivity;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.opengl.Visibility;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener , LoaderManager.LoaderCallbacks<List<Book>> {

    public static final String LOG_TAG = MainActivity.class.getName();

    /** URL for Book data from the Google Books dataset */
    private String REQUEST_URL =
            "https://www.googleapis.com/books/v1/volumes?q=";

    // String to store the Search Keyword
    private String SEARCH_KEYWORD;

    // String to Store spinner text
    private String spinnerText;

    // integer to store spinner item position
    private int i;

    /** Adapter for the list of earthquakes */
    private BookAdapter mAdapter;

    /** TextView that is displayed when the list is empty */
    private TextView mEmptyStateTextView;

    /** View to display loading indicator. */
    private View loadingIndicator;

    /** for displaying spinner */
    Spinner spinner;

    /** for displaying listview */
    ListView listView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find a reference to the {@link ListView} in the layout
        listView = findViewById(R.id.listView);
        listView.setVisibility(View.INVISIBLE);

        final EditText editText = findViewById(R.id.searchText);

        //Code for setting empty textview
        mEmptyStateTextView = (TextView) findViewById(R.id.empty_textview);

        mEmptyStateTextView.setVisibility(View.GONE);

        // loadingIndicator
        loadingIndicator = findViewById(R.id.loading_indicator);


        // Code for setting Spinner
        spinner = findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this , R.array.numbers
                ,android.R.layout.simple_spinner_item );
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(this);

        // Code for submit button on click listener
        final Button SubmitButton = findViewById(R.id.submit_button);
        SubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setVisibility(View.GONE);
                spinner.setVisibility(View.GONE);
                SubmitButton.setVisibility(View.GONE);
                SEARCH_KEYWORD = editText.getText().toString();
                int flag = 0;
                for (int i=0 ; i< SEARCH_KEYWORD.length() ; i++){

                    if(SEARCH_KEYWORD.charAt(i)== ' '){
                        flag = 1;
                    }
                }
                if(flag==1){
                     SEARCH_KEYWORD.replaceAll(" " , "%20");
                }

                if (i==0){
                    REQUEST_URL = REQUEST_URL + SEARCH_KEYWORD;
                }
                else if (i == 1){
                    REQUEST_URL = REQUEST_URL + "intitle:" + SEARCH_KEYWORD;
                }
                else if (i == 2){
                    REQUEST_URL = REQUEST_URL + "inauthor:" + SEARCH_KEYWORD;
                }
                else if (i == 3) {
                    REQUEST_URL = REQUEST_URL + "inpublisher:" + SEARCH_KEYWORD;
                }
                Log.v("MainActivity" , REQUEST_URL);
                Toast.makeText(MainActivity.this , REQUEST_URL, Toast.LENGTH_SHORT).show();
                getLoaderManager().restartLoader(BOOK_LOADER_ID, null, MainActivity.this);
                listView.setVisibility(View.VISIBLE);
                listView.setEmptyView(mEmptyStateTextView);
                mEmptyStateTextView.setVisibility(View.VISIBLE);
                loadingIndicator.setVisibility(View.VISIBLE);
            }
        });

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String editTextString = editText.getText().toString().trim();
                SubmitButton.setEnabled(!editTextString.isEmpty());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        Button backButton = findViewById(R.id.new_search);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setVisibility(View.VISIBLE);
                spinner.setVisibility(View.VISIBLE);
                SubmitButton.setVisibility(View.VISIBLE);
                REQUEST_URL = "https://www.googleapis.com/books/v1/volumes?q=";
                mAdapter.clear();
                mEmptyStateTextView.setVisibility(View.GONE);
                listView.setVisibility(View.INVISIBLE);
            }

        });

        // Create a new adapter that takes an empty list of books as input
        mAdapter = new BookAdapter(this, new ArrayList<Book>());

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        listView.setAdapter(mAdapter);

        //code for intent
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Book currentBook =mAdapter.getItem(position);
                Uri bookUri = Uri.parse(currentBook.getUrl());
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW , bookUri);
                startActivity(websiteIntent);
            }
        });


        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) { //NULL POINTER SHOULD COME FIRST ELSE THE APP WILL CRASH WITH AN NULL POINTER EXCEPTION
            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();

            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader(BOOK_LOADER_ID, null, this);
        }
        else {
            // Otherwise, display error
            // First, hide loading indicator so error message will be visible
            loadingIndicator.setVisibility(View.GONE);

            //disable back button
            backButton.setEnabled(false);
            SubmitButton.setEnabled(false);

            // Update empty state with no connection error message
            mEmptyStateTextView.setVisibility(View.VISIBLE);
            mEmptyStateTextView.setText(R.string.no_internet_connection);
            TextView checkInternetTextView = findViewById(R.id.check_internet_textview);
            checkInternetTextView.setText(R.string.please_check_connection);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        spinnerText = parent.getItemAtPosition(position).toString();
        Toast.makeText(parent.getContext(), spinnerText, Toast.LENGTH_SHORT).show();
        i = parent.getSelectedItemPosition();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    /**
     * Constant value for the earthquake loader ID. We can choose any integer.
     * This really only comes into play if you're using multiple loaders.
     */
    private static final int BOOK_LOADER_ID = 1;

    @Override
    public Loader<List<Book>> onCreateLoader(int i, Bundle bundle) {

        Log.v("MainActivity" , "onCreateLoader kaam kar raha hai");
        // Create a new loader for the given URL
        return new BookLoader(this , REQUEST_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<Book>> loader, List<Book> books) {

        Log.v("MainActivity" , "onLoadFinished kaam kar raha hai");

        // Hide loading indicator because the data has been loaded
        loadingIndicator.setVisibility(View.GONE);

        mEmptyStateTextView.setText(R.string.no_result);

        // Clear the adapter of previous earthquake data
        mAdapter.clear();

        // If there is a valid list of {@link Earthquake}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (books != null && !books.isEmpty()) {
            mAdapter.addAll(books);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Book>> loader) {

        Log.v("EarthquakeActivity" , "onLoaderReset kaam kar raha hai");
        // Loader reset, so we can clear out our existing data.
        mAdapter.clear();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        EditText editText = findViewById(R.id.searchText);
        editText.setVisibility(View.GONE);
        spinner = findViewById(R.id.spinner);
        spinner.setVisibility(View.GONE);
        Button SubmitButton = findViewById(R.id.submit_button);
        SubmitButton.setVisibility(View.GONE);
    }
}
