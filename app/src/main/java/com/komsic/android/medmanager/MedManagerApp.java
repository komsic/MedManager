package com.komsic.android.medmanager;

import android.app.Application;
import android.util.Log;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by komsic on 4/2/2018.
 *
 */

public class MedManagerApp extends Application{
    final String TAG = "MedManagerApp";

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG, "onCreate: starting");

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
