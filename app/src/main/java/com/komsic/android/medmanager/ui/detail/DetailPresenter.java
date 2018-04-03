package com.komsic.android.medmanager.ui.detail;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.komsic.android.medmanager.data.DataManager;
import com.komsic.android.medmanager.data.model.Med;
import com.komsic.android.medmanager.data.model.Reminder;
import com.komsic.android.medmanager.ui.base.BasePresenter;
import com.komsic.android.medmanager.ui.base.MvpView;
import com.komsic.android.medmanager.util.CalendarUtil;

/**
 * Created by komsic on 4/3/2018.
 */

public class DetailPresenter<V extends DetailMvpView> extends BasePresenter<V> implements DetailMvpPresenter<V> {

    Med med;
    DatabaseReference itemRef;

    public DetailPresenter(DataManager dataManager) {
        super(dataManager);
    }

    @Override
    public void onViewPrepared(String ref) {
         itemRef = FirebaseDatabase.getInstance().getReference()
                .child("medList/" + ref);

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                med = dataSnapshot.getValue(Med.class);

                if (med != null) {
                    getMvpView().setText(med.name, DetailActivity.MED_NAME_TEXT);
                    getMvpView().setText(med.description, DetailActivity.MED_DESCRIPTION_TEXT);
                    getMvpView().setText(CalendarUtil.getDateInString(med.startDate),
                            DetailActivity.START_DAY_TEXT);
                    getMvpView().setText(CalendarUtil.getDateInString(med.endDate), DetailActivity.END_DAY_TEXT);

                    for (Reminder rem : med.reminders) {
                        getMvpView().addReminder(rem);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                ((AppCompatActivity) getMvpView().setContext()).finish();
                Toast.makeText(getMvpView().setContext(), "Error Processing...", Toast.LENGTH_SHORT)
                        .show();
            }
        };
        itemRef.addListenerForSingleValueEvent(valueEventListener);
    }

    void addReminder(Reminder reminder) {
        med.addReminder(reminder);
    }

    void onPause(){
        if (med != null) {
            itemRef.updateChildren(med.toMap());
        }
    }

    Reminder getReminder(int viewPositionInList) {
        return med.reminders.get(viewPositionInList);
    }

    void updateReminderTimeOfDay(int viewPositionInList, long time) {
        med.updateReminderTimeOfDay(viewPositionInList, time);
    }

    void removeReminderDayState(Reminder reminder) {
        med.removeReminderDayState(reminder);
    }
}
