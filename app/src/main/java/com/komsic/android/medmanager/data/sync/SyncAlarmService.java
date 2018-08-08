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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by komsic on 4/2/2018.
 * This class will help to save alarm on Firebase Database
 */

public class SyncAlarmService extends IntentService implements DataManager.AlarmEventListener {
    private static final String TAG = "SyncAlarmService";

    private Set<Alarm> mAlarmList;

    public static final String ACTION_NOTIFY = "com.komsic.android.med_manager.ACTION_NOTIFY";

    public SyncAlarmService() {
        super("SyncAlarmService");
        mAlarmList = DataManager.getInstance().getAlarmList();
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (ACTION_NOTIFY.equals(intent.getAction())) {
            DataManager.getInstance().setAlarmEventListener(this);

//            mAlarmList = new ArrayList<>();
//
//            Set<String> hh = new HashSet<>();
//            hh.add("Para");
//            long time = 13800000L;
//            Alarm a1 = new Alarm(time, hh);
//
//            mAlarmList.add(a1);
            onAlarmChanged();
        }
    }

    @Override
    public void onAlarmChanged() {
        if (mAlarmList != null) {

            Intent alarmIntent = new Intent(ACTION_NOTIFY);
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

            List<Alarm> alarmList = new ArrayList<>(mAlarmList);
            for (int i = 0; i < alarmList.size(); i++) {
                Alarm alarm = alarmList.get(i);
                if (alarm.getTimeOfDay() > CalendarUtil.getCurrentTime()) {
                    Log.e(TAG, "onAlarmChanged: " +  alarm.toString());

                    ArrayList<String> medNames = new ArrayList<>(alarm.medNames);
                    alarmIntent.putStringArrayListExtra("medNames", medNames);

                    PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),
                            i, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, alarm.getTimeOfDay(),
                            pendingIntent);
                }

            }
        }
    }
}
