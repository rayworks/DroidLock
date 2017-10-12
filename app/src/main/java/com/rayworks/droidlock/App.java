package com.rayworks.droidlock;

import android.app.Application;
import android.content.SharedPreferences;

import timber.log.Timber;

/**
 * Created by Sean on 10/12/17.
 */

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Timber.plant(new Timber.DebugTree());
    }
}
