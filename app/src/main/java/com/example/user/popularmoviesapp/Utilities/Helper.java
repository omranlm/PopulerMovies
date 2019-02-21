package com.example.user.popularmoviesapp.Utilities;

import android.net.Uri;

import java.net.URI;
import java.net.URISyntaxException;

public class Helper {

    private static String URI_STRING = "content://com.example.user.popularmoviesapp/favorite";
    public static final Uri CONENT_URI()
    {
        Uri favorit = null;

       favorit = new Uri.Builder().build();
        return favorit;
    }


}
