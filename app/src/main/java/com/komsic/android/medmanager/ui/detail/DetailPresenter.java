package com.komsic.android.medmanager.ui.detail;

import com.komsic.android.medmanager.data.DataManager;
import com.komsic.android.medmanager.data.model.Med;
import com.komsic.android.medmanager.data.model.Reminder;
import com.komsic.android.medmanager.ui.base.BasePresenter;
import com.komsic.android.medmanager.util.CalendarUtil;

/**
 * Created by komsic on 4/3/2018.
 */

public class DetailPresenter<V extends DetailMvpView> extends BasePresenter<V>
        implements DetailMvpPresenter<V>, DataManager.MedEventListener {

    private static final String TAG = "DetailPresenter";

    private String databaseRef;

    DetailPresenter(DataManager dataManager) {
        super(dataManager);
    }

    @Override
    public void onViewPrepared(String ref) {
        databaseRef = ref;
        getDataManager().addListenerForSingleValueEvent(ref, this);
    }

    @Override
    public void removeReminderDayState(int reminderPosition) {
        getDataManager().removeReminderDayState(reminderPosition);
    }

    @Override
    public void onPause() {
        if (getDataManager().getMed() != null) {
            getDataManager().updateChildren(databaseRef, getDataManager().getMed());
        }
    }

    @Override
    public void addReminder() {
        Reminder reminder = new Reminder();
        reminder.init();
        getDataManager().getMed().addReminder(reminder);
        getMvpView().updateReminderList(reminder);
        getDataManager().processAlarm(getDataManager().getMed());
    }

    @Override
    public void onDialogDismissed() {
        getMvpView().updateReminderList(getDataManager().getMedReminders());
    }

    @Override
    public void onDayReminderClick(int reminderPosition) {
        getMvpView().openDialogChooseDay(reminderPosition);
    }

    @Override
    public void updateCurrentReminderTime(int reminderPosition, long timeInMillis) {
        getDataManager().updateCurrentReminderTime(reminderPosition, timeInMillis);
        getMvpView().updateReminderList(getDataManager().getMedReminders());
    }

    @Override
    public void onMedAdded() {
        if (getDataManager().getMed() != null) {
            Med med = getDataManager().getMed();
            getMvpView().setText(med.name, DetailActivity.MED_NAME_TEXT);
            getMvpView().setText(med.description, DetailActivity.MED_DESCRIPTION_TEXT);
            getMvpView().setText(CalendarUtil.getDateInString(med.startDate),
                    DetailActivity.START_DAY_TEXT);
            getMvpView().setText(CalendarUtil.getDateInString(med.endDate), DetailActivity.END_DAY_TEXT);

            for (Reminder rem : med.reminders) {
                getMvpView().updateReminderList(rem);
            }
        }
    }

    @Override
    public void onDetach() {
        getDataManager().removeListener(DataManager.VALUE_EVENT_LISTENER);
        super.onDetach();
    }
}