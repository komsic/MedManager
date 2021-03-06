package com.komsic.android.medmanager.ui.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.komsic.android.medmanager.R;
import com.komsic.android.medmanager.data.DataManager;
import com.komsic.android.medmanager.ui.base.BaseActivity;
import com.komsic.android.medmanager.ui.login.new_user.NewUserFragment;
import com.komsic.android.medmanager.ui.login.sign_in.SignInFragment;
import com.komsic.android.medmanager.ui.splash.SplashActivity;

import static com.komsic.android.medmanager.ui.splash.SplashActivity.ADD_NEW_EXTRA;
import static com.komsic.android.medmanager.ui.splash.SplashActivity.SIGN_IN_EXTRA;

public class LoginActivity extends BaseActivity implements LogInMvpView {

    private LoginMvpPresenter<LogInMvpView> mPresenter;

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
        int intentExtra = getIntent().getIntExtra(SplashActivity.FRAGMENT_EXTRA, 1);

        if (intentExtra > 0) {
            mPresenter.attachFragment(intentExtra);
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


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(SplashActivity.getStartIntent(this));
        finish();
    }

    @Override
    protected void onDestroy() {
        mPresenter.onDetach();
        super.onDestroy();
    }
}

