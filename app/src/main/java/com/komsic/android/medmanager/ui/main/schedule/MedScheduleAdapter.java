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
import android.widget.Toast;

import com.komsic.android.medmanager.R;
import com.komsic.android.medmanager.data.model.Reminder;
import com.komsic.android.medmanager.util.CalendarUtil;

import java.util.ArrayList;
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
    MedScheduleAdapter(Context context) {
        this.context = context;
        dataList = new ArrayList<>();
    }

    @NonNull
    @Override
    public MedScheduleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MedScheduleViewHolder(LayoutInflater
                .from(context)
                .inflate(R.layout.item_med_schedule, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MedScheduleViewHolder holder, int position) {
        Map<Reminder, Set<String>> currentMapData = dataList.get(position);
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

    void makeToast(Object s) {
        Toast.makeText(context, String.valueOf(s), Toast.LENGTH_SHORT).show();
    }

    void init() {


//        long currentTime = CalendarUtil.getCurrentTime();
//        if (CalendarUtil.getDateInString(currentTime).equals(CalendarUtil.getDateInString(mSelectedDate))) {
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
//                    pendingIntents.add(PendingIntent.getBroadcast(context, count, alarmIntent,
// PendingIntent.FLAG_UPDATE_CURRENT));
//                }
//
//                for (PendingIntent pendingIntent : pendingIntents) {
//                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, getTimeInMili(currentReminder.timeOfDay), pendingIntent);
//                }
//            }
//        }
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

