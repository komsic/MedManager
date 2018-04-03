package com.komsic.android.medmanager.ui.main;

import android.support.v4.app.FragmentManager;

import com.komsic.android.medmanager.data.DataManager;
import com.komsic.android.medmanager.ui.base.BasePresenter;
import com.komsic.android.medmanager.ui.base.MvpView;
import com.komsic.android.medmanager.ui.main.add_med.AddMedDialog;

/**
 * Created by komsic on 4/2/2018.
 */

public class MainPresenter<V extends MvpView> extends BasePresenter<V>
        implements MainMvpPresenter<V> {

    public MainPresenter(DataManager dataManager) {
        super(dataManager);
    }

    @Override
    public void openAddMedDialog(FragmentManager fm) {
        AddMedDialog dialogAddMed = new AddMedDialog();
        dialogAddMed.show(fm, "DialogAddMed");
    }
}
