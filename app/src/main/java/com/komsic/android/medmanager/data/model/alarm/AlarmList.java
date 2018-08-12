package com.komsic.android.medmanager.data.model.alarm;

import android.annotation.SuppressLint;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AlarmList {
    private Map<Long, Set<String>> alarmList;
    private AlarmEvent alarmEvent;

    @SuppressLint("UseSparseArrays")
    public AlarmList() {
        alarmList = new HashMap<>();
    }

    public void addAlarms(List<Alarm> alarms) {
        for (Alarm alarm : alarms) {
            addAlarm(alarm);
            if (alarmEvent != null) {
                alarmEvent.onNewAlarmAdded(alarm);
            }
        }
    }

    private void addAlarm(Alarm alarm) {
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

    private void addMedNameToSet(Alarm alarm) {
        getNameSet(alarm.time).add(alarm.medName);

        if (alarmEvent != null) {
            alarmEvent.onNameAddedToSet(alarm);
        }
    }

    public void removeAlarms(List<Alarm> alarms) {
        for (Alarm alarm : alarms) {
            if (isTimeAlreadyAdded(alarm.time)) {
                if (getNameSet(alarm.time).size() == 1) {
                    alarmList.remove(alarm.time);

                    if (alarmEvent != null) {
                        alarmEvent.onAlarmRemoved(alarm);
                    }
                } else {
                    removeMedNameToSet(alarm);

                    if (alarmEvent != null) {
                        alarmEvent.onNameRemovedFromSet(alarm);
                    }
                }
            }
        }
    }

    private void removeMedNameToSet(Alarm alarm) {
        getNameSet(alarm.time).remove(alarm.medName);
    }

    public void changeAlarmTime(Alarm alarm, long newTime) {
        if (isTimeAlreadyAdded(alarm.time)) {
            if (getNameSet(alarm.time).size() == 1) {
                Set<String> medNames = getNameSet(alarm.time);
                alarmList.remove(alarm.time);

                alarmList.put(newTime, medNames);
            } else {
                removeMedNameToSet(alarm);

                alarm.time = newTime;
                addAlarm(alarm);
            }

            if (alarmEvent != null) {
                alarmEvent.onAlarmTimeChanged(alarm, newTime);
            }
        }
    }

    public void setAlarmEvent(AlarmEvent alarmEvent) {
        this.alarmEvent = alarmEvent;
    }

    public interface AlarmEvent {
        void onNewAlarmAdded(Alarm alarm);

        void onAlarmRemoved(Alarm alarm);

        void onNameAddedToSet(Alarm alarm);

        void onNameRemovedFromSet(Alarm alarm);

        void onAlarmTimeChanged(Alarm time, long newTime);
    }
}
