package com.komsic.android.medmanager.util;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.komsic.android.medmanager.util.callbacks.FirebaseCallBacks;

/**
 * Created by komsic on 4/2/2018.
 */

public class FirebaseManager implements ChildEventListener, ValueEventListener {
    private volatile static FirebaseManager sFirebaseManager;
    private DatabaseReference mReference;
    private FirebaseCallBacks mCallBacks;

    private FirebaseManager(String ref, FirebaseCallBacks callBacks){
        mReference = FirebaseDatabase.getInstance().getReference().child(ref);
        this.mCallBacks = callBacks;
    }

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {

    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

    }

    //value
    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }

    public static synchronized FirebaseManager getInstance(String ref, FirebaseCallBacks callBacks) {
        if (sFirebaseManager != null) {
            synchronized (FirebaseManager.class) {
                sFirebaseManager = new FirebaseManager(ref, callBacks);
            }
        }
        return sFirebaseManager;
    }

    public void addChildEventListener() {
        mReference.addChildEventListener(this);
    }

    public void addListenerForSingleValueEvent() {
        mReference.addListenerForSingleValueEvent(this);
    }

    public void removeListeners(){
        mReference.removeEventListener((ChildEventListener) this);
        mReference.removeEventListener((ValueEventListener) this);
    }

    public void onDetached() {
        mReference = null;
        sFirebaseManager = null;
        mCallBacks = null;
    }
}
