package com.komsic.android.medmanager.ui.login.new_user;

import com.komsic.android.medmanager.ui.base.BaseActivity;
import com.komsic.android.medmanager.ui.base.MvpView;

public interface NewUserMvpView extends MvpView {

    BaseActivity getBaseActivity();

    void openMainActivity();

    void issueError();

    void openSyncAlarmService();
}
