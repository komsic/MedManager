package com.komsic.android.medmanager.ui.main.schedule;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.komsic.android.medmanager.R;
import com.komsic.android.medmanager.data.model.Med;
import com.komsic.android.medmanager.data.model.Reminder;
import com.komsic.android.medmanager.util.CalendarUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * Created by komsic on 4/2/2018.
 */

class MedScheduleAdapter extends
        RecyclerView.Adapter<MedScheduleAdapter.MedScheduleViewHolder> {

    private final Context context;
    private ArrayList<Med> mMedList;
    private long mSelectedDate;
    List<Map<Reminder, Set<String>>> dataList;
    Map<Reminder, Set<String>> data;

    public MedScheduleAdapter(Context context, long selectedDate) {
        this.context = context;
        this.mMedList = new ArrayList<>();
        this.mSelectedDate = selectedDate;
        data = new HashMap<>();
        dataList = new ArrayList<>();
    }

    @Override
    public MedScheduleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MedScheduleViewHolder(LayoutInflater
                .from(context)
                .inflate(R.layout.item_med_schedule, parent, false));
    }

    @Override
    public void onBindViewHolder(MedScheduleViewHolder holder, int position) {
        Map<Reminder, Set<String>> currentMapData = dataList.get(position);
        Reminder currentReminder = getReminderFromMapData(currentMapData);

        holder.textTimeOfDay.setText(CalendarUtil.getTimeInString(currentReminder.timeOfDay));

        Set<String> medNames = currentMapData.get(currentReminder);
        holder.populateScheduleList(medNames);
        holder.setIsRecyclable(false);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    void addMed(Med med) {
        mMedList.add(med);
        init();
        notifyItemInserted(data.size() - 1);
    }

    public Reminder getReminderFromMapData(Map<Reminder, Set<String>> reminderListMap) {
        if (reminderListMap != null) {
            return (Reminder) reminderListMap.keySet().toArray()[0];
        }

        return null;
    }

    void makeToast(Object s) {
        Toast.makeText(context, String.valueOf(s), Toast.LENGTH_SHORT).show();
    }

    void init() {
        for (Med med : mMedList) {
            for (Reminder reminder : med.reminders) {
                if (mSelectedDate >= med.startDate && mSelectedDate <= med.endDate) {
                    if (reminder.getDateDayState(mSelectedDate)) {
                        if (!data.containsKey(reminder)) {
                            data.put(reminder, new HashSet<String>());
                        }
                        data.get(reminder).add(med.name);
                    }
                }
            }
        }

        List<Reminder> sortedKeySet = new ArrayList<>(data.keySet());
        Collections.sort(sortedKeySet);
        dataList.clear();
        for (Reminder reminder : sortedKeySet) {
            Map<Reminder, Set<String>> dataMap = new HashMap<>();
            dataMap.put(reminder, data.get(reminder));
            dataList.add(dataMap);
        }

//        long currentTime = CalendarUtil.getCurrentTime();
//        if (getDateInString(currentTime).equals(getDateInString(mSelectedDate))) {
//            Intent alarmIntent = new Intent(ACTION_NOTIFY);
//            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//            for (int i = 0; i < dataList.size(); i++) {
//                Map<Reminder, Set<String>> currentMapData = dataList.get(i);
//                Reminder currentReminder = getReminderFromMapData(currentMapData);
//                ArrayList<String> medNames = new ArrayList<>(currentMapData.get(currentReminder));
//                alarmIntent.putStringArrayListExtra("medNames", medNames);
//
//                ArrayList<PendingIntent> pendingIntents = new ArrayList<>();
//                for (int count = 0; count < dataList.size(); count++) {
//                    pendingIntents.add(PendingIntent.getBroadcast(context, count, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT));
//                }
//
//                for (PendingIntent pendingIntent : pendingIntents) {
//                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, getTimeInMili(currentReminder.timeOfDay), pendingIntent);
//                }
//            }
//        }
    }

    void setDate(Date date) {
        mSelectedDate = date.getTime();
        data.clear();
        dataList.clear();
        init();
        notifyDataSetChanged();
    }

    class MedScheduleViewHolder extends RecyclerView.ViewHolder {
        TextView textTimeOfDay;
        LinearLayout scheduleLayout;

        MedScheduleViewHolder(View itemView) {
            super(itemView);
            textTimeOfDay = itemView.findViewById(R.id.text_time_of_the_day);
            scheduleLayout = itemView.findViewById(R.id.linear_layout_schedule);
        }

        private View addViewToSchedule(String medName) {
            @SuppressLint("InflateParams")
            View itemView = ((AppCompatActivity) context).getLayoutInflater()
                    .inflate(R.layout.item_med_name_list, null);
            CheckBox scheduleCheckBox = itemView.findViewById(R.id.checkbox);
            scheduleCheckBox.setText(medName);

            return itemView;
        }

        void populateScheduleList(Set<String> medNames) {
            for (String s : medNames) {
                View view = addViewToSchedule(s);
                scheduleLayout.addView(view);
            }

        }
    }
}

