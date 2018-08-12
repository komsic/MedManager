package com.komsic.android.medmanager.ui.splash;

import com.google.firebase.auth.FirebaseAuth;
import com.komsic.android.medmanager.data.DataManager;
import com.komsic.android.medmanager.ui.base.BasePresenter;

public class SplashPresenter<V extends SplashMvpView> extends BasePresenter<V>
        implements SplashMvpPresenter<V> {

    SplashPresenter(DataManager dataManager) {
        super(dataManager);
    }

    @Override
    public void onAttach(V mvpView) {
        super.onAttach(mvpView);

        decideNextActivity();
    }

    private void decideNextActivity() {
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            getMvpView().openMainActivity();
        }
    }

    @Override
    public void openLoginActivity(int whichFragment) {
        getMvpView().openLoginActivity(whichFragment);
    }
}
