package com.komsic.android.medmanager.ui.login;

import com.komsic.android.medmanager.ui.base.MvpPresenter;
import com.komsic.android.medmanager.ui.base.MvpView;

public interface LoginMvpPresenter<V extends MvpView> extends MvpPresenter<V> {

    void attachFragment(int intentExtra);

    boolean isUserSignedIn();
}
