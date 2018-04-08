package com.komsic.android.medmanager.ui.main.schedule;

import android.util.Log;

import com.komsic.android.medmanager.data.DataManager;
import com.komsic.android.medmanager.ui.base.BasePresenter;

/**
 * Created by komsic on 4/2/2018.
 */

public class MedSchedulePresenter<V extends MedScheduleMvpView> extends BasePresenter<V>
        implements MedScheduleMvpPresenter<V>, DataManager.MedEventListener {


    public MedSchedulePresenter(DataManager dataManager) {
        super(dataManager);
    }

    @Override
    public void onViewPrepared() {
        getDataManager().setMedEventSchedule(this);
    }

    @Override
    public void onMedAdded() {
        getMvpView().updateList(getDataManager().getMed());
    }

    @Override
    public void onDetach() {
        super.onDetach();
        getDataManager().removeListener(DataManager.CHILD_EVENT_LISTENER);
    }
}
