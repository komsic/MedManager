package com.komsic.android.medmanager.ui.main.list;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.komsic.android.medmanager.data.DataManager;
import com.komsic.android.medmanager.data.model.Med;
import com.komsic.android.medmanager.ui.base.BasePresenter;

/**
 * Created by komsic on 4/2/2018.
 */

public class MedListPresenter<V extends MedListMvpView> extends BasePresenter<V>
        implements MedListMvpPresenter<V>{

    public MedListPresenter(DataManager dataManager){
        super(dataManager);
    }

    @Override
    public void onViewPrepared() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("medList");
        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Med med = dataSnapshot.getValue(Med.class);

                getMvpView().updateList(med);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
