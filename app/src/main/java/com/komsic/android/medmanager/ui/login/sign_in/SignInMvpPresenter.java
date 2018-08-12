package com.komsic.android.medmanager.ui.login.sign_in;

import com.komsic.android.medmanager.ui.base.MvpPresenter;

public interface SignInMvpPresenter<V extends SignInMvpView> extends MvpPresenter<V> {

    void openMainActivity();

    void onForgotPasswordClicked(String email);

    void signIn(String email, String password);
}
