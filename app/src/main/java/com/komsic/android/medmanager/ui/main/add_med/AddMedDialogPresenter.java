package com.komsic.android.medmanager.ui.main.add_med;

import android.content.Intent;

import com.komsic.android.medmanager.data.DataManager;
import com.komsic.android.medmanager.data.model.Med;
import com.komsic.android.medmanager.ui.base.BasePresenter;
import com.komsic.android.medmanager.ui.detail.DetailActivity;

/**
 * Created by komsic on 4/3/2018.
 */

public class AddMedDialogPresenter<V extends AddMedDialogMvpView> extends BasePresenter<V>
        implements AddMedDialogMvpPresenter<V> {

    AddMedDialogPresenter(DataManager dataManager) {
        super(dataManager);
    }

    @Override
    public void onDoneClicked(String name, String description, long startTime, long endTime) {
        addNewMed(name, description, startTime, endTime);
    }


    @Override
    public void onDismiss() {
        getMvpView().dismissDialog();
    }

    private void addNewMed(String name, String description, long startTime, long endTime) {
        Med newMed = new Med();
        newMed.name = name;
        newMed.description = description;
        newMed.startDate = startTime;
        newMed.endDate = endTime;

        getDataManager().addMed(newMed);

        Intent intent = new Intent(getMvpView().getContext(), DetailActivity.class);
        intent.putExtra("key", newMed.id);
        getMvpView().getContext().startActivity(intent);
    }
}
