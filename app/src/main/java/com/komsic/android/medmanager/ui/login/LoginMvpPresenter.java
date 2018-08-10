package com.komsic.android.medmanager.ui.login;

import android.app.Fragment;

import com.komsic.android.medmanager.ui.base.MvpPresenter;
import com.komsic.android.medmanager.ui.base.MvpView;

public interface LoginMvpPresenter<V extends MvpView> extends MvpPresenter<V> {

    void attachFragment(Fragment fragment, int whichFragment);
}
