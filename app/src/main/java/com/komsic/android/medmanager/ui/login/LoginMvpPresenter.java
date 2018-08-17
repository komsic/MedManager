package com.komsic.android.medmanager.ui.login;

import com.komsic.android.medmanager.ui.base.MvpPresenter;

public interface LoginMvpPresenter<V extends LogInMvpView> extends MvpPresenter<V> {

    void attachFragment(int intentExtra);
}
