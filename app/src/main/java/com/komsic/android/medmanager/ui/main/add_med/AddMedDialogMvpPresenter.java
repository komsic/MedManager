package com.komsic.android.medmanager.ui.main.add_med;

import com.komsic.android.medmanager.ui.base.MvpPresenter;

import java.util.Calendar;

/**
 * Created by komsic on 4/3/2018.
 */

public interface AddMedDialogMvpPresenter<V extends AddMedDialogMvpView> {
    void onDoneClicked(String name, String description, long startTime, long endTime);

    void onDismiss();
}
