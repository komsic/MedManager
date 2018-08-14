package com.komsic.android.medmanager.data;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.komsic.android.medmanager.data.model.Alarm;
import com.komsic.android.medmanager.data.model.Med;
import com.komsic.android.medmanager.data.model.Reminder;
import com.komsic.android.medmanager.data.model.User;
import com.komsic.android.medmanager.data.model.alarm.AlarmItem;
import com.komsic.android.medmanager.data.model.alarm.AlarmList;
import com.komsic.android.medmanager.ui.base.BaseActivity;
import com.komsic.android.medmanager.util.CalendarUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by komsic on 4/2/2018.
 * This class is responsible for storing data
 */

public class DataManager implements ValueEventListener, ChildEventListener,
        FirebaseAuth.AuthStateListener {
    private static final String TAG = "DataManager";

    public static final int VALUE_EVENT_LISTENER = 1;
    public static final int CHILD_EVENT_LISTENER = 2;

    public static String[] daysOfTheWeek = {"sun", "mon", "tue", "wed", "thu", "fri", "sat"};

    private static DataManager sDataManager;

    private Map<String, Boolean> mDayStateMap;

    private List<Med> mMedList;
    private List<Alarm> mAlarmList;
    private AlarmItemEvent mAlarmEvent;

    private AlarmItemEvent mServiceAlarmEvent;

    private Med mMed;
    private User mCurrentUser;

    private DatabaseReference mDatabaseReference;
    private DatabaseReference mChildDatabaseReference;

    private MedEventListener mMedEventListener;

    public static DataManager getInstance() {
        if (sDataManager == null) {
            sDataManager = new DataManager();
        }
        return sDataManager;
    }

    private DataManager() {
        mMedList = new ArrayList<>();
        mAlarmList = new ArrayList<>();
        mDayStateMap = new HashMap<>();

        for (String dayState : daysOfTheWeek) {
            mDayStateMap.put(dayState, true);
        }

        mMed = new Med();

        FirebaseAuth.getInstance().addAuthStateListener(this);
    }

    public Map<String, Boolean> getDayStateMap(int reminderPosition) {
        return mMed.getDayStateMap(reminderPosition);
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
        if (dataSnapshot != null && mMedEventListener != null) {
            mMed = dataSnapshot.getValue(Med.class);
            mMedEventListener.onMedAdded();
        }
    }

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        if (dataSnapshot != null && mMedEventListener != null) {
            mMed = dataSnapshot.getValue(Med.class);
            mMedList.add(mMed);
            processAlarm();
            mMedEventListener.onMedAdded();
        }
    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
        if (dataSnapshot != null) {
            Med med = dataSnapshot.getValue(Med.class);

            int updatedMedIndex = mMedList.indexOf(med);
            mMedList.get(updatedMedIndex).update(med);

            if (mAlarmEvent != null) {
                mAlarmEvent.onAlarmListChanged(null);
            }

            processAlarm();
        }
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

    public void addListenerForSingleValueEvent(String s, MedEventListener medEventListener) {
        mMedEventListener = medEventListener;
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("users/"
                + mCurrentUser.getUid() + "/userMedList/" + s);
        mDatabaseReference.addListenerForSingleValueEvent(this);
    }

    public void removeListener(int whichListener) {
        if (sDataManager != null) {
            switch (whichListener) {
                case VALUE_EVENT_LISTENER:
                    mDatabaseReference.removeEventListener((ValueEventListener) this);
                    break;
                case CHILD_EVENT_LISTENER:
                    mChildDatabaseReference.removeEventListener((ChildEventListener) this);
                    break;
            }
        }
    }

    public void updateChildren(String s, Med med) {
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("users/" +
                mCurrentUser.getUid() + "/userMedList/" + s);
        mDatabaseReference.updateChildren(med.toMap());
    }

    public List<Med> getMedList() {
        return mMedList;
    }

    public void storeUser(String fullName, String username) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(username)
                .build();

        if (user != null) {
            user.updateProfile(profileUpdates);

            mCurrentUser = new User(user.getEmail(), fullName, username, user.getUid());

            DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();
            databaseRef.child("users").child(user.getUid()).child("userInfo")
                    .setValue(mCurrentUser);

            setMedListListener();
        }
    }

    private void setMedListListener() {
        if (mCurrentUser != null) {
            mChildDatabaseReference = FirebaseDatabase.getInstance().getReference()
                    .child("users/" + mCurrentUser.getUid() + "/userMedList");
            mChildDatabaseReference.addChildEventListener(this);
        }
    }

    public void createUserWithEmailAndPassword(String email, String password,
                                               OnCompleteListener<AuthResult> completeListener,
                                               BaseActivity activity) {

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity, completeListener);
    }

    public void addMed(Med newMed) {
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();
        newMed.id = databaseRef.child("users/" + mCurrentUser.getUid() + "/userMedList").push()
                .getKey();
        databaseRef.child("users/" + mCurrentUser.getUid() + "/userMedList" + "/" + newMed.id)
                .setValue(newMed);
    }

    public void fetchCurrentUser() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();
        if (user != null) {
            databaseRef.child("users").child(user.getUid()).child("userInfo")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot != null) {
                                mCurrentUser = dataSnapshot.getValue(User.class);
                                setMedListListener();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
        }
    }

    public void signOut() {
        FirebaseAuth.getInstance().signOut();
    }

    public boolean onForgotPasswordClicked(String email) {
        final boolean[] status = new boolean[1];
        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        status[0] = task.isSuccessful();
                    }
                });
        return status[0];
    }

    public void signIn(String email, String password, BaseActivity activity,
                       OnCompleteListener<AuthResult> onCompleteListener) {
        FirebaseAuth.getInstance()
                .signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity, onCompleteListener);
    }

    public boolean isUserSignedIn() {
        return FirebaseAuth.getInstance().getCurrentUser() == null;
    }

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user == null) {
            sDataManager = null;
            firebaseAuth.removeAuthStateListener(this);
        }
    }

    public void clearMedList() {
        mMedList.clear();
    }

    public void processAlarm() {
        mAlarmList = getScheduleListForSelectedDate(-1);

        if (mServiceAlarmEvent != null) {
            mServiceAlarmEvent.onAlarmListChanged(mAlarmList);
        }

        if (mAlarmEvent != null) {
            mAlarmEvent.onAlarmListChanged(mAlarmList);
        }
    }

    public void setServiceAlarmEvent(AlarmItemEvent serviceAlarmEvent) {
        mServiceAlarmEvent = serviceAlarmEvent;
    }

    private List<AlarmItem> extractAlarmFromRem(Med med, Long currentTime) {
        List<AlarmItem> alarms = new ArrayList<>();

        if (currentTime == null) {
            currentTime = Calendar.getInstance().getTimeInMillis();
        }
        if (med.startDate <= currentTime && med.endDate >= currentTime) {
            if (med.reminders.size() > 0) {
                for (Reminder rem : med.reminders) {
                    if (rem.getDateDayState(currentTime)) {
                        alarms.add(new AlarmItem(rem.getTimeOfDay(), med.name));
                    }
                }
            }
        }

        return alarms;
    }

    public void setAlarmEvent(AlarmItemEvent alarmEvent) {
        mAlarmEvent = alarmEvent;
    }

    public List<Alarm> getScheduleListForSelectedDate(long selectedDate) {
        if (selectedDate <= 0) {
            selectedDate = CalendarUtil.getCurrentTime();
        }
        AlarmList alarmList = new AlarmList();

        for (Med med : mMedList) {
            alarmList.addAlarmItems(extractAlarmFromRem(med, selectedDate));
        }

        return alarmList.getAlarmList();
    }

    public void updateCurrentReminderDayState(boolean status, int dayOfTheWeek, int reminderIndex) {
        mMed.updateCurrentReminderDayState(status, dayOfTheWeek, reminderIndex);
    }

    public boolean getCurrentReminderDayState(int reminderIndex, int dayOfTheWeek) {
        return mMed.getCurrentReminderDayState(reminderIndex, dayOfTheWeek);
    }

    public void removeReminderDayState(int reminderPosition) {
        mMed.removeReminderDayState(reminderPosition);
    }

    public List<Reminder> getMedReminders() {
        return mMed.reminders;
    }

    public void updateCurrentReminderTime(int reminderPosition, long timeInMillis) {
        mMed.updateReminderTime(reminderPosition, timeInMillis);
    }

    public interface MedEventListener{
        void onMedAdded();
    }

    public interface AlarmItemEvent {
        void onAlarmListChanged(List<Alarm> alarms);
    }
}
