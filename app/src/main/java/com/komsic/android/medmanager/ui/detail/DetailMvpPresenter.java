package com.komsic.android.medmanager.ui.detail;

import android.view.View;

import com.komsic.android.medmanager.data.model.Reminder;
import com.komsic.android.medmanager.ui.base.MvpPresenter;
import com.komsic.android.medmanager.ui.base.MvpView;

import java.util.ArrayList;

/**
 * Created by komsic on 4/3/2018.
 */

public interface DetailMvpPresenter<V extends MvpView> extends MvpPresenter<V> {

    void onViewPrepared(String ref);

    void removeReminderDayState(Reminder reminder);

    void onPause();

    void onDayReminderClick(View view, ArrayList<View> reminderViews);

    void onTimeReminderClick(View view, ArrayList<View> reminderViews);

    void addReminder();
}
