package com.komsic.android.medmanager.ui.main.schedule;

import com.komsic.android.medmanager.ui.base.MvpPresenter;

/**
 * Created by komsic on 4/2/2018.
 */

public interface MedScheduleMvpPresenter<V extends MedScheduleMvpView> extends MvpPresenter<V> {

    void onViewPrepared();
}
