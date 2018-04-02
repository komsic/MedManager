package com.komsic.android.medmanager.ui.main.add_med;

import com.komsic.android.medmanager.ui.base.DialogMvpView;
import com.komsic.android.medmanager.ui.base.MvpPresenter;

/**
 * Created by komsic on 4/2/2018.
 */

public interface DialogAddMedMvpPresenter<V extends DialogMvpView> extends MvpPresenter<V> {
    void onDoneClicked();


}
