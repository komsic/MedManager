package com.komsic.android.medmanager.data.model;

import android.support.annotation.NonNull;

import com.google.firebase.database.Exclude;
import com.komsic.android.medmanager.util.CalendarUtil;

import java.util.Set;

/**
 * Created by komsic on 4/2/2018.
 */

public class Alarm implements Comparable<Alarm> {

    private long timeOfDay;
    public Set<String> medNames;

    public Alarm(long timeOfDay, Set<String> medNames) {
        this.timeOfDay = CalendarUtil.convertToTime(timeOfDay);
        this.medNames = medNames;
    }

    @Exclude
    @Override
    public boolean equals(Object obj) {
        return obj instanceof Alarm && timeOfDay == ((Alarm) obj).timeOfDay;
    }

    @Exclude
    @Override
    public int hashCode() {
        return (int) timeOfDay;
    }

    @Exclude
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (medNames != null){
            for (String s : medNames){
                sb.append(s).append(" | ");
            }
        }
        return CalendarUtil.getTimeInString(timeOfDay) + " || " + sb.toString(); //To change body of generated methods, choose Tools | Templates.
    }

    public long getTimeOfDay() {
        return CalendarUtil.convertToTime(timeOfDay);
    }

    public long getTimeFrom00HrsLong() {
        return CalendarUtil.getCurrentDate00HrsLong(timeOfDay);
    }

    @Override
    public int compareTo(@NonNull Alarm o) {
        return (int) (timeOfDay - o.timeOfDay);
    }
}