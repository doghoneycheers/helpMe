package com.example.q.helpme;

import android.app.Application;
import android.content.Context;


public class helpMe extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        helpMe.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return helpMe.context;
    }
}