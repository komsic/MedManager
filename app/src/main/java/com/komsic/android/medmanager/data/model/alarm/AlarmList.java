package com.komsic.android.medmanager.data.model.alarm;

import android.annotation.SuppressLint;

import com.komsic.android.medmanager.data.model.Alarm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AlarmList {
    private Map<Long, Set<String>> alarmList;

    @SuppressLint("UseSparseArrays")
    public AlarmList() {
        alarmList = new HashMap<>();
    }

    public void addAlarmItems(List<AlarmItem> alarms) {
        for (AlarmItem alarm : alarms) {
            addAlarmItem(alarm);
        }
    }

    private void addAlarmItem(AlarmItem alarm) {
        if (isTimeAlreadyAdded(alarm.time)) {
            if (!getNameSet(alarm.time).contains(alarm.medName)) {
                addMedNameToSet(alarm);
            }
        } else {
            Set<String> medNames = new HashSet<>();
            medNames.add(alarm.medName);

            alarmList.put(alarm.time, medNames);
        }
    }

    private boolean isTimeAlreadyAdded(long time) {
        return alarmList.keySet().contains(time);
    }

    private Set<String> getNameSet(long time) {
        return alarmList.get(time);
    }

    private void addMedNameToSet(AlarmItem alarm) {
        getNameSet(alarm.time).add(alarm.medName);
    }

    public void removeAlarmItems(List<AlarmItem> alarms) {
        for (AlarmItem alarm : alarms) {
            if (isTimeAlreadyAdded(alarm.time)) {
                if (getNameSet(alarm.time).size() == 1) {
                    alarmList.remove(alarm.time);
                } else {
                    removeMedNameToSet(alarm);
                }
            }
        }
    }

    private void removeMedNameToSet(AlarmItem alarm) {
        getNameSet(alarm.time).remove(alarm.medName);
    }

    public void changeAlarmItemTime(AlarmItem alarm, long newTime) {
        if (isTimeAlreadyAdded(alarm.time)) {
            if (getNameSet(alarm.time).size() == 1) {
                Set<String> medNames = getNameSet(alarm.time);
                alarmList.remove(alarm.time);

                alarmList.put(newTime, medNames);
            } else {
                removeMedNameToSet(alarm);

                alarm.time = newTime;
                addAlarmItem(alarm);
            }
        }
    }

    public void clear() {
        alarmList.clear();
    }

    public List<Alarm> getAlarmList() {
        List<Alarm> alarms = new ArrayList<>();
        for (long timeKey : alarmList.keySet()) {
            alarms.add(new Alarm(timeKey, alarmList.get(timeKey)));
        }
        Collections.sort(alarms);

        return alarms;
    }
}
