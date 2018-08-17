package com.komsic.android.medmanager.ui.main.schedule;

import com.komsic.android.medmanager.data.DataManager;
import com.komsic.android.medmanager.data.model.Alarm;
import com.komsic.android.medmanager.ui.base.BasePresenter;

import java.util.List;

/**
 * Created by komsic on 4/2/2018.
 */

public class MedSchedulePresenter<V extends MedScheduleMvpView> extends BasePresenter<V>
        implements MedScheduleMvpPresenter<V>, DataManager.AlarmItemEvent {
    private long mSelectedDate;

    MedSchedulePresenter(DataManager dataManager) {
        super(dataManager);
    }

    @Override
    public void onAttach(V mvpView) {
        super.onAttach(mvpView);
        getDataManager().setAlarmEvent(this);
    }

    @Override
    public void onDateSelected(long selectedDate) {
        mSelectedDate = selectedDate;
        getMvpView().updateList(getDataManager().getScheduleListForSelectedDate(selectedDate));
    }

    @Override
    public void onAlarmListChanged(List<Alarm> alarm) {
        getMvpView().updateList(getDataManager().getScheduleListForSelectedDate(mSelectedDate));
    }

    @Override
    public void signOut() {
        getDataManager().signOut();
        getMvpView().onSignOutDone();
    }

    @Override
    public void onDetach() {
        getDataManager().removeListener(DataManager.CHILD_EVENT_LISTENER);
        getDataManager().removeAlarmEvent();
        super.onDetach();
    }
}
