package com.komsic.android.medmanager.ui.detail.choose_day;

import com.komsic.android.medmanager.ui.base.MvpPresenter;

/**
 * Created by komsic on 4/3/2018.
 */

public interface ChooseDayDialogMvpPresenter<V extends ChooseDayDialogMvpView>
        extends MvpPresenter<V> {
    void onDismiss();
}
