package com.komsic.android.medmanager.ui.login.new_user;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.komsic.android.medmanager.data.DataManager;
import com.komsic.android.medmanager.ui.base.BasePresenter;

public class NewUserPresenter<V extends NewUserMvpView> extends BasePresenter<V>
        implements OnCompleteListener<AuthResult>, NewUserMvpPresenter<V> {
    private static final String TAG = "NewUserPresenter";

    private String mFullName;
    private String mUsername;

    NewUserPresenter(DataManager dataManager) {
        super(dataManager);
    }

    @Override
    public void openMainActivity() {

    }

    @Override
    public void createUserWithEmailAndPassword(String email, String password,
                                               String fullName, String username) {
        mFullName = fullName;
        mUsername = username;
        getDataManager().createUserWithEmailAndPassword(email, password, this,
                getMvpView().getBaseActivity());
    }

    @Override
    public void onComplete(@NonNull Task<AuthResult> task) {
        if (task.isSuccessful()) {
            if (!mUsername.isEmpty() && !mFullName.isEmpty()) {
                getDataManager().storeUser(mFullName, mUsername);
            }
            getMvpView().openMainActivity();
        } else {
            getMvpView().issueError();
        }
    }
}
