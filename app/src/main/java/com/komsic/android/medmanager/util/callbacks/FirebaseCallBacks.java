package com.komsic.android.medmanager.util.callbacks;

import com.google.firebase.database.DataSnapshot;

/**
 * Created by komsic on 4/2/2018.
 */

public interface FirebaseCallBacks {
    void onNedMed(DataSnapshot dataSnapshot);

    void onNewAlarm(DataSnapshot dataSnapshot);
}
