package com.komsic.android.medmanager.data.sync;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import com.komsic.android.medmanager.data.DataManager;
import com.komsic.android.medmanager.data.model.Alarm;
import com.komsic.android.medmanager.util.CalendarUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by komsic on 4/2/2018.
 * This class will help to save alarm on Firebase Database
 */

public class SyncAlarmService extends IntentService
        implements DataManager.AlarmItemEvent, DataManager.SignOutEvent {
    private static final String TAG = "SyncAlarmService";

    public static final String ACTION_NOTIFY_EXTRA = "com.komsic.android.med_manager.ACTION_NOTIFY.medNames";

    public static final String ACTION_NOTIFY = "com.komsic.android.med_manager.ACTION_NOTIFY";
    public static final String ACTION_SERVICE = "com.komsic.android.med_manager.ACTION_SERVICE";
    private List<Alarm> mAlarmList;

    public SyncAlarmService() {
        super("SyncAlarmService");
        if (DataManager.getInstance() != null) {
            DataManager.getInstance().setServiceAlarmEvent(this);
            DataManager.getInstance().setSignOutEvent(this);
        }
    }

    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, SyncAlarmService.class);
        intent.setAction(ACTION_SERVICE);
        return intent;
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null && ACTION_SERVICE.equals(intent.getAction())) {
            if (DataManager.getInstance() != null) {
                onSignOutEventListener();

                mAlarmList = DataManager.getInstance()
                        .getScheduleListForSelectedDate(CalendarUtil.getCurrentTime());
            }
        }
    }

    @Override
    public void onAlarmListChanged(List<Alarm> alarmList) {

        if (mAlarmList != null) {
            cancelAllAlarm(mAlarmList);
        }

        for (Alarm alarm : alarmList) {
            addAlarm(alarm);
        }

        mAlarmList = alarmList;
    }

    @Override
    public void onSignOutEventListener() {
        if (mAlarmList != null) {
            cancelAllAlarm(mAlarmList);
        }
    }

    private void cancelAllAlarm(List<Alarm> alarmList) {
        Log.e(TAG, "cancelAllAlarm: cancelling alarms");
        for (Alarm alarm : alarmList) {
            cancelAlarm(alarm);
        }
    }

    private void cancelAlarm(Alarm alarm) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.cancel(setPendingIntentForAlarm(alarm));
            Log.e(TAG, "cancelAlarm: canceled alarm: " + alarm.getTimeFrom00HrsLong() + " | "
                    + CalendarUtil.getTimeInString(alarm.getTimeFrom00HrsLong()) + " | "
                    + alarm.medNames);
        }
    }

    private PendingIntent setPendingIntentForAlarm(Alarm alarm) {
        Intent intent = new Intent(ACTION_NOTIFY);
        intent.putStringArrayListExtra(ACTION_NOTIFY_EXTRA, new ArrayList<>(alarm.medNames));
        intent.putExtra("time", alarm.getTimeOfDay());

        return PendingIntent.getBroadcast(
                getApplicationContext(),
                ((int) alarm.getTimeOfDay()),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private void addAlarm(Alarm alarm) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null && alarm.getTimeFrom00HrsLong() > System.currentTimeMillis()) {

            alarmManager.setExact(AlarmManager.RTC,
                    alarm.getTimeFrom00HrsLong(), setPendingIntentForAlarm(alarm));
            Log.e(TAG, "addAlarm: added alarm: " + alarm.getTimeFrom00HrsLong() + " | "
                    + CalendarUtil.getTimeInString(alarm.getTimeFrom00HrsLong())
                    + " | " + alarm.medNames);
        }
    }
}
