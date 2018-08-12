package com.komsic.android.medmanager.ui.main.schedule;

import com.komsic.android.medmanager.data.DataManager;
import com.komsic.android.medmanager.ui.base.BasePresenter;

/**
 * Created by komsic on 4/2/2018.
 */

public class MedSchedulePresenter<V extends MedScheduleMvpView> extends BasePresenter<V>
        implements MedScheduleMvpPresenter<V>, DataManager.MedEventListener {

    private long mSelectedDate;

    MedSchedulePresenter(DataManager dataManager) {
        super(dataManager);
    }

    @Override
    public void onViewPrepared() {
        //getMvpView().updateList(getDataManager().getMed());

        getDataManager().setMedEventSchedule(this);
    }

    @Override
    public void onDateSelected(long selectedDate) {
        mSelectedDate = selectedDate;
        getMvpView().updateList(getDataManager().processSchedule(mSelectedDate));
    }

    @Override
    public void onMedAdded() {
        //getMvpView().updateList(getDataManager().getMed());
        getMvpView().updateList(getDataManager().processSchedule(mSelectedDate));
    }

    @Override
    public void onDetach() {
        getDataManager().removeListener(DataManager.CHILD_EVENT_LISTENER);
        super.onDetach();
    }
}
