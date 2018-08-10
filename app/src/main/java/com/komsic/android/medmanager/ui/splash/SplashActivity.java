package com.komsic.android.medmanager.ui.splash;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.komsic.android.medmanager.R;
import com.komsic.android.medmanager.data.DataManager;
import com.komsic.android.medmanager.ui.base.BaseActivity;
import com.komsic.android.medmanager.ui.login.LoginActivity;
import com.komsic.android.medmanager.ui.main.MainActivity;

public class SplashActivity extends BaseActivity implements SplashMvpView, View.OnClickListener {

    public static final String FRAGMENT_EXTRA = "FRAGMENT_EXTRA";
    public static final int SIGN_IN_EXTRA = 1;
    public static final int ADD_NEW_EXTRA = 2;
    private SplashPresenter<SplashMvpView> mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mPresenter = new SplashPresenter<>(DataManager.getInstance());
        mPresenter.onAttach(this);

        setUp();
    }

    @Override
    protected void setUp() {
        TextView login = findViewById(R.id.text_login);
        TextView signUp = findViewById(R.id.text_sign_up);

        login.setOnClickListener(this);
        signUp.setOnClickListener(this);
    }

    @Override
    public void openLoginActivity(int whichFragment) {
        Intent intent = LoginActivity.getStartIntent(SplashActivity.this);
        intent.putExtra(FRAGMENT_EXTRA, whichFragment);
        startActivity(intent);
        finish();
    }

    @Override
    public void openMainActivity() {
        Intent intent = MainActivity.getStartIntent(SplashActivity.this);
        startActivity(intent);
        finish();
    }

    @Override
    public void toastMessage(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.text_login:
                mPresenter.openLoginActivity(SIGN_IN_EXTRA);
                break;
            case R.id.text_sign_up:
                mPresenter.openLoginActivity(ADD_NEW_EXTRA);
                break;
            default:
                throw new UnsupportedOperationException("Not a valid view");
        }
    }
}
