package com.komsic.android.medmanager.ui.main;

import com.komsic.android.medmanager.data.DataManager;
import com.komsic.android.medmanager.ui.base.BasePresenter;

/**
 * Created by komsic on 4/2/2018.
 */

public class MainPresenter<V extends MainMvpView> extends BasePresenter<V>
        implements MainMvpPresenter<V> {

    MainPresenter(DataManager dataManager) {
        super(dataManager);
    }

    @Override
    public void onAttach(V mvpView) {
        super.onAttach(mvpView);
        getDataManager().fetchCurrentUser();
    }

    @Override
    public void openAddMedDialog() {
        getMvpView().openAddMedDialog();
    }
}
