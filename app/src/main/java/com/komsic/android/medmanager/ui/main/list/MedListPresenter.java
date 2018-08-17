package com.komsic.android.medmanager.ui.main.list;

import com.komsic.android.medmanager.data.DataManager;
import com.komsic.android.medmanager.ui.base.BasePresenter;

/**
 * Created by komsic on 4/2/2018.
 */

public class MedListPresenter<V extends MedListMvpView> extends BasePresenter<V>
        implements MedListMvpPresenter<V>, DataManager.MedEventListener{

    MedListPresenter(DataManager dataManager) {
        super(dataManager);
    }

    @Override
    public void onViewPrepared() {
        getDataManager().setMedEventList(this);
    }

    @Override
    public void signOut() {
        getDataManager().signOut();
        getMvpView().onSignOutDone();
    }

    @Override
    public void removeMed(int position) {
        getDataManager().removeMed(position);
    }

    @Override
    public void onDetach() {
        getDataManager().removeListener(DataManager.CHILD_EVENT_LISTENER);
        getDataManager().clearMedList();
        super.onDetach();
    }

    @Override
    public void onMedAdded() {
        if (getDataManager().getMedList() != null) {
            getMvpView().updateList(getDataManager().getMedList());
        }
    }

    @Override
    public void onMedChanged(int indexToBeChanged) {
        getMvpView().updateMedAtIndexAt(indexToBeChanged);
    }

    @Override
    public void onMedRemoved(int indexDeleted) {
        getMvpView().notifyMedRemoved(indexDeleted);
    }
}
