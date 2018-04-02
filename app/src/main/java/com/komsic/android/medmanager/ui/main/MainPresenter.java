package com.komsic.android.medmanager.ui.main;

import com.komsic.android.medmanager.data.DataManager;
import com.komsic.android.medmanager.ui.base.BasePresenter;
import com.komsic.android.medmanager.ui.base.MvpView;

/**
 * Created by komsic on 4/2/2018.
 */

public class MainPresenter<V extends MvpView> extends BasePresenter<V>
        implements MainMvpPresenter<V> {

    public MainPresenter(DataManager dataManager) {
        super(dataManager);
    }
}
