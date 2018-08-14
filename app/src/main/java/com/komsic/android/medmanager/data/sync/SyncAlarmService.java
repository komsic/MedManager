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

public class SyncAlarmService extends IntentService implements DataManager.AlarmItemEvent {
    private static final String TAG = "SyncAlarmService";

    public static final String ACTION_NOTIFY_EXTRA = "com.komsic.android.med_manager.ACTION_NOTIFY.medNames";

    public static final String ACTION_NOTIFY = "com.komsic.android.med_manager.ACTION_NOTIFY";
    private List<Alarm> mAlarmList;
    private int i;

    public SyncAlarmService() {
        super("SyncAlarmService");
        if (DataManager.getInstance() != null) {
            DataManager.getInstance().setServiceAlarmEvent(this);
        }
    }

    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, SyncAlarmService.class);
        intent.setAction(ACTION_NOTIFY);
        return intent;
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (ACTION_NOTIFY.equals(intent.getAction())) {
            if (DataManager.getInstance() != null) {
                mAlarmList = DataManager.getInstance()
                        .getScheduleListForSelectedDate(CalendarUtil.getCurrentTime());
            }
        }
    }

    public void onAlarmChanged(Alarm alarm) {
        if (alarm != null) {

            Intent alarmIntent = new Intent(ACTION_NOTIFY);
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

            if (alarm.getTimeOfDay() > CalendarUtil.getCurrentTime()) {
                Log.e(TAG, "onAlarmChanged: " +  alarm.toString());

                ArrayList<String> medNames = new ArrayList<>(alarm.medNames);
                alarmIntent.putStringArrayListExtra(ACTION_NOTIFY_EXTRA, medNames);

                PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),
                        i, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                alarmManager.setExact(AlarmManager.RTC_WAKEUP, alarm.getTimeOfDay(),
                        pendingIntent);
            }
        }
    }

    @Override
    public void onAlarmListChanged(List<Alarm> alarmList) {
        int j = 0;
        for (Alarm a : alarmList) {
            if (a.getTimeOfDay() > CalendarUtil.getCurrentTimeInTimeInstanceForm()) {
                Log.e(TAG, "onAlarmListChanged: " + i + " || " + j + " | "
                        + a.getTimeOfDay() + " | "
                        + CalendarUtil.getTimeInString(a.getTimeOfDay()) + " | "
                        + a.medNames);

                Intent alarmIntent = new Intent(ACTION_NOTIFY);
                alarmIntent.putStringArrayListExtra(ACTION_NOTIFY_EXTRA,
                        new ArrayList<>(a.medNames));

                PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),
                        i, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                alarmManager.setExact(AlarmManager.RTC_WAKEUP,
                        (System.currentTimeMillis() + (j * 60000)),
                        pendingIntent);
            }
            j++;
        }
        Log.e(TAG, "onAlarmListChanged: -----------------------------------------------------------------");
        i++;

        if (mAlarmList != null) {
            cancelAllAlarm(mAlarmList);
        }

        for (Alarm alarm : alarmList) {
            addAlarm(alarm);
        }

        mAlarmList = alarmList;
    }

    private void cancelAllAlarm(List<Alarm> alarmList) {
        for (Alarm alarm : alarmList) {
            cancelAlarm(alarm);
        }
    }

    private void addAlarm(Alarm alarm) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null &&
                alarm.getTimeOfDay() > CalendarUtil.getCurrentTimeInTimeInstanceForm()) {
            alarmManager.setExact(AlarmManager.RTC,
                    alarm.getTimeOfDay(), setPendingIntentForAlarm(alarm));
        }
    }

    private void cancelAlarm(Alarm alarm) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.cancel(setPendingIntentForAlarm(alarm));
        }
    }

    private PendingIntent setPendingIntentForAlarm(Alarm alarm) {
        Intent intent = new Intent(ACTION_NOTIFY);
        intent.putStringArrayListExtra(ACTION_NOTIFY_EXTRA, new ArrayList<>(alarm.medNames));

        return PendingIntent.getBroadcast(
                getApplicationContext(),
                ((int) alarm.getTimeOfDay()),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }
}
