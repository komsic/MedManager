package com.komsic.android.medmanager.data.model;

import android.support.annotation.NonNull;

import com.google.firebase.database.Exclude;
import com.komsic.android.medmanager.util.CalendarUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by komsic on 4/13/2018.
 */

public class Reminder implements Comparable<Reminder> {
    private long timeOfDay;
    public Map<String, Boolean> dayStates = new HashMap<>();

    @Exclude
    public static String[] daysOfTheWeek = {"sun", "mon", "tue", "wed", "thu", "fri", "sat"};

    public Reminder() {
    }

    @Override
    public int compareTo(@NonNull Reminder compareReminder) {
        return (int) (timeOfDay - compareReminder.timeOfDay);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Reminder && CalendarUtil.convertToTime(timeOfDay) ==
                CalendarUtil.convertToTime(((Reminder) obj).timeOfDay);
    }

    @Override
    public int hashCode() {
        return (int) timeOfDay;
    }

    public void init() {
        timeOfDay = Calendar.getInstance().getTimeInMillis();
        timeOfDay = CalendarUtil.convertToTime(timeOfDay);
        for (String s : daysOfTheWeek) {
            dayStates.put(s, true);
        }
    }

    public void setTimeOfDay(long timeOfDay) {
        this.timeOfDay = CalendarUtil.convertToTime(timeOfDay);
    }

    public long getTimeOfDay() {
        return timeOfDay;
    }

    @Exclude
    public boolean getDayState(int day){
        return dayStates.get(daysOfTheWeek[day]);
    }

    @Exclude
    public boolean getDateDayState(long date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE", Locale.getDefault());
        String day = simpleDateFormat.format(new Date(date));

        return dayStates.get(day.toLowerCase());
    }

    @Override
    public String toString() {
        return CalendarUtil.getTimeInString(timeOfDay) + " | " + dayStates + "\t";
    }

    public void updateCurrentReminderDayState(boolean status, int dayOfTheWeek) {
        dayStates.put(daysOfTheWeek[dayOfTheWeek], status);
    }
}
