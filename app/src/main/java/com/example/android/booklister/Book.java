package com.example.android.booklister;

public class Book {

    //will store book title
    private String mTitle;

    //will store author name
    private String mAuthor;

    //will store publisher name
    private String mPublisher;

    //will store description of the book
    private String mDescription;

    //will store url of the book
    private String mUrl;

    public Book(String Title , String Publisher , String Description , String Url , String Author ){

        mTitle = Title;
       mAuthor = Author;
        mPublisher = Publisher;
        mDescription = Description;
        mUrl = Url;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public String getPublisher() {
        return mPublisher;
    }

    public String getDescription() {
        return mDescription;
    }

    public String getUrl() {
        return mUrl;
    }
}
