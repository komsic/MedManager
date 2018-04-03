package com.komsic.android.medmanager.ui.detail;

import com.komsic.android.medmanager.ui.base.MvpPresenter;
import com.komsic.android.medmanager.ui.base.MvpView;

/**
 * Created by komsic on 4/3/2018.
 */

public interface DetailMvpPresenter<V extends MvpView> extends MvpPresenter<V> {

    void onViewPrepared(String ref);

}
