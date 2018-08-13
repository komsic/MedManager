package com.komsic.android.medmanager.ui.detail.choose_day;

import android.view.View;

import com.komsic.android.medmanager.ui.base.MvpPresenter;

/**
 * Created by komsic on 4/3/2018.
 */

public interface ChooseDayDialogMvpPresenter<V extends ChooseDayDialogMvpView>
        extends MvpPresenter<V> {
    void onDismiss();

    void init();

    void onClick(View view, int dayOfTheWeek);

    void setReminderIndex(int reminderPosition);

    void updateCurrentReminderDayState(boolean newStatus, int dayOfTheWeek);
}
