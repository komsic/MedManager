package com.komsic.android.medmanager.ui.main.add_med;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.komsic.android.medmanager.data.DataManager;
import com.komsic.android.medmanager.data.model.Med;
import com.komsic.android.medmanager.ui.base.BasePresenter;

import java.util.Calendar;

/**
 * Created by komsic on 4/3/2018.
 */

public class AddMedDialogPresenter<V extends AddMedDialogMvpView> extends BasePresenter<V>
        implements AddMedDialogMvpPresenter{

    public AddMedDialogPresenter(DataManager dataManager) {
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

        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();
        newMed.id = databaseRef.child("medList").push().getKey();
        databaseRef.child("medList" + "/" + newMed.id).setValue(newMed);

//        Intent intent = new Intent(getActivity(), DetailActivity.class);
//        intent.putExtra("key", newMed.id);
//        startActivity(intent);
    }
}
