package com.komsic.android.medmanager.ui.main.schedule;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.komsic.android.medmanager.R;
import com.komsic.android.medmanager.data.model.Reminder;
import com.komsic.android.medmanager.util.CalendarUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by komsic on 4/2/2018.
 */

class MedScheduleAdapter extends
        RecyclerView.Adapter<MedScheduleAdapter.MedScheduleViewHolder> {

    private final Context context;
    private List<Map<Reminder, Set<String>>> dataList;
    private List<Map<Reminder, Set<String>>> mockdataList;

    MedScheduleAdapter(Context context) {
        this.context = context;
        dataList = new ArrayList<>();
        mockdataList = mock();
    }

    @NonNull
    @Override
    public MedScheduleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MedScheduleViewHolder(LayoutInflater
                .from(context)
                .inflate(R.layout.item_med_schedule, parent, false));
    }

    List<Map<Reminder, Set<String>>> mock() {
        List<Map<Reminder, Set<String>>> data = new ArrayList<>();

        Map<Reminder, Set<String>> d = new HashMap<>();

        Map<String, Boolean> dayStates = new HashMap<>();
        dayStates.put("sun", true);
        dayStates.put("mon", true);
        dayStates.put("tue", true);
        dayStates.put("wed", true);
        dayStates.put("thu", true);
        dayStates.put("fri", true);
        dayStates.put("sat", true);

        Set<String> med = new HashSet<>();
        med.add("Norflor TZ");
        med.add("Vitamin C");
        med.add("Diet Pill");
        med.add("Vitamins");
        med.add("Fiber Active");

        Reminder reminder1 = new Reminder(calc(10), dayStates);
        d.put(reminder1, med);
        data.add(d);
        d.clear();

        Reminder reminder2 = new Reminder(calc(13), dayStates);
        d.put(reminder2, med);
        data.add(d);
        d.clear();

        Reminder reminder3 = new Reminder(calc(18), dayStates);
        d.put(reminder3, med);
        data.add(d);
        d.clear();

        return data;
    }

    long calc(int time) {
        return time * 3600000;
    }

    @Override
    public void onBindViewHolder(@NonNull MedScheduleViewHolder holder, int position) {
        Map<Reminder, Set<String>> currentMapData = dataList.get(position);

//        Map<Reminder, Set<String>> currentMapData = mockdataList.get(position);
        Reminder currentReminder = getReminderFromMapData(currentMapData);

        holder.textTimeOfDay.setText(CalendarUtil.getTimeInString(currentReminder.getTimeOfDay()));

        Set<String> medNames = currentMapData.get(currentReminder);
        holder.populateScheduleList(medNames);
        holder.setIsRecyclable(false);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    void addScheduleList(List<Map<Reminder, Set<String>>> mDataList) {
        dataList.clear();
        this.dataList.addAll(mDataList);
        notifyDataSetChanged();
    }

    private Reminder getReminderFromMapData(Map<Reminder, Set<String>> reminderListMap) {
        if (reminderListMap != null) {
            return (Reminder) reminderListMap.keySet().toArray()[0];
        }

        return null;
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

