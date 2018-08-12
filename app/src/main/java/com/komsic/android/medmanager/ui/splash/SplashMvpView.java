package com.komsic.android.medmanager.ui.splash;

import com.komsic.android.medmanager.ui.base.MvpView;

public interface SplashMvpView extends MvpView {

    void openLoginActivity(int whichFragment);

    void openMainActivity();
}
