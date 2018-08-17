package com.komsic.android.medmanager.data.model;

import android.support.annotation.NonNull;

import com.google.firebase.database.Exclude;
import com.komsic.android.medmanager.util.CalendarUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * Created by komsic on 4/12/2018.
 */

public class Med implements Comparable<Med> {

    public String name;
    public String description;
    public String id;
    public long startDate;
    public long endDate;
    public List<Reminder> reminders = new ArrayList<>();

    public Med() {
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

    private Reminder getReminder(int reminderIndex) {
        return reminders.get(reminderIndex);
    }

    @Override
    public int compareTo(@NonNull Med compareMed) {
        return (int) (startDate - compareMed.startDate);
    }

    @Override
    public String toString() {
        return name + " | "
                + description + " | "
                + CalendarUtil.getDateInString(startDate) + " | "
                + CalendarUtil.getDateInString(endDate) + " | "
                + new HashSet<>(reminders) + "\n";
    }

    public void update(Med med) {
        name = med.name;
        description = med.description;
        startDate = med.startDate;
        endDate = med.endDate;
        id = med.id;
        reminders = med.reminders;
    }

    @Exclude
    public void removeReminderDayState(int reminderPosition) {
        reminders.remove(reminderPosition);
    }

    public Map<String, Boolean> getDayStateMap(int reminderPosition) {
        return getReminder(reminderPosition).dayStates;
    }

    public void updateCurrentReminderDayState(boolean status, int dayOfTheWeek, int reminderIndex) {
        getReminder(reminderIndex).updateCurrentReminderDayState(status, dayOfTheWeek);
    }

    public boolean getCurrentReminderDayState(int reminderIndex, int dayOfTheWeek) {
        return getReminder(reminderIndex).getDayState(dayOfTheWeek);
    }

    public void updateReminderTime(int reminderPosition, long timeInMillis) {
        getReminder(reminderPosition).setTimeOfDay(timeInMillis);
    }

    public static int get(List<Med> medList, String medId) {

        for (int i = 0; i < medList.size(); i++) {
            Med med = medList.get(i);
            if (med.id.equals(medId)) {
                return i;
            }
        }

        return -1;
    }
}
