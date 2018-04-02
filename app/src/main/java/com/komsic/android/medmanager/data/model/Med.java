package com.komsic.android.medmanager.data.model;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.Exclude;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by komsic on 4/12/2018.
 */

public class Med {
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



    // [START post_to_map]
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
    // [END post_to_map]

    @Exclude
    public void addReminder(Reminder newReminder) {
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
}
