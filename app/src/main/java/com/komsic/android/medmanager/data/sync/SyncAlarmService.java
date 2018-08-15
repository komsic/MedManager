package com.komsic.android.medmanager.data.sync;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import com.komsic.android.medmanager.data.DataManager;
import com.komsic.android.medmanager.data.model.Alarm;
import com.komsic.android.medmanager.ui.main.MainActivity;
import com.komsic.android.medmanager.util.CalendarUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

/**
 * Created by komsic on 4/2/2018.
 */

public class SyncAlarmService extends IntentService
        implements DataManager.AlarmItemEvent, DataManager.SignOutEvent {
    private static final String TAG = "SyncAlarmService";

    public static final String ACTION_NOTIFY_EXTRA = "com.komsic.android.med_manager.ACTION_NOTIFY.medNames";
    public static final String ACTION_NOTIFY = "com.komsic.android.med_manager.ACTION_NOTIFY";
    public static final String ACTION_SERVICE = "com.komsic.android.med_manager.ACTION_SERVICE";
    public static final String ACTION_INIT_SYNC_ALARM = "com.komsic.android.med_manager.ACTION_INIT_SYNC_ALARM";
    public static final String ACTION_SET__SYNC_ALARM = "com.komsic.android.med_manager.ACTION_SET__SYNC_ALARM";

    private List<Alarm> mAlarmList;

    public SyncAlarmService() {
        super("SyncAlarmService");
        if (DataManager.getInstance() != null) {
            DataManager.getInstance().setServiceAlarmEvent(this);
            DataManager.getInstance().setSignOutEvent(this);
        }
    }

    public static Intent getStartIntent(Context context) {
        return new Intent(context, SyncAlarmService.class);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            if (ACTION_SERVICE.equals(intent.getAction())) {
                setUpSyncAlarmTrigger();
                openMainActivity();
            } else if (ACTION_SET__SYNC_ALARM.equals(intent.getAction())) {
                mAlarmList = DataManager.getInstance()
                        .getScheduleListForSelectedDate(System.currentTimeMillis());

                onAlarmListChanged(mAlarmList);
            }
        }
    }

    private void setUpSyncAlarmTrigger() {
        PendingIntent pendingIntent = setUpSyncAlarmPendingIntent();

        Calendar currentDateCalendar = Calendar.getInstance();
        currentDateCalendar.set(Calendar.HOUR, 0);
        currentDateCalendar.set(Calendar.MINUTE, 0);
        currentDateCalendar.set(Calendar.SECOND, 0);
        currentDateCalendar.set(Calendar.AM_PM, Calendar.AM);

        long time = currentDateCalendar.getTimeInMillis();

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.setRepeating(AlarmManager.RTC,
                    time,
                    AlarmManager.INTERVAL_DAY,
                    pendingIntent);
        }
    }

    private void openMainActivity() {
        Intent intent = MainActivity.getStartIntent(this);
        intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private PendingIntent setUpSyncAlarmPendingIntent() {
        Intent intent = new Intent(ACTION_INIT_SYNC_ALARM);

        int ALARM_ID = 500;
        return PendingIntent.getBroadcast(
                getApplicationContext(),
                ALARM_ID,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
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

            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            if (alarmManager != null) {
                alarmManager.cancel(setUpSyncAlarmPendingIntent());
            }

            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            if (notificationManager != null) {
                notificationManager.cancelAll();
            }
        }
    }

    private void cancelAllAlarm(List<Alarm> alarmList) {
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
        if (alarmManager != null && alarm.getTimeFrom00HrsLong() >= (System.currentTimeMillis() - 60000)) {

            alarmManager.setExact(AlarmManager.RTC,
                    alarm.getTimeFrom00HrsLong(), setPendingIntentForAlarm(alarm));
            Log.e(TAG, "addAlarm: added alarm: " + alarm.getTimeFrom00HrsLong() + " | "
                    + CalendarUtil.getTimeInString(alarm.getTimeFrom00HrsLong())
                    + " | " + alarm.medNames);
        }
    }
}