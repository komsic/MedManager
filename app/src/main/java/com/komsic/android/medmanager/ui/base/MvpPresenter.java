package com.komsic.android.medmanager.ui.base;

/**
 * Created by komsic on 4/2/2018.
 */

public interface MvpPresenter<V extends MvpView> {

    void onAttach(V mvpView);

    void onDetach();
}
