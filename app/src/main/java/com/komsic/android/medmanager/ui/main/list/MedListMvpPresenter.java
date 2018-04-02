package com.komsic.android.medmanager.ui.main.list;

import com.komsic.android.medmanager.ui.base.MvpPresenter;

/**
 * Created by komsic on 4/2/2018.
 */

public interface MedListMvpPresenter<V extends MedListMvpView>
        extends MvpPresenter<V> {

    void onViewPrepared();
}
