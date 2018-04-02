package com.komsic.android.medmanager.ui.base;

import android.support.v7.app.AppCompatActivity;

/**
 * Created by komsic on 4/2/2018.
 */

public abstract class BaseActivity extends AppCompatActivity implements
        MvpView, BaseFragment.Callback {

    @Override
    public void onFragmentAttached() {

    }

    @Override
    public void onFragmentDetached(String tag) {

    }

    protected abstract void setUp();
}
