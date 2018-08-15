package com.komsic.android.medmanager.ui.login.sign_in;

import com.komsic.android.medmanager.ui.base.BaseActivity;
import com.komsic.android.medmanager.ui.base.MvpView;

public interface SignInMvpView extends MvpView {

    void openSyncAlarmService();

    BaseActivity getBaseActivity();

    void issueError(boolean status);

    void issueForgottenEmailError();
}
