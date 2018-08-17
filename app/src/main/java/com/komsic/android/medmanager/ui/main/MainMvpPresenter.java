package com.komsic.android.medmanager.ui.main;

import com.komsic.android.medmanager.ui.base.MvpPresenter;

/**
 * Created by komsic on 4/2/2018.
 */

public interface MainMvpPresenter<V extends MainMvpView> extends MvpPresenter<V> {
    void openAddMedDialog();

    void startSyncAlarmService();
}
