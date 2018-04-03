package com.komsic.android.medmanager.ui.detail;

import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TimePicker;

import com.komsic.android.medmanager.data.DataManager;
import com.komsic.android.medmanager.data.model.Med;
import com.komsic.android.medmanager.data.model.Reminder;
import com.komsic.android.medmanager.ui.base.BasePresenter;
import com.komsic.android.medmanager.ui.detail.choose_day.ChooseDayDialog;
import com.komsic.android.medmanager.util.CalendarUtil;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by komsic on 4/3/2018.
 */

public class DetailPresenter<V extends DetailMvpView> extends BasePresenter<V>
        implements DetailMvpPresenter<V>, DataManager.MedEventListener {

    public DetailPresenter(DataManager dataManager) {
        super(dataManager);
    }

    @Override
    public void onViewPrepared(String ref) {

        getDataManager().addListenerForSingleValueEvent(ref, this);
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
                getMvpView().addReminder(rem);
            }
        }
    }

    void addReminder() {
        Reminder reminder = new Reminder();
        reminder.init();
        getDataManager().getMed().addReminder(reminder);
        getMvpView().addReminder(reminder);
    }

    void onPause() {
        if (getDataManager().getMed() != null) {
            //itemRef.updateChildren(getDataManager().getMed().toMap());
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        getDataManager().removeListener(DataManager.VALUE_EVENT_LISTENER);
    }

    Reminder getReminder(int viewPositionInList) {
        return getDataManager().getMed().reminders.get(viewPositionInList);
    }

    void updateReminderTimeOfDay(int viewPositionInList, long time) {
        getDataManager().getMed().updateReminderTimeOfDay(viewPositionInList, time);
    }

    void removeReminderDayState(Reminder reminder) {
        getDataManager().getMed().removeReminderDayState(reminder);
    }

    void onDayReminderClick(View view, ArrayList<View> reminderViews) {
        // This variable will allow us to determine view's position and therefore the appropriate
        // reminder for it
        final LinearLayout layout = (LinearLayout) view.getParent();
        final int viewPositionInList = reminderViews.indexOf(layout);
        Reminder viewReminder = getReminder(viewPositionInList);

        final ChooseDayDialog dialogChooseDay = new ChooseDayDialog();
        dialogChooseDay.setDayStateMap(viewReminder.dayStates);
        dialogChooseDay.show(((AppCompatActivity) view.getContext()).getSupportFragmentManager(),
                "dialogChooseDay");

        ((AppCompatActivity) view.getContext()).getSupportFragmentManager()
                .executePendingTransactions();
        dialogChooseDay.getDialog().setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                getDataManager().getMed().updateReminderDayState(viewPositionInList, dialogChooseDay.getDayStateMap());
                getMvpView().reload(layout, getReminder(viewPositionInList));
            }
        });
    }

    public void onTimeReminderClick(View view, ArrayList<View> reminderViews) {
        //This variable will allow us to determine view's position and therefore the appropriate
        // reminder for it
        final LinearLayout layout = (LinearLayout) view.getParent();
        final int viewPositionInList = reminderViews.indexOf(layout);
        Reminder viewReminder = getReminder(viewPositionInList);

        TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                updateReminderTimeOfDay(viewPositionInList, calendar.getTimeInMillis());
                getMvpView().reload(layout, getReminder(viewPositionInList));
            }
        };

        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(viewReminder.timeOfDay);

        TimePickerDialog timePickerDialog = new TimePickerDialog(view.getContext(),
                timeSetListener, c.get(Calendar.HOUR_OF_DAY),
                c.get(Calendar.MINUTE), false);
        timePickerDialog.show();
    }
}
