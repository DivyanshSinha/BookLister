package com.example.android.booklister;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

import androidx.annotation.NonNull;

public class BookAdapter extends ArrayAdapter<Book> {


    public BookAdapter(Activity context, ArrayList<Book> Book) {
        super(context, 0, Book);
    }

    /**
     * Provides a view for an AdapterView (ListView, GridView, etc.)
     *
     * @param position The position in the list of data that should be displayed in the
     *                 list item view.
     * @param convertView The recycled view to populate.
     * @param parent The parent ViewGroup that is used for inflation.
     * @return The View for the position in the AdapterView.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_items, parent, false);
        }

        // Get the {@link AndroidFlavor} object located at this position in the list
        Book currentBook = getItem(position);

        //TextView to display title of the book
        TextView titleTextView = listItemView.findViewById(R.id.title_view);
        titleTextView.setText(currentBook.getTitle());

        //TextView to display author of the book
        TextView authorTextView = listItemView.findViewById(R.id.author_view);
        authorTextView.setText(currentBook.getAuthor());

        //TextView to display publisher
        TextView publisherTextView = listItemView.findViewById(R.id.publisher_view);
        publisherTextView.setText(currentBook.getPublisher());

        //TextView to display description
        TextView descriptionTextView = listItemView.findViewById(R.id.description_view);
        descriptionTextView.setText(currentBook.getDescription());

        // Return the whole list item layout (containing 4 TextViews)
        // so that it can be shown in the ListView
        return listItemView;
    }

}
