package com.komsic.android.medmanager.ui.main;

import android.support.v4.app.FragmentManager;

import com.komsic.android.medmanager.ui.base.MvpPresenter;
import com.komsic.android.medmanager.ui.base.MvpView;

import static android.os.Build.VERSION_CODES.M;

/**
 * Created by komsic on 4/2/2018.
 */

public interface MainMvpPresenter<V extends MvpView> extends MvpPresenter<V> {
    void openAddMedDialog(FragmentManager fm);
}
