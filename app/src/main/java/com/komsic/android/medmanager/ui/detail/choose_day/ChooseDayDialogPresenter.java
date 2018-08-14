package com.komsic.android.medmanager.ui.detail.choose_day;

import android.view.View;

import com.komsic.android.medmanager.data.DataManager;
import com.komsic.android.medmanager.ui.base.BasePresenter;


/**
 * Created by komsic on 4/3/2018.
 */

public class ChooseDayDialogPresenter<V extends ChooseDayDialogMvpView> extends BasePresenter<V>
        implements ChooseDayDialogMvpPresenter<V> {

    private int reminderIndex;

    ChooseDayDialogPresenter(DataManager dataManager) {
        super(dataManager);
    }

    @Override
    public void onDismiss() {
        getMvpView().dismissDialog("dialogChooseDay");
    }

    @Override
    public void init() {
        if (getDataManager().getDayStateMap(reminderIndex) != null &&
                getMvpView() != null &&
                getDataManager() != null) {
            getMvpView().init(getDataManager().getDayStateMap(reminderIndex));
        }
    }

    @Override
    public void onClick(View v, int dayOfTheWeek) {
        boolean status = getDataManager().getCurrentReminderDayState(reminderIndex, dayOfTheWeek);
        getMvpView().onDayClicked(status, v, dayOfTheWeek);
    }

    @Override
    public void setReminderIndex(int reminderPosition) {
        reminderIndex = reminderPosition;
    }

    @Override
    public void updateCurrentReminderDayState(boolean newStatus, int dayOfTheWeek) {
        getDataManager().updateCurrentReminderDayState(newStatus, dayOfTheWeek, reminderIndex);
    }
}
