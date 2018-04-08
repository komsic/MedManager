package com.komsic.android.medmanager.data;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.komsic.android.medmanager.data.model.Med;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by komsic on 4/2/2018.
 * This clase is reponsible for storing data
 */

public class DataManager implements ValueEventListener, ChildEventListener {
    public static final int VALUE_EVENT_LISTENER = 1;
    public static final int CHILD_EVENT_LISTENER = 2;

    public static String[] daysOfTheWeek = {"sun", "mon", "tue", "wed", "thu", "fri", "sat"};

    private static DataManager sDataManager;

    private Map<String, Boolean> mDayStateMap;

    private Med mMed;

    private DatabaseReference mDatabaseReference;
    private DatabaseReference mChildDatabaseReference;

    private MedEventListener mMedEventListener;
    private MedEventListener scheduleMedEventListener;


    public static DataManager getInstance() {
        if (sDataManager == null) {
            sDataManager = new DataManager();
        }
        return sDataManager;
    }

    private DataManager() {
        mDayStateMap = new HashMap<>();

        for (String dayState : daysOfTheWeek) {
            mDayStateMap.put(dayState, true);
        }

        mMed = new  Med();
        mChildDatabaseReference = FirebaseDatabase.getInstance().getReference().child("medList");
        mChildDatabaseReference.addChildEventListener(this);
    }

    public Map<String, Boolean> getDayStateMap() {
        return mDayStateMap;
    }

    public void setDayStateMap(Map<String, Boolean> dayStateMap) {
        mDayStateMap = dayStateMap;
    }

    public Med getMed() {
        return mMed;
    }

    public void setMed(Med med) {
        mMed = med;

    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        if (dataSnapshot != null) {
            mMed = dataSnapshot.getValue(Med.class);
            mMedEventListener.onMedAdded();
        }
    }

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        if (dataSnapshot != null) {
            mMed = dataSnapshot.getValue(Med.class);
            mMedEventListener.onMedAdded();
            scheduleMedEventListener.onMedAdded();
        }
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

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }

    public void setMedEventList(MedEventListener medEventListener) {
        mMedEventListener = medEventListener;
    }

    public void setMedEventSchedule(MedEventListener medEventListener) {
        scheduleMedEventListener = medEventListener;
    }

    public void addListenerForSingleValueEvent(String s, MedEventListener medEventListener) {
        mMedEventListener = medEventListener;
        mDatabaseReference =FirebaseDatabase.getInstance().getReference().child("medList/" + s);
        mDatabaseReference.addListenerForSingleValueEvent(this);
    }

    public void removeListener(int whichListener) {
        switch (whichListener) {
            case VALUE_EVENT_LISTENER:
                mDatabaseReference.removeEventListener((ValueEventListener) this);
                break;
            case CHILD_EVENT_LISTENER:
                mChildDatabaseReference.removeEventListener((ChildEventListener) this);
        }
    }

    public void updateChildren(String s, Med med) {
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child(s);
        mDatabaseReference.updateChildren(med.toMap());
    }

    public interface MedEventListener{
        void onMedAdded();
    }
}
