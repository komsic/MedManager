package com.komsic.android.medmanager.ui.main.schedule;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.komsic.android.medmanager.R;
import com.komsic.android.medmanager.data.model.Alarm;
import com.komsic.android.medmanager.util.CalendarUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by komsic on 4/2/2018.
 */

class MedScheduleAdapter extends
        RecyclerView.Adapter<MedScheduleAdapter.MedScheduleViewHolder> {

    private static final String TAG = "MedScheduleAdapter";

    private final Context context;
    private List<Alarm> dataList;

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
        Alarm currentAlarm = dataList.get(position);


        holder.textTimeOfDay.setText(CalendarUtil.getTimeInString(currentAlarm.getTimeOfDay()));

        Set<String> medNames = currentAlarm.medNames;
//        holder.populateScheduleList(medNames);
        ItemScheduleAdapter adapter = new ItemScheduleAdapter(new ArrayList<>(medNames));
        holder.scheduleLayout.setAdapter(adapter);
        holder.setIsRecyclable(false);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    void addScheduleList(List<Alarm> mDataList) {
        dataList.clear();
        this.dataList.addAll(mDataList);
        notifyDataSetChanged();
    }

    class MedScheduleViewHolder extends RecyclerView.ViewHolder {
        TextView textTimeOfDay;
        RecyclerView scheduleLayout;

        MedScheduleViewHolder(View itemView) {
            super(itemView);
            textTimeOfDay = itemView.findViewById(R.id.text_time_of_the_day);
            scheduleLayout = itemView.findViewById(R.id.recycler_view_schedule);
            scheduleLayout.setLayoutManager(new LinearLayoutManager(itemView.getContext()));
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
                Log.e(TAG, "populateScheduleList: " + view);
                scheduleLayout.addView(view);
            }
        }
    }
}

