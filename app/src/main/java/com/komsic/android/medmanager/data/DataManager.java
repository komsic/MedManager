package com.komsic.android.medmanager.data;

import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.komsic.android.medmanager.data.model.Alarm;
import com.komsic.android.medmanager.data.model.Med;
import com.komsic.android.medmanager.data.model.Reminder;
import com.komsic.android.medmanager.util.CalendarUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static android.R.attr.data;

/**
 * Created by komsic on 4/2/2018.
 * This class is responsible for storing data
 */

public class DataManager implements ValueEventListener, ChildEventListener {
    private static final String TAG = "DataManager";

    public static final int VALUE_EVENT_LISTENER = 1;
    public static final int CHILD_EVENT_LISTENER = 2;

    public static String[] daysOfTheWeek = {"sun", "mon", "tue", "wed", "thu", "fri", "sat"};

    private static DataManager sDataManager;

    private Map<String, Boolean> mDayStateMap;

    private List<Med> mMedList;
    private Set<Alarm> mAlarmList;
    private int mAlarmListSize;

    private Med mMed;

    private DatabaseReference mDatabaseReference;
    private DatabaseReference mChildDatabaseReference;

    private MedEventListener mMedEventListener;
    private MedEventListener scheduleMedEventListener;
    private AlarmEventListener mAlarmEventListener;


    public static DataManager getInstance() {
        if (sDataManager == null) {
            sDataManager = new DataManager();
        }
        return sDataManager;
    }

    private DataManager() {
        mMedList = new ArrayList<>();
        mAlarmList = new HashSet<>();
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
            Med med = dataSnapshot.getValue(Med.class);
            mMedList.add(med);
            mMedEventListener.onMedAdded();
            scheduleMedEventListener.onMedAdded();
        }
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

    public void setAlarmEventListener(AlarmEventListener alarmEventListener) {
        mAlarmEventListener = alarmEventListener;
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

    public List<Med> getMedList() {
        return mMedList;
    }

    public List<Map<Reminder, Set<String>>> processSchedule(long selectedDate) {
        Map<Reminder, Set<String>> data = new HashMap<>();

        for (Med med : mMedList) {
            for (Reminder reminder : med.reminders) {
                if (selectedDate >= med.startDate && selectedDate <= med.endDate) {
                    if (reminder.getDateDayState(selectedDate)) {
                        if (!data.containsKey(reminder)) {
                            data.put(reminder, new HashSet<String>());
                        }
                        data.get(reminder).add(med.name);
                    }
                }
            }
        }

        List<Map<Reminder, Set<String>>> dataList = new ArrayList<>();
        List<Reminder> sortedKeySet = new ArrayList<>(data.keySet());
        Collections.sort(sortedKeySet);
        dataList.clear();
        for (Reminder reminder : sortedKeySet) {
            Map<Reminder, Set<String>> dataMap = new HashMap<>();
            dataMap.put(reminder, data.get(reminder));
            dataList.add(dataMap);
            Alarm newAlarm = new Alarm(reminder.getTimeOfDay(), dataMap.get(reminder));
            processAlarm(selectedDate, newAlarm);
        }

        if (mAlarmEventListener != null) {
            Log.e(TAG, "processSchedule: " );
            mAlarmEventListener.onAlarmChanged();
        }

        return dataList;
    }

    public void processAlarm(long selectedDate, Alarm alarm) {
        long currentTime = CalendarUtil.getCurrentTime();
        if (CalendarUtil.getDateInString(currentTime)
                .equals(CalendarUtil.getDateInString(selectedDate))) {
            if (mAlarmList != null) {
                mAlarmList.add(alarm);
            }
        }
    }

    public Set<Alarm> getAlarmList() {
        return mAlarmList;
    }

    public interface MedEventListener{
        void onMedAdded();
    }

    public interface AlarmEventListener{
        void onAlarmChanged();
    }
}
