package com.komsic.android.medmanager.ui.detail;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.komsic.android.medmanager.R;
import com.komsic.android.medmanager.data.model.Reminder;
import com.komsic.android.medmanager.util.CalendarUtil;
import com.komsic.android.medmanager.util.Util;

import java.util.ArrayList;
import java.util.List;

public class ReminderItemAdapter
        extends RecyclerView.Adapter<ReminderItemAdapter.ReminderItemViewHolder> {

    private List<Reminder> mReminderList;
    private ReminderItemAdapterEvent mReminderItemAdapterEvent;

    public ReminderItemAdapter(ReminderItemAdapterEvent reminderItemAdapterEvent) {
        mReminderList = new ArrayList<>();
        mReminderItemAdapterEvent = reminderItemAdapterEvent;
    }

    @NonNull
    @Override
    public ReminderItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ReminderItemViewHolder(
                LayoutInflater
                        .from(parent.getContext())
                        .inflate(R.layout.item_reminder, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ReminderItemViewHolder holder, int position) {

        final int index = position;

        final Reminder currentReminder = mReminderList.get(index);

        if (currentReminder != null) {
            holder.timeText.setText(CalendarUtil.getTimeInString(currentReminder.getTimeOfDay()));

            if (!currentReminder.dayStates.containsValue(false)) {
                holder.dayStates.setText(R.string.daily);
            } else if (!currentReminder.dayStates.containsValue(true)) {
                deleteReminder(index);
            } else {
                StringBuilder sb = new StringBuilder();
                List<String> sortedDays = Util.sortDaysOfWeek(currentReminder.dayStates.keySet());
                for (String s : sortedDays) {
                    if (currentReminder.dayStates.get(s)) {
                        sb.append(s).append(" ");
                    }
                }
                holder.dayStates.setText(sb.toString());
            }

            holder.deleteItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteReminder(index);
                }
            });

            holder.dayStates.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mReminderItemAdapterEvent.onDayReminderClick(index);
                }
            });

            holder.timeText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mReminderItemAdapterEvent.onTimeReminderClick(index,
                            currentReminder.getTimeOfDay());
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mReminderList.size();
    }

    private void deleteReminder(int position) {
        mReminderList.remove(position);
        notifyDataSetChanged();
        mReminderItemAdapterEvent.removeReminderDayState(position);
    }

    public void updateList(Reminder reminder) {
        mReminderList.add(reminder);
        notifyDataSetChanged();
    }

    public void updateList(List<Reminder> reminders) {
        mReminderList.clear();
        mReminderList.addAll(reminders);
        notifyDataSetChanged();
    }

    public interface ReminderItemAdapterEvent {
        void removeReminderDayState(int position);

        void onDayReminderClick(int position);

        void onTimeReminderClick(int position, long time);
    }

    class ReminderItemViewHolder extends RecyclerView.ViewHolder {

        TextView timeText;
        TextView dayStates;
        ImageView deleteItem;

        public ReminderItemViewHolder(View itemView) {
            super(itemView);

            timeText = itemView.findViewById(R.id.text_time_of_day);
            dayStates = itemView.findViewById(R.id.text_day);
            deleteItem = itemView.findViewById(R.id.image_delete);
        }
    }
}
