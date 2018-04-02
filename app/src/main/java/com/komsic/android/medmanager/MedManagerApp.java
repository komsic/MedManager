package com.komsic.android.medmanager;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by komsic on 4/2/2018.
 *
 */

public class MedManagerApp extends Application{
    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
