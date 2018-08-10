package com.komsic.android.medmanager.ui.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.komsic.android.medmanager.R;
import com.komsic.android.medmanager.data.DataManager;
import com.komsic.android.medmanager.ui.base.BaseActivity;
import com.komsic.android.medmanager.ui.login.new_user.NewUserFragment;
import com.komsic.android.medmanager.ui.login.sign_in.SignInFragment;
import com.komsic.android.medmanager.ui.splash.SplashActivity;

import static com.komsic.android.medmanager.ui.splash.SplashActivity.ADD_NEW_EXTRA;
import static com.komsic.android.medmanager.ui.splash.SplashActivity.SIGN_IN_EXTRA;

public class LoginActivity extends BaseActivity implements LogInMvpView {

    private int mIntentExtra;

    private LoginPresenter<LogInMvpView> mPresenter;

    public static Intent getStartIntent(Context context) {
        return new Intent(context, LoginActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mPresenter = new LoginPresenter<>(DataManager.getInstance());
        mPresenter.onAttach(this);

        setUp();
    }

    @Override
    protected void setUp() {
        mIntentExtra = getIntent().getIntExtra(SplashActivity.FRAGMENT_EXTRA, 1);

        if (mIntentExtra > 0) {
            mPresenter.attachFragment(mIntentExtra);
        }
    }

    @Override
    public void attachFragment(int whichFragment) {
        Fragment fragment = getFragment(whichFragment);

        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.container, fragment);
        transaction.commit();
    }

    private Fragment getFragment(int whichFragment) {
        switch (whichFragment) {
            case SIGN_IN_EXTRA:
                return SignInFragment.newInstance();
            case ADD_NEW_EXTRA:
                return NewUserFragment.newInstance();
            default:
                throw new UnsupportedOperationException("Fragment invalid");
        }
    }
}

