package com.komsic.android.medmanager.ui.login.sign_in;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.komsic.android.medmanager.data.DataManager;
import com.komsic.android.medmanager.ui.base.BasePresenter;

public class SignInPresenter<V extends SignInMvpView> extends BasePresenter<V>
        implements SignInMvpPresenter<V>, OnCompleteListener<AuthResult> {

    SignInPresenter(DataManager dataManager) {
        super(dataManager);
    }

    @Override
    public void openMainActivity() {
        getMvpView().openMainActivity();
    }

    @Override
    public void onForgotPasswordClicked(String email) {
        if (!getDataManager().onForgotPasswordClicked(email)) {
            getMvpView().issueForgottenEmailError();
        }
    }

    @Override
    public void signIn(String email, String password) {
        getDataManager().signIn(email, password, getMvpView().getBaseActivity(), this);
    }

    @Override
    public void onComplete(@NonNull Task<AuthResult> task) {
        if (task.isSuccessful()) {
            getMvpView().openMainActivity();
        } else {
            getMvpView().issueError(true);
        }
    }
}
