package com.komsic.android.medmanager.ui.main.schedule;

import com.komsic.android.medmanager.data.DataManager;
import com.komsic.android.medmanager.data.model.alarm.AlarmItem;
import com.komsic.android.medmanager.ui.base.BasePresenter;

import java.util.List;

/**
 * Created by komsic on 4/2/2018.
 */

public class MedSchedulePresenter<V extends MedScheduleMvpView> extends BasePresenter<V>
        implements MedScheduleMvpPresenter<V>, DataManager.AlarmItemEvent {
    private static final String TAG = "MedSchedulePresenter";
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
    public void onDetach() {
        getDataManager().removeListener(DataManager.CHILD_EVENT_LISTENER);
        super.onDetach();
    }

    @Override
    public void onAlarmListChanged(List<AlarmItem> alarm) {
        getMvpView().updateList(getDataManager().getScheduleListForSelectedDate(mSelectedDate));
    }
}
