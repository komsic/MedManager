package com.komsic.android.medmanager.ui.main.add_med;

import com.komsic.android.medmanager.data.DataManager;
import com.komsic.android.medmanager.ui.base.BasePresenter;
import com.komsic.android.medmanager.ui.base.MvpView;

/**
 * Created by komsic on 4/2/2018.
 */

public class DialogAddMedPresenter<V extends DialogAddMedMvpView> extends BasePresenter<V>
        implements DialogAddMedMvpPresenter {

    public DialogAddMedPresenter(DataManager dataManager) {
        super(dataManager);
    }



    @Override
    public void onDoneClicked() {

    }
}
