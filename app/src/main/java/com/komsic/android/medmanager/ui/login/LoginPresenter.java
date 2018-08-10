package com.komsic.android.medmanager.ui.login;

import com.komsic.android.medmanager.data.DataManager;
import com.komsic.android.medmanager.ui.base.BasePresenter;

public class LoginPresenter<V extends LogInMvpView> extends BasePresenter<V> {

    public LoginPresenter(DataManager dataManager) {
        super(dataManager);
    }

    public void attachFragment(int whichFragment) {
        getMvpView().attachFragment(whichFragment);
    }
}
