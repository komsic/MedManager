package com.komsic.android.medmanager.ui.login.new_user;

import com.komsic.android.medmanager.ui.base.MvpPresenter;

public interface NewUserMvpPresenter<V extends NewUserMvpView> extends MvpPresenter<V> {

    void openMainActivity();

    void createUserWithEmailAndPassword(String email, String password, String fullName, String userName);
}
