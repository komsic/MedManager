package com.komsic.android.medmanager.ui.login;

import com.komsic.android.medmanager.data.DataManager;
import com.komsic.android.medmanager.ui.base.BasePresenter;

public class LoginPresenter<V extends LogInMvpView> extends BasePresenter<V>
        implements LoginMvpPresenter<V> {

    LoginPresenter(DataManager dataManager) {
        super(dataManager);
    }

    @Override
    public void attachFragment(int whichFragment) {
        getMvpView().attachFragment(whichFragment);
    }

    @Override
    public boolean isUserSignedIn() {
        return getDataManager().isUserSignedIn();
    }
}
