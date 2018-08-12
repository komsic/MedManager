package com.komsic.android.medmanager.data.model;

import android.support.annotation.NonNull;

import com.google.firebase.database.Exclude;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by komsic on 4/12/2018.
 */

public class Med implements Comparable<Med>{
    public String name;
    public String description;
    public String id;
    public long startDate;
    public long endDate;
    public List<Reminder> reminders = new ArrayList<>();

    public Med() {
    }

    public Med(String name, String description, long startDate, long endDate, String id, List<Reminder> reminders) {
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.id = id;
        this.reminders = reminders;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Med && startDate == ((Med) obj).startDate;
    }

    @Override
    public int hashCode() {
        return (int) startDate;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("name", name);
        result.put("description", description);
        result.put("startDate", startDate);
        result.put("endDate", endDate);
        result.put("id", id);
        result.put("reminders", reminders);

        return result;
    }

    @Exclude
    public void addReminder(Reminder newReminder) {
        newReminder.setTimeOfDay(newReminder.getTimeOfDay());
        reminders.add(newReminder);
    }

    @Exclude
    public void updateReminderDayState(int index, Map<String, Boolean> newDayState) {
        reminders.get(index).updateReminderDayStateMap(newDayState);
    }

    @Exclude
    public void updateReminderTimeOfDay(int index, long newTimeOfDay) {
        reminders.get(index).updateReminderTimeOfDay(newTimeOfDay);
    }

    @Exclude
    public void removeReminderDayState(Reminder reminder) {
        reminders.remove(reminder);
    }

    @Override
    public int compareTo(@NonNull Med compareMed) {
        return (int) (startDate - compareMed.startDate);
    }
}
