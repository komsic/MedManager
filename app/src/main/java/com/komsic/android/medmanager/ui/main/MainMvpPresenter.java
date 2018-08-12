package com.komsic.android.medmanager.ui.main;

import com.komsic.android.medmanager.ui.base.MvpPresenter;
import com.komsic.android.medmanager.ui.base.MvpView;

/**
 * Created by komsic on 4/2/2018.
 */

public interface MainMvpPresenter<V extends MvpView> extends MvpPresenter<V> {
    void openAddMedDialog();

    void signOut();
}
